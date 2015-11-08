package noelflantier.sfartifacts.common.blocks.tiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.PillarStructures;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;

public abstract class TileMachine extends TileSFA implements ITileCanHavePillar,ISFAFluid,
							ISFAEnergyHandler,ISidedInventory{
	
	//CONTROL
	public boolean isManualyEnable = true;
	public boolean hasRF = false;
	public boolean hasFL = false;
	
	//STRUCTURE
	public Coord4 master;
	
	//ENERGY
	public boolean isTransferCaped = false;
    public int energyCapacity = 0;
    //public int rf;
    public EnergyStorage storage = new EnergyStorage(0,0,0);
    public List<ForgeDirection> recieveSides = new ArrayList<>();
    public List<ForgeDirection> extractSides = new ArrayList<>();
    public int ratioTransfer = 100;
	public int lastEnergyStoredAmount = -1;
	
	//FLUID
   	public FluidTank tank = new FluidTank(0);
   	public int tankCapacity;
	public Hashtable<Fluid, List<Integer>> fluidAndSide;
	
	//machine
	
	public TileMachine(){
		
	}

	public TileMachine(String name){
		super(name);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
        if(this.worldObj.isRemote)
        	return;
        if(this.isManualyEnable)
        	processMachine();
        processPackets();
    }

	@Override
    public void setVariables(Object... params){
    	if(params[0] instanceof Coord4){
    		this.master = (Coord4)params[0];
    	}
    }
    
	public abstract void processPackets();
	public abstract void processMachine();
	
	@Override
	public void init(){
		super.init();
	}
	
	@Override
	public Coord4 getMasterCoord() {
		return this.master;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //STRUCTURE
        if(this.hasMaster()){
	        nbt.setInteger("masterx", this.getMasterX());
	        nbt.setInteger("mastery", this.getMasterY());
	        nbt.setInteger("masterz", this.getMasterZ());
        	nbt.setBoolean("hasmaster", true);
        }else
        	nbt.setBoolean("hasmaster", false);
        
        //FLUID
        this.tank.writeToNBT(nbt);
        //nbt.setInteger("tankCapacity", this.tankCapacity);
        
        //INVENTORY
        NBTTagCompound[] tag = new NBTTagCompound[this.getItems().length];
		for (int i = 0; i < this.getItems().length; i++)
		{
			tag[i] = new NBTTagCompound();
			if (this.getItems()[i] != null)
				tag[i] = this.getItems()[i].writeToNBT(tag[i]);
			nbt.setTag("Items" + i, tag[i]);
		}

		//ENERGY
        //nbt.setInteger("rf", this.getEnergyStored(ForgeDirection.UNKNOWN));
		this.storage.writeToNBT(nbt);
        nbt.setInteger("lastEnergyStoredAmount", this.lastEnergyStoredAmount);
        
      /*  int[] dirext = new int[this.extractSides.size()];
        int i = 0;
        for(ForgeDirection direction : this.extractSides){
        	dirext[i] = direction.ordinal();
        	i++;
        }
    	nbt.setIntArray("tabextract", dirext);

        int[] dirrec = new int[this.recieveSides.size()];
        int j = 0;
        for(ForgeDirection direction : this.recieveSides){
        	dirrec[j] = direction.ordinal();
        	j++;
        }
    	nbt.setIntArray("tabrecieve", dirrec);*/
    	
    	//Control
    	nbt.setBoolean("manualyEnabled", this.isManualyEnable);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //STRUCTURE
    	if(nbt.getBoolean("hasmaster"))
    		this.master = new Coord4(nbt.getInteger("masterx"),nbt.getInteger("mastery"),nbt.getInteger("masterz"));

    	//FLUID
        this.tank.readFromNBT(nbt);
		//this.tank.setCapacity(this.tankCapacity);
        //this.tankCapacity = nbt.getInteger("tankCapacity");
		//this.tank.setCapacity(this.tankCapacity);
		
		//INVENTORY
		NBTTagCompound[] tag = new NBTTagCompound[this.getItems().length];
		for (int i = 0; i < this.getItems().length; i++)
		{
			tag[i] = nbt.getCompoundTag("Items" + i);
			this.getItems()[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
        
		//ENERGY
		//this.storage.setCapacity(this.capacity);
		this.storage.readFromNBT(nbt);
		
		//this.storage.setMaxTransfer(this.capacity/this.ratioTransfer);
		//this.rf = nbt.getInteger("rf");
		this.lastEnergyStoredAmount = nbt.getInteger("lastEnergyStoredAmount");
		
        /*int[] dirext = nbt.getIntArray("tabextract");
        for(int i = 0 ; i < dirext.length ; i++){
        	this.extractSides.add(ForgeDirection.getOrientation(dirext[i]));
        }

        int[] dirrec = nbt.getIntArray("tabrecieve");
        for(int i = 0 ; i < dirrec.length ; i++){
        	this.recieveSides.add(ForgeDirection.getOrientation(dirrec[i]));
        }*/
        
        //Control
        this.isManualyEnable = nbt.getBoolean("manualyEnabled");
    }
	
    public abstract ItemStack[] getItems();
    
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(this.canFill(from, resource.getFluid()) && !this.isRedStoneEnable)
			return this.tank.fill(resource, doFill);
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(this.canDrain(from, resource.getFluid()))
			return this.drain(from, this.isRedStoneEnable?0:resource.amount, doDrain);
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank.drain(this.isRedStoneEnable?0:maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(!this.init || this.isRedStoneEnable || !this.isManualyEnable || fluid==null || this.fluidAndSide==null || !this.fluidAndSide.containsKey(fluid) 
				|| !this.fluidAndSide.get(fluid).contains(from.ordinal()) ){
			return false;
		}
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(!this.init || this.isRedStoneEnable || !this.isManualyEnable || fluid==null || this.fluidAndSide==null || !this.fluidAndSide.get(fluid).contains(from.ordinal()) ){
			return false;
		}
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
		return ft;
	}

	
	
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {		
		for(ForgeDirection direction : recieveSides)
		if(direction == from)
			return true;
		for(ForgeDirection direction : extractSides)
			if(direction == from)
				return true;
		return false;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable||!this.isManualyEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable||!this.isManualyEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int getEnergyStored(ForgeDirection from) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return this.storage.getMaxEnergyStored();
	}
	
	
	
	@Override
	public int getSizeInventory() {
		return this.getItems().length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.getItems()[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		ItemStack it = this.getStackInSlot(slot);
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
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack it = this.getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return it;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.getItems()[slot] = stack;
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory() {		
	}

	@Override
	public void closeInventory() {		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int slot) {
		int[] tab = new int[]{};
		for(int i = 0;i<getItems().length;i++)
			tab[i] = i;
		return tab;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack))
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return true;
	}

	@Override
	public void addToWaila(List<String> list) {
		list.add("Status : "+(this.isManualyEnable?"ON":"OFF")+"");
		if(this.hasMaster())
			list.add("Pillar at : "+this.master.x+", "+this.master.y+", "+this.master.z);
		else
			list.add("Not connected to a pillar");
		list.add("Energy : "+NumberFormat.getNumberInstance().format(this.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(this.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
		if(this.getFluidTanks().size()>0 && hasFL){
			for(int i = 0 ; i < this.getFluidTanks().size() ; i++){
				list.add(this.getFluidTanks().get(i).getFluid().getLocalizedName()+" : "+
					NumberFormat.getNumberInstance().format(this.getFluidTanks().get(i).getFluidAmount())+" MB / "+
					NumberFormat.getNumberInstance().format(this.getFluidTanks().get(i).getCapacity())+" MB");
			}
		}
		
	}

}
