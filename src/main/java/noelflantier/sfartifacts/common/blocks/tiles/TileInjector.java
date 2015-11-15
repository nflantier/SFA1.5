package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.InjectorRecipe;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;

public class TileInjector extends TileMachine implements ITileUsingMaterials,ITileGlobalNBT{

	//PROCESSING
	public int tickToInject = 10;
	public boolean isRunning[] = new boolean[3];
	public int currentTickToInject[] = new int[3];
	public int currentRecipeId[] = new int[3];

	//INVENTORY
	public ItemStack[] items = new ItemStack[13];
	
	public TileInjector(){
		super("Injector");
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
		//this.naturalEnergy = this.getNaturalEnergy(this.getBlockMetadata());
		for(ForgeDirection f:ForgeDirection.values()){
			this.recieveSides.add(f);
			this.extractSides.add(f);
		}
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar = new ArrayList<Integer>();
		ar.add(ForgeDirection.getOrientation(this.side).getOpposite().ordinal());
		ar.add(ForgeDirection.UNKNOWN.ordinal());
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, ar);
	}

	@Override
	public void processPackets() {	
		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
		PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
		PacketHandler.sendToAllAround(new PacketInjector(this),this);
	}
    
	@Override
	public void processMachine() {
        if(!this.isRedStoneEnable){
        	if(this.tank.getFluidAmount()<=this.tank.getCapacity()){
	    		for(int or : this.fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite)){
	    			ForgeDirection fd = ForgeDirection.getOrientation(or);
	    			TileEntity t = this.worldObj.getTileEntity(this.xCoord+fd.offsetX,this.yCoord+fd.offsetY, this.zCoord+fd.offsetZ);
	        		if(t !=null && t instanceof IFluidHandler && t instanceof TileBlockPillar){
	        			ForgeDirection fdt = fd.getOpposite();
	        			FluidStack av = ((IFluidHandler)t).drain(fdt, new FluidStack(ModFluids.fluidLiquefiedAsgardite, 1000), false);
	        			if(av!=null && av.amount>0){
	    	    			int ci = this.fill(fd, av, false);
	    	    			if(ci>0){
	    	    				((IFluidHandler)t).drain(fdt, ci, true);
	    	    				this.fill(fd, av, true);
	    	    			}
	        			}
	        		}
	    		}
        	}
        	this.processInjecting();
        	this.processInventory();
        }
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
	
	public void isRunningProcess(int idline){
		if(this.currentRecipeId[idline]!=-1){
			InjectorRecipe cr = InjectorRecipe.values()[this.currentRecipeId[idline]];
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=cr.energyAmount/this.tickToInject 
					&& this.tank.getFluidAmount()>=cr.fluidAmount/this.tickToInject){
				this.currentTickToInject[idline]-=1;
				
				if(this.getRandom(this.getBlockMetadata(),this.randomMachine)){
					this.extractEnergy(ForgeDirection.UNKNOWN, cr.energyAmount/this.tickToInject, false);
					this.tank.drain(cr.fluidAmount/this.tickToInject, true);
				}
				
				if(this.currentTickToInject[idline]<=0){
					//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					for(int idcaseres =0;idcaseres<2;idcaseres++){
						if(this.items[idline*2+idcaseres+6+1]==null){
							this.items[idline*2+idcaseres+6+1] = new ItemStack(cr.result);
							this.isRunning[idline] = false;
							this.currentRecipeId[idline]=-1;
							return;
						}else if(this.items[idline*2+idcaseres+6+1].getItem()==cr.result){
							if(this.items[idline*2+idcaseres+6+1].stackSize+1<=this.items[idline*2+idcaseres+6+1].getMaxStackSize()){
								this.items[idline*2+idcaseres+6+1].stackSize++;
								this.isRunning[idline] = false;
								this.currentRecipeId[idline]=-1;
								return;
							}
						}
					}
				}
			}
		}
	}

	public void isNotRunningProcess(int idline){
		this.currentTickToInject[idline] = this.tickToInject;
		this.currentRecipeId[idline]=-1;
		InjectorRecipe irf = null;

		for(int idcase =0;idcase<2;idcase++){
			if(this.items[idline*2+idcase+1]!=null){

				for(InjectorRecipe ir : InjectorRecipe.values()){
					if(ir.recipe.size()>1){
						int ad = (idcase==0)?1:-1;
						if(this.checkContainsItem(ir, this.items[idline*2+idcase+1]) && this.checkContainsItem(ir, this.items[idline*2+idcase+ad+1])  
								&& this.items[idline*2+idcase+1].getItem()!=this.items[idline*2+idcase+ad+1].getItem()){
							irf = ir;
							break;
						}
					}else{
						if(this.checkContainsItem(ir, this.items[idline*2+idcase+1])){
							irf = ir;
							break;
						}
					}
				}
				
			}
			if(irf!=null && this.tank.getFluidAmount()>=irf.fluidAmount 
					&& this.getEnergyStored(ForgeDirection.UNKNOWN)>=irf.energyAmount){
				for(int idcaseres =0;idcaseres<2;idcaseres++){
					if( ( this.items[idline*2+idcaseres+6+1]!=null && this.items[idline*2+idcaseres+6+1].stackSize+1<=this.items[idline*2+idcaseres+6+1].getMaxStackSize() 
							&& this.items[idline*2+idcaseres+6+1].getItem()==irf.result ) || this.items[idline*2+idcaseres+6+1]==null){
						this.currentRecipeId[idline] = irf.ordinal();
						this.isRunning[idline] = true;
						
						if(irf.recipe.size()>1){// && idcaseres == 0
							int itsize = getStackSize(irf,this.items[idline*2+idcase+1]);
							if(this.items[idline*2+idcase+1].stackSize==itsize){
								this.items[idline*2+idcase+1]=null;
							}else
								this.items[idline*2+idcase+1].stackSize-=itsize;
							
							int itsize2 = getStackSize(irf,this.items[idline*2+idcase+1+1]);
							if(this.items[idline*2+idcase+1+1].stackSize==itsize2){
								this.items[idline*2+idcase+1+1]=null;
							}else
								this.items[idline*2+idcase+1+1].stackSize-=itsize2;
						}
						if(irf.recipe.size()<=1){				
							if(this.items[idline*2+idcase+1].stackSize==irf.recipe.get(0).stackSize){
								this.items[idline*2+idcase+1]=null;
							}else
								this.items[idline*2+idcase+1].stackSize-=irf.recipe.get(0).stackSize;
						}
						return;
					}
				}
			}
		}
	}

	public int getStackSize(InjectorRecipe ir, ItemStack stack){
		for(ItemStack sta : ir.recipe){
			if(sta.getItem()==stack.getItem() && sta.getItemDamage()==stack.getItemDamage()){
				return sta.stackSize;
			}
		}
		return 0;
	}
	
	public boolean checkContainsItem(InjectorRecipe ir, ItemStack stack){
		for(ItemStack sta : ir.recipe){
			if(stack!=null && sta.getItem()==stack.getItem() && sta.getItemDamage()==stack.getItemDamage() && sta.stackSize<=stack.stackSize)
				return true;
		}
		return false;
	}
	
	public boolean processInjecting(){
		for(int i=0;i<this.isRunning.length;i++){
			if(this.isRunning[i]){
				this.isRunningProcess(i);
			}else{
				this.isNotRunningProcess(i);
			}
		}
		return false;
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
        
        for(int i =0;i<this.isRunning.length;i++)
        	nbt.setBoolean("isRunning"+i, this.isRunning[i]);

        for(int i =0;i<this.currentTickToInject.length;i++)
        	nbt.setInteger("currentTickToInject"+i, this.currentTickToInject[i]);

        for(int i =0;i<this.currentRecipeId.length;i++)
        	nbt.setInteger("currentRecipeId"+i, this.currentRecipeId[i]);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        for(int i =0;i<this.isRunning.length;i++)
    		this.isRunning[i] = nbt.getBoolean("isRunning"+i);

        for(int i =0;i<this.currentTickToInject.length;i++)
    		this.currentTickToInject[i] = nbt.getInteger("currentTickToInject"+i);
        
        for(int i =0;i<this.currentRecipeId.length;i++)
    		this.currentRecipeId[i] = nbt.getInteger("currentRecipeId"+i);
    }
    
	@Override
	public ItemStack[] getItems() {
		return this.items;
	}
	

	@Override
	public String getInventoryName() {
		return "Injector";
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return  ( slot==0 && fluid != null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite ) || (slot>0 && slot<7);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
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
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
}
