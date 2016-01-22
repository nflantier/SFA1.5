package noelflantier.sfartifacts.common.blocks.tiles.pillar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import noelflantier.sfartifacts.common.blocks.tiles.ISFAEnergyHandler;
import noelflantier.sfartifacts.common.blocks.tiles.ISFAFluid;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileInterfacePillar extends TileBlockPillar implements ISFAFluid,ISFAEnergyHandler{
	
	//ENERGY
    public List<ForgeDirection> recieveSides = new ArrayList<>();
    public List<ForgeDirection> extractSides = new ArrayList<>();
    
    //FLUID
	public Hashtable<Fluid, List<Integer>> fluidAndSide;
	
	public TileInterfacePillar(){
    }
    
    public TileInterfacePillar(String name){
    	super(name);
    }
    

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
        	return;
        if(this.extractSides.isEmpty())
        	return;
        
		for(ForgeDirection fd : this.extractSides){
    		int maxExtract = this.getEnergyStored(fd);
    		int maxAvailable = this.extractEnergy(fd, maxExtract, true);
    		double energyTransferred = 0;
			TileEntity tile = worldObj.getTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
			if(tile!=null && tile instanceof IEnergyHandler){
				energyTransferred = ((IEnergyHandler) tile).receiveEnergy(fd.getOpposite(), maxAvailable, false);
				this.extractEnergy(fd, (int)energyTransferred, false);
			}else if(tile!=null && InterMods.hasIc2 && tile instanceof IEnergySink && ((IEnergySink)tile).acceptsEnergyFrom(this, fd.getOpposite())){
				energyTransferred = InterMods.injectEnergy(tile, fd.getOpposite(), InterMods.convertRFtoEU(maxAvailable,5), false);
    			this.extractEnergy(fd, InterMods.convertEUtoRF(InterMods.convertRFtoEU(maxAvailable,5)-energyTransferred), false);
			}
		}
    	
    }
    
	@Override
	public void init(){
		super.init();
		
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar = new ArrayList<Integer>();
		for(ForgeDirection direction : this.recieveSides)
			ar.add(direction.ordinal());
		ar.add(ForgeDirection.UNKNOWN.ordinal());
		if(ar.size()>0)
			this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, ar);
	}

	
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        
        int[] dirext = new int[this.extractSides.size()];
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
    	nbt.setIntArray("tabrecieve", dirrec);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
		
        int[] dirext = nbt.getIntArray("tabextract");
        for(int i = 0 ; i < dirext.length ; i++){
        	this.extractSides.add(ForgeDirection.getOrientation(dirext[i]));
        }

        int[] dirrec = nbt.getIntArray("tabrecieve");
        for(int i = 0 ; i < dirrec.length ; i++){
        	this.recieveSides.add(ForgeDirection.getOrientation(dirrec[i]));
        }
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(this.canFill(from, resource.getFluid()) && !this.isRedStoneEnable){
			return this.getFluidTanks().get(0).fill(resource, doFill);
		}
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
		return this.getFluidTanks().get(0).drain(this.isRedStoneEnable?0:maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(!this.init || fluid==null || this.fluidAndSide==null || !this.fluidAndSide.containsKey(fluid) || !this.fluidAndSide.get(fluid).contains(from.ordinal()) ){
			return false;
		}
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(!this.init || fluid==null || this.fluidAndSide==null || !this.fluidAndSide.get(fluid).contains(from.ordinal()) ){
			return false;
		}
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.getFluidTanks().get(0).getInfo() };
	}

	@Override
	public List<FluidTank> getFluidTanks() {
		return this.getMasterTile().getFluidTanks();
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
		return this.isRedStoneEnable || this.getMasterTile()==null?0:this.getMasterTile().getEnergyStorage().receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable || this.getMasterTile()==null?0:this.getMasterTile().getEnergyStorage().extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return this.getMasterTile().getEnergyStorage().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return this.getMasterTile().getEnergyStorage().getMaxEnergyStored();
	}

	@Override
	public EnergyStorage getEnergyStorage(){
		return this.getMasterTile().getEnergyStorage();
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		
	}
}
