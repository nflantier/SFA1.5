package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.items.ItemAsgardite;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketLiquefier;

public class TileLiquefier extends TileMachine implements ITileUsingMaterials,ITileGlobalNBT{
	
	//PROCESSING
	public boolean isRunning;
	public int tickToMelt = 10;
	public int currentTickToMelt;
	public int energyNeededPerOneItem = 100;
	public FluidStack fluidNeededPerOneItem = new FluidStack(FluidRegistry.WATER, 200);
	public FluidStack fluidGivenPerOneItem = new FluidStack(ModFluids.fluidLiquefiedAsgardite, 1000);
	public FluidTank tankMelt = new FluidTank(10000);
	public int[][] offsetWater = new int[][]{{0,-1,0},{1,-1,0},{-1,-1,0},{0,-1,1},{1,-1,1},{-1,-1,1},{0,-1,-1},{1,-1,-1},{-1,-1,-1}};
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[3];
	
	public TileLiquefier() {
		super("Liquefier");
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
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar = new ArrayList<Integer>();
		ar.add(ForgeDirection.getOrientation(this.side).getOpposite().ordinal());
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, ar);
		List<Integer> ar2 = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(4);add(5);add(ForgeDirection.UNKNOWN.ordinal());}};
		this.fluidAndSide.put(FluidRegistry.WATER, ar2);
	}

	@Override
	public void processPackets() {
        PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
        PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount(), this.tankMelt.getFluidAmount()}, new int[]{this.tank.getCapacity(), this.tankMelt.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID(), FluidRegistry.WATER.getID()}),this);
        PacketHandler.sendToAllAround(new PacketLiquefier(this),this);
	}

	@Override
    public void processAtRandomTicks(){
		if(this.getFluidTanks().get(1).getFluidAmount()>=this.getFluidTanks().get(1).getCapacity())
			return;
		int wateramount = 0;
		if(!this.getRandom(this.getBlockMetadata(),this.randomMachine)){
			wateramount += 15;
		}
    	for(int i = 0;i<this.offsetWater.length;i++){
    		if(this.worldObj.getBlock(xCoord+this.offsetWater[i][0], yCoord+this.offsetWater[i][1], zCoord+this.offsetWater[i][2])==Blocks.water)
    			wateramount+=1;
    	}
    	this.fill(ForgeDirection.DOWN, new FluidStack(FluidRegistry.WATER,wateramount), true);
    }

	@Override
    public float getRandomTickChance(){
    	return 0.18F;
    }
    
	@Override
	public void processMachine() {
        if(!this.isRedStoneEnable){
        	if(this.tank.getFluidAmount()>0){
				for(int or : this.fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite)){
					ForgeDirection fd = ForgeDirection.getOrientation(or);
					TileEntity t = this.worldObj.getTileEntity(this.xCoord+fd.offsetX,this.yCoord+fd.offsetY, this.zCoord+fd.offsetZ);
		    		if(t !=null && t instanceof IFluidHandler){
		        		ForgeDirection fdt = fd.getOpposite();	
		    			FluidStack av = this.drain(fd, new FluidStack(ModFluids.fluidLiquefiedAsgardite, 1000), false);
		    			if(av!=null && av.amount>0){
			    			int ci = ((IFluidHandler)t).fill(fdt, av, false);
			    			av.amount = ci;
			    			if(ci>0){
			    				this.drain(fd, ci, true);
			    				((IFluidHandler)t).fill(fdt, av, true);
			    			}
		    			}
		    		}
				}
        	}
	    	this.processInventory();
	    	this.processLiquefying();
        }
	}
	
	public boolean processInventory(){
		if(this.items[1]!=null){
			if(this.tankMelt.getFluidAmount()<this.tankMelt.getCapacity()){
				FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.items[1]);
				if(fluid!=null && fluid.amount>0){
					ItemStack emptyItem = this.items[1].copy();
					int avail = this.tankMelt.fill(fluid, true);
					fluid.amount = fluid.amount-avail;
				    if(this.items[1].getItem().hasContainerItem(this.items[1])) {
				    	emptyItem = this.items[1].getItem().getContainerItem(this.items[1]);
				    }
					FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
					this.items[1] = emptyItem;
				}
			}
		}
		if(this.items[2]!=null){
			if(!FluidContainerRegistry.isFilledContainer(this.items[2])){
				FluidStack fsavail = this.tank.drain(1000, false);
				if(fsavail!=null && fsavail.amount>0){
					this.items[2] = FluidContainerRegistry.fillFluidContainer(fsavail, this.items[2]);
					this.tank.drain(fsavail.amount, true);
				}
			}
		}
		return true;
	}
	public boolean processLiquefying(){
		if(!this.isRunning){
			this.currentTickToMelt = this.tickToMelt;
			if(this.items[0]!=null && this.getEnergyStored(ForgeDirection.UNKNOWN)-this.energyNeededPerOneItem>=0 && this.tankMelt.getFluidAmount()>=this.fluidNeededPerOneItem.amount && this.tank.getFluidAmount()+this.fluidGivenPerOneItem.amount<=this.tank.getCapacity()){
				this.isRunning = true;
				if(this.items[0].stackSize==1)
					this.items[0]=null;
				else
					this.items[0].stackSize--;
			}else
				return false;
		}else{
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=this.energyNeededPerOneItem/this.tickToMelt && this.tankMelt.getFluidAmount()>=this.fluidNeededPerOneItem.amount/this.tickToMelt){
				this.currentTickToMelt -= 1;
				FluidStack fs = this.fluidGivenPerOneItem.copy();
				fs.amount = fs.amount/this.tickToMelt;
				this.tank.fill(fs, true);
				if(this.getRandom(this.getBlockMetadata(),this.randomMachine)){
					this.tankMelt.drain(this.fluidNeededPerOneItem.amount/this.tickToMelt, true);
					this.extractEnergy(ForgeDirection.UNKNOWN, this.energyNeededPerOneItem/this.tickToMelt, false);
				}
				if(this.currentTickToMelt<=0){
					this.isRunning = false;
					return true;
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

        nbt.setString("FluidName2", FluidRegistry.getFluidName(FluidRegistry.WATER));
        nbt.setInteger("Amount2", this.tankMelt.getFluidAmount());
		
		nbt.setBoolean("isRunning", this.isRunning);
		nbt.setInteger("currentTickToMelt", this.currentTickToMelt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        
        if(nbt.getString("FluidName2")!=null)
        	this.tankMelt.setFluid(new FluidStack(FluidRegistry.getFluid(nbt.getString("FluidName2")), nbt.getInteger("Amount2")));
		
		this.isRunning = nbt.getBoolean("isRunning");
		this.currentTickToMelt = nbt.getInteger("currentTickToMelt");
    }
    
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(!this.canFill(from, resource.getFluid()) || this.isRedStoneEnable)
			return 0;
		if(resource.getFluid()==FluidRegistry.WATER)
			return this.tankMelt.fill(resource, doFill);
		
		return this.tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(this.canDrain(from, resource.getFluid()))
			return drain(from, this.isRedStoneEnable?0:resource.amount, doDrain);
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank.drain(this.isRedStoneEnable?0:maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(!this.init || fluid==null || !this.fluidAndSide.containsKey(fluid) || !this.fluidAndSide.get(fluid).contains(from.ordinal()) )
			return false;
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(!this.init || fluid == null || !this.fluidAndSide.get(fluid).contains(from.ordinal()) )
			return false;
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	@Override
	public List<FluidTank> getFluidTanks() {
		List<FluidTank> ft = new ArrayList<FluidTank>();
		ft.add(this.tank);
		ft.add(this.tankMelt);
		return ft;
	}
	
	
	//INVENTORY
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.items[slot] = stack;
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Liquefier";
	}


	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return ( slot==0 && stack.getItem() instanceof ItemAsgardite ) || ( slot==1 && fluid != null && fluid.getFluid()==FluidRegistry.WATER ) 
				|| ( slot==2 && ( ( FluidContainerRegistry.isContainer(stack) || stack.getItem() == Items.bucket ) || ( fluid != null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite ) )   );
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack))
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack))
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
