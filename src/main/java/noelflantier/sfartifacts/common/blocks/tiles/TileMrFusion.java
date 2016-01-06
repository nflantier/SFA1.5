package noelflantier.sfartifacts.common.blocks.tiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;

public class TileMrFusion extends TileMachine{

	//RATIO
	public static float ratioFood = 3;
	public static float ratioLiquid = 2.5F;
	public static float ratioDefault= 1;
	
	//RF
	public static int rf = 5;
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[64];
	
	public TileMrFusion(){
		super("MrFusion");
		this.hasFL = true;
		this.hasRF = true;
    	this.energyCapacity = ModConfig.capacityMrFusion;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.tankCapacity = ModConfig.capacityLiquidMrFusion;
		this.tank.setCapacity(this.tankCapacity);
	}	

	@Override
	public void init(){
		super.init();
		this.extractSides.add(ForgeDirection.getOrientation(side));
		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}

	@Override
	public void processPackets() {
		//if(this.getEnergyStored(ForgeDirection.UNKNOWN)!=this.lastEnergyStoredAmount)
    		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
		PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
		
	}
	
	public boolean processInventory(){
		if(this.items[12]!=null){
			if(this.tank.getFluidAmount()<this.tank.getCapacity()){
				FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.items[12]);
				if(fluid!=null && fluid.amount>0){
					ItemStack emptyItem = this.items[12].copy();
					int avail = this.tank.fill(fluid, true);
					fluid.amount = fluid.amount-avail;
				    if(this.items[12].getItem().hasContainerItem(this.items[12])) {
				    	emptyItem = this.items[12].getItem().getContainerItem(this.items[12]);
				    }
					FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
					this.items[12] = emptyItem;
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public void processMachine() {
		processInventory();
		processRfGain();
		if(!this.extractSides.isEmpty()){
    		for(ForgeDirection fd : this.extractSides){
        		int maxAvailable = this.extractEnergy(fd, this.getEnergyStored(fd), true);
        		int energyTransferred = 0;
    			TileEntity tile = worldObj.getTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
    			if(tile!=null && tile instanceof IEnergyHandler){
    				energyTransferred = ((IEnergyHandler) tile).receiveEnergy(fd.getOpposite(), maxAvailable, true);
    				if(energyTransferred!=0){
    					energyTransferred = ((IEnergyHandler) tile).receiveEnergy(fd.getOpposite(), maxAvailable, false);
        				this.extractEnergy(fd, energyTransferred, false);
    				}
    			}
    		}
    	}
	}

	public void processRfGain(){
		if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=this.energyCapacity)
			return;
		float rfg = (float)0;
		rfg += (float)this.tank.getFluidAmount()*this.ratioLiquid;
		int m  = (int)(this.tank.getFluidAmount()/this.tankCapacity)*10;
		m = m<=0?1:m;
		this.tank.drain(this.tankCapacity, true);
		this.storage.receiveEnergy((int)rfg, false);
		if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=this.energyCapacity)
			return;

		for(int i = 0 ; i < this.items.length-1 ; i++){
			if(this.items[i]==null)
				continue;
			rfg = 0;
			if(Utils.getAllSuperclasses(this.items[i].getItem().getClass()).contains(ItemFood.class)){//FOOD
				rfg+=this.ratioFood*(float)this.rf*(float)this.items[i].stackSize*m;
			}else{
				rfg+=this.ratioDefault*(float)this.rf*(float)this.items[i].stackSize;
			}
			this.items[i] = null;
			this.storage.receiveEnergy((int)rfg*m, false);
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=this.energyCapacity)
				return;
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return (slot == 63 && FluidContainerRegistry.isContainer(stack)) || slot<63;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}
	
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from)?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public ItemStack[] getItems() {
		return items;
	}
	
	@Override
	public void addToWaila(List<String> list) {
		list.add("Status : "+(this.worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0?"Processing and port disable":this.isManualyEnable?"Processing enable":"Processing disable")+"");
		list.add("Energy : "+NumberFormat.getNumberInstance().format(this.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(this.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
		if(this.getFluidTanks().size()>0 && hasFL){
			for(int i = 0 ; i < this.getFluidTanks().size() ; i++){
				list.add("Liquid : "+NumberFormat.getNumberInstance().format(this.getFluidTanks().get(i).getFluidAmount())+" MB / "+
					NumberFormat.getNumberInstance().format(this.getFluidTanks().get(i).getCapacity())+" MB");
			}
		}
	}

}
