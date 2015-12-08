package noelflantier.sfartifacts.common.blocks.tiles.pillar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.blocks.tiles.ITileCanBeMaster;
import noelflantier.sfartifacts.common.blocks.tiles.ITileWirelessEnergy;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.common.network.messages.PacketPillar;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;

public class TileMasterPillar extends TileInterfacePillar implements ITileCanBeMaster,ITileWirelessEnergy{

	//STRUCTURE
    public String namePillar;
    public int structureId;
    public int materialId;

	//ENERGY
	public boolean isTransferCaped = false;
    public int energyCapacity = 0;
    //public int rf;
    public EnergyStorage storage = new EnergyStorage(0,0,0);
    public int ratioTransfer = 100;
	public int lastEnergyStoredAmount = -1;
    public boolean onlySender = false;
	public int passiveEnergy = 0;
	public int fluidEnergy = 0;
	public int amountToExtract = 0;

	//ENERGY CHILD
    public int[] energyChildInit;
    public List<TileEntity> energyChild = new ArrayList<>();
    public int currentIndexChild = -1;
    
    //ENERGY STRUCTURE
	public int naturalEnergy = 12;
	public int maxHeightEfficiency = 256;
	public float rainEfficiency = 2.2F;
	
    //FLUID
   	public FluidTank tank = new FluidTank(0);
   	public int tankCapacity;
	public Hashtable<Fluid, List<Integer>> fluidAndSide;
    
    public TileMasterPillar(){
    }
    
    public TileMasterPillar(String name){
    	super(name);
    }


    @Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
        	return;
        if(!this.hasMaster())
        	return;
    	if(!this.energyChild.isEmpty()){
    		List<TileEntity> ttoremove  = new ArrayList<>();
    		for(TileEntity t : this.energyChild){
        		//if(this.currentIndexChild==-1)
        			//this.currentIndexChild = this.energyChild.indexOf(t);
    			if(t != null && t instanceof IEnergyConnection){
        			TileEntity majt = this.worldObj.getTileEntity(t.xCoord, t.yCoord, t.zCoord);
        			if(majt != null && majt instanceof IEnergyConnection && t==majt){
        				//if(this.energyChild.indexOf(t)!=this.currentIndexChild)
        					//continue;
	    				for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
		                	int maxExtract = this.getEnergyStored(ForgeDirection.UNKNOWN);
		                	if(this.isTransferCaped)
		                		maxExtract  = (int) ((float)maxExtract>=(float)this.energyCapacity/(float)100?(float)this.energyCapacity/(float)100:maxExtract);
		                	int maxAvailable = this.extractEnergy(ForgeDirection.UNKNOWN, maxExtract, true);
		                	int energyTransferred = 0;
		    				if(majt instanceof IEnergyReceiver){
		        				energyTransferred = ((IEnergyReceiver) majt).receiveEnergy(fd.getOpposite(), maxAvailable, true);
	    						if(energyTransferred!=0){
		        					energyTransferred = ((IEnergyReceiver) majt).receiveEnergy(fd.getOpposite(), maxAvailable, false);
		            				this.extractEnergyWireless(energyTransferred, false, majt.xCoord, majt.yCoord, majt.zCoord);
		            				if(energyTransferred>0)
			            				break;
		        				}
	    					}
	    				}
    				}else{
    					ttoremove.add(t);
    				}
        			//break;
    			}
    		}
    		this.energyChild.removeAll(ttoremove);
    		if(this.currentIndexChild!=-1)
    			this.currentIndexChild = this.currentIndexChild>=this.energyChild.size()-1?0:this.currentIndexChild+1;
    		/*for(TileEntity t : ttoremove){
    			this.energyChild.remove(t);
    		}*/
    	}else
    		this.currentIndexChild=-1;
    	
    	float rP =  (this.structureId>0)?PillarsConfig.getInstance().getPillarFromName(namePillar).naturalRatio : 1;
    	float ratioHeight = (this.yCoord<this.maxHeightEfficiency)?(float)this.yCoord/(float)this.maxHeightEfficiency:1;
    	ratioHeight += 0.1;
    	float ratioRaining = (this.worldObj.isRaining())?this.rainEfficiency:0;
    	this.passiveEnergy = (int) (this.naturalEnergy*rP+this.naturalEnergy*ratioHeight+this.naturalEnergy*ratioRaining)+1;
    	
    	if(this.getEnergyStored(ForgeDirection.UNKNOWN)<this.energyCapacity){
    		this.fluidEnergy = 0;
        	FluidStack ds = this.tank.drain(this.amountToExtract, false);
        	if(ds!=null && ds.amount>=this.amountToExtract && this.amountToExtract!=0){
        		//double r =  ds.amount*this.naturalEnergy/1.8F*0.1;
        		double r =  ds.amount*2*this.naturalEnergy/2.5F;
        		r = r<ds.amount?ds.amount:r;
        		this.fluidEnergy = (int)(rP*this.naturalEnergy+r);
        		this.tank.drain(this.amountToExtract, true);
        	}
    	}
    	
    	this.receiveEnergy(ForgeDirection.UNKNOWN,(int)this.passiveEnergy+(int)this.fluidEnergy, false);
    	
        PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN), this.lastEnergyStoredAmount),this);
        PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
        PacketHandler.sendToAllAround(new PacketPillar(this), this);
    	this.lastEnergyStoredAmount = this.getEnergyStored(ForgeDirection.UNKNOWN);
    	
    }
    
	@Override
	public void init(){
		super.init();
		//this.storage.setCapacity(this.capacity);
		//this.tank.setCapacity(this.tankCapacity);
		//this.storage.setMaxTransfer(this.capacity/this.ratioTransfer);
		//this.storage.setEnergyStored(this.rf);
        int k = 0;
        if(this.energyChildInit != null && this.energyChildInit.length>0){
	        for(int i = 0 ; i < this.energyChildInit.length/3 ; i++){
	        	TileEntity t = this.worldObj.getTileEntity(this.energyChildInit[k],this.energyChildInit[k+1], this.energyChildInit[k+2]);
	        	k = k+3;
	        	if(t!=null && t instanceof IEnergyConnection)
	        		this.energyChild.add(t);
	        }
        }
	}
	
	public boolean isMaster(){
		return this.xCoord==this.getMasterX() && this.yCoord==this.getMasterY() && this.zCoord==this.getMasterZ();
	}
	
    public void setMaterialRatio(int ne, int mhe, float re){
    	this.naturalEnergy = ne;
    	this.maxHeightEfficiency = mhe;
    	this.rainEfficiency = re;
    }
	
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //nbt.setInteger("structureId", this.structureId);
        nbt.setString("namePillar", this.namePillar);
        nbt.setInteger("materialId", this.materialId);
        nbt.setInteger("amountToExtract", this.amountToExtract);

        this.storage.writeToNBT(nbt);
        this.tank.writeToNBT(nbt);
        
       	if(this.energyChild.size()>0){
	        int[] tabch = new int[this.energyChild.size()*3];
	        int k = 0;
	        for(TileEntity ch : this.energyChild){
	        	tabch[k] = ch.xCoord;
	        	k+=1;
	        	tabch[k] = ch.yCoord;
	        	k+=1;
	        	tabch[k] = ch.zCoord;
	        	k+=1;
	        }
	    	nbt.setIntArray("tabch", tabch);
    	}
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //this.structureId = nbt.getInteger("structureId");
        this.namePillar = nbt.getString("namePillar");
        this.materialId = nbt.getInteger("materialId");
		this.energyCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).energyCapacity;
		this.tankCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).fluidCapacity;

		this.storage.setCapacity(this.energyCapacity);
		this.storage.readFromNBT(nbt);
		this.storage.setMaxTransfer(this.energyCapacity/this.ratioTransfer);
		this.lastEnergyStoredAmount = nbt.getInteger("lastEnergyStoredAmount");
		//this.storage.setEnergyStored(this.rf);
		this.tank.setCapacity(this.tankCapacity);
		this.tank.readFromNBT(nbt);
		
		PillarMaterials pm = PillarMaterials.getMaterialFromId(this.materialId);
		this.naturalEnergy = pm.naturalEnergy;
		this.maxHeightEfficiency = pm.maxHeightEfficiency;
		this.rainEfficiency = pm.rainEfficiency;
        this.amountToExtract = nbt.getInteger("amountToExtract");

        int[] tabch = nbt.getIntArray("tabch");
        this.energyChildInit = tabch.clone();
    }

	@Override
	public List<TileEntity> getChildsList() {
		return this.energyChild;
	}
	
	@Override
	public int getEnergyStored(ForgeDirection from) {
		return this.getEnergyStorage().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return this.getEnergyStorage().getMaxEnergyStored();
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable?0:this.getEnergyStorage().receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable?0:this.getEnergyStorage().extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int receiveEnergyWireless(int maxReceive, boolean simulate, int x, int y, int z) {
		int re = this.storage.receiveEnergy(maxReceive, true);
		if(maxReceive>0 && re>0 && !simulate)
			PacketHandler.sendToAllAround(new PacketParticleMoving((double)this.xCoord,(double)this.yCoord,(double)this.zCoord,(double)x,(double)y,(double)z),this);
		return this.isRedStoneEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	
	}

	@Override
	public int extractEnergyWireless(int maxExtract, boolean simulate, int x, int y, int z) {
		int ex = this.storage.extractEnergy(maxExtract, true);
		if(maxExtract>0 && ex>0 && !simulate){
            PacketHandler.sendToAllAround(new PacketParticleMoving((double)x,(double)y,(double)z,(double)this.xCoord,(double)this.yCoord,(double)this.zCoord),this);
		}
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public List<FluidTank> getFluidTanks() {
		ArrayList<FluidTank> fl = new ArrayList<FluidTank>();
		fl.add(this.tank);
		return fl;
	}

	@Override
	public EnergyStorage getEnergyStorage(){
		return this.storage;
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
}
