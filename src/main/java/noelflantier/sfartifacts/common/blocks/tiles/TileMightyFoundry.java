package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Molds;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundry;

public class TileMightyFoundry extends TileMachine implements ITileGlobalNBT{

	//PROCESSING
	public boolean isLocked = false;
	public boolean isRunning = false;
	public ItemStack currentCasting = null;
	public double progression = 0.0D;
	public int currentID = -1;
	public final int initialTickToMelt = 1000;
	public int tickToMelt = 1000;
	public int currentTickToMelt = 0;
	public int energyNeededPerOneItem = 200;
	public FluidStack fluidNeededPerOneItem = new FluidStack(FluidRegistry.LAVA, 100);
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[7];
	
	public TileMightyFoundry() {
		super("Mighty Foundry");
		this.hasFL = true;
		this.hasRF = true;
    	this.energyCapacity = 100000;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity/100);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.tankCapacity = 100000;
		this.tank.setCapacity(this.tankCapacity);
	}
	
	@Override
	public void init(){
		super.init();
		for(ForgeDirection f:ForgeDirection.values()){
			this.recieveSides.add(f);
			this.extractSides.add(f);
		}
		if(this.currentTickToMelt==0)
			this.currentTickToMelt = this.tickToMelt;
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar2 = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(4);add(5);add(ForgeDirection.UNKNOWN.ordinal());}};
		this.fluidAndSide.put(FluidRegistry.LAVA, ar2);
	}
	
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
		PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{FluidRegistry.LAVA.getID()}),this);
		PacketHandler.sendToAllAround(new PacketMightyFoundry(this),this);  
	}

	@Override
	public void processMachine() {        
        this.processInventory();
        
        if(this.isLocked && this.getStackInSlot(1)!=null)
        	this.processFoundry();
        if(this.isRunning && this.getStackInSlot(1)==null){
			this.isRunning = false;
			this.progression = 0.0D;
        	this.currentID = -1;
			this.currentCasting = null;
        } 
	}
	
	public boolean processFoundry(){
		ItemStack mold = this.getStackInSlot(1);
		int mid = ItemNBTHelper.getInteger(mold, "idmold", 0);
		ItemStack ing = Molds.getMold(mid).ingredients;
		if(this.currentCasting!=null){
			this.progression = (double)this.currentCasting.stackSize/(double)ing.stackSize;
		}
		if(this.isRunning){
			if(this.currentTickToMelt<this.tickToMelt){
				this.currentTickToMelt+=1;
			}else{
				if(this.currentCasting.stackSize>=ing.stackSize){
					this.setInventorySlotContents(6, new ItemStack(Molds.getMold(mid).result.getItem(),1,0));
					this.setInventorySlotContents(1, null);
					this.isLocked = false;
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					this.progression = 0.0D;
					this.isRunning = false;
		        	this.currentID = -1;
					this.currentCasting = null;
				}else{
					if(this.tank.getFluidAmount()>=this.fluidNeededPerOneItem.amount){
						for(int i = 0 ;i<4;i++){
							ItemStack st =  this.getStackInSlot(2+i);
							if(st!=null && st.getItem()==this.currentCasting.getItem() && st.getItemDamage()==this.currentCasting.getItemDamage()){
								if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=this.energyNeededPerOneItem){
									this.tickToMelt = this.initialTickToMelt/10;
									this.extractEnergy(ForgeDirection.UNKNOWN, this.energyNeededPerOneItem, false);
								}else{
									this.tickToMelt = this.initialTickToMelt;
								}
								this.items[2+i].stackSize-=1;
								this.currentCasting.stackSize+=1;
								this.tank.drain(this.fluidNeededPerOneItem.amount, true);
								this.currentTickToMelt = 0;
								if(this.items[2+i].stackSize<=0)
									this.items[2+i] = null;
								break;
							}
						}
					}
				}
			}
		}else{
			this.isRunning = true;
			this.currentID = mid;
			this.currentCasting = new ItemStack(ing.getItem(),0,ing.getItemDamage());
		}
		
		return false;
	}
	
	public boolean processInventory(){
		if(this.items[0]!=null){
			if(this.tank.getFluidAmount()<this.tank.getCapacity()){
				FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.items[0]);
				if(fluid!=null && fluid.amount>0){
					ItemStack emptyItem = this.items[0].copy();
					int avail = this.tank.fill(fluid, true);
					fluid.amount = fluid.amount-avail;
				    if(this.items[0].getItem().hasContainerItem(this.items[0])) {
				    	emptyItem = this.items[0].getItem().getContainerItem(this.items[0]);
				    }
					FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
					this.items[0] = emptyItem;
				}
			}
		}
		return true;
	}
	public void writeToItem(ItemStack stack){
	    if(stack == null) {
	        return;
	    }
	    if(stack.stackTagCompound == null) {
	        stack.stackTagCompound = new NBTTagCompound();
	    }
	    NBTTagCompound root = stack.stackTagCompound;
	    writeToNBT(root);
	}

	public void readFromItem(ItemStack stack){
	    if(stack == null || stack.stackTagCompound == null) {
	        return;
	    }
	    readFromNBT(stack.stackTagCompound);
	}
	
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

		nbt.setBoolean("isLocked", this.isLocked);
		nbt.setBoolean("isRunning", this.isRunning);
		nbt.setInteger("currentID",this.currentID);
		nbt.setInteger("stackSizeCC",this.currentCasting!=null?this.currentCasting.stackSize:0);
		nbt.setInteger("tickToMelt",this.tickToMelt);
		nbt.setInteger("currentTickToMelt",this.currentTickToMelt);
		
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

		this.isLocked = nbt.getBoolean("isLocked");
		this.isRunning = nbt.getBoolean("isRunning");
		this.currentID = nbt.getInteger("currentID");
		if(this.currentID!=-1){
			ItemStack ing = Molds.getMold(this.currentID).ingredients;
			this.currentCasting = new ItemStack(ing.getItem(),nbt.getInteger("stackSizeCC"),ing.getItemDamage());
		}else
			this.currentCasting = null;
		this.tickToMelt = nbt.getInteger("tickToMelt");
		this.currentTickToMelt = nbt.getInteger("currentTickToMelt");
		
    }

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		ItemStack it = this.getStackInSlot(slot);
		if(slot==1 && this.isLocked)
			return null;
		if(it!=null){
			if(it.stackSize < i)
				this.setInventorySlotContents(slot, null);
			else{
				int s = it.stackSize;
				it = it.splitStack(i);
				if(s-i<=0)
					this.setInventorySlotContents(slot, null);
				this.markDirty();
			}
		}
		return it;
	}

	@Override
	public String getInventoryName() {
		return "MightyFoundry";
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return 	( slot==0 && fluid != null && fluid.getFluid()==FluidRegistry.LAVA ) || 
				( slot==1 && stack.getItem() instanceof ItemMold && ItemNBTHelper.getInteger(stack, "idmold", 0)>0 ) ||
				( slot>1 && slot<6 && this.getStackInSlot(1)!= null && isItemValidForSlot(1,this.getStackInSlot(1)) && this.isLocked );
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2,3,4,5};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack) && side!=0 || side!=1)
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack) && side!=0 || side!=1)
			return true;
		return false;
	}

	@Override
	public ItemStack[] getItems() {
		return this.items;
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
}
