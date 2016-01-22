package noelflantier.sfartifacts.common.blocks.tiles.pillar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.blocks.tiles.ITileCanBeMaster;
import noelflantier.sfartifacts.common.blocks.tiles.ITileCanTakeRFonlyFromPillars;
import noelflantier.sfartifacts.common.blocks.tiles.ITileWirelessEnergy;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.common.network.messages.PacketPillar;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileMasterPillar extends TileInterfacePillar implements ITileCanBeMaster,ITileWirelessEnergy{

	//STRUCTURE & MATERIAL
    public String namePillar;
    public int materialId;
    public Random random = new Random();

	//ENERGY
	public boolean isTransferCaped = false;
    public int energyCapacity = 0;
    public EnergyStorage storage = new EnergyStorage(0,0,0);
    public int ratioTransfer = 100;
	public int lastEnergyStoredAmount = -1;
    public boolean onlySender = false;
	public int passiveEnergy = 0;
	public int fluidEnergy = 0;
	public int amountToExtract = 0;

	//ENERGY CHILD
    public int[] energyChildInit;
    public List<Coord4> energyChild = new ArrayList<>();
    
    //ENERGY MATERIAL
	public float materialEnergyRatio = 12;
	public float materialHeightRatio = 256;
	public float materialRainRatio = 2.2F;
	
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
    		this.energyChild.removeIf((d)->worldObj.getTileEntity(d.x, d.y, d.z)==null 
    				||  ( worldObj.getTileEntity(d.x, d.y, d.z) instanceof IEnergyReceiver == false 
    				&& ( InterMods.hasIc2 && worldObj.getTileEntity(d.x, d.y, d.z) instanceof IEnergySink == false) ) );
        	if(!this.energyChild.isEmpty()){
        		for(Coord4 c : this.energyChild){
        			TileEntity te = worldObj.getTileEntity(c.x, c.y, c.z);
        			int maxAc = this.extractEnergy(ForgeDirection.UNKNOWN, this.getEnergyStored(ForgeDirection.UNKNOWN)/this.energyChild.size(), true);
    				
        			if(te!=null && te instanceof IEnergyHandler && ((IEnergyReceiver)te).getEnergyStored(ForgeDirection.UNKNOWN)<((IEnergyReceiver)te).getMaxEnergyStored(ForgeDirection.UNKNOWN)){
        				for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
	        				int energyTc = 0;
	        				if(ModConfig.isAMachinesWorksOnlyWithPillar && te instanceof ITileCanTakeRFonlyFromPillars){
	    						ITileCanTakeRFonlyFromPillars iop = (ITileCanTakeRFonlyFromPillars)te;
	    						energyTc = iop.receiveOnlyFromPillars( maxAc, true);
	    						if(energyTc!=0){
	    							energyTc = iop.receiveOnlyFromPillars(maxAc, false);
		            				this.extractEnergyWireless(energyTc, false, te.xCoord, te.yCoord, te.zCoord);
		        				}
	    					}else{
			                	energyTc = ((IEnergyReceiver) te).receiveEnergy(fd.getOpposite(), maxAc, false);
		            			this.extractEnergyWireless(energyTc, false, te.xCoord, te.yCoord, te.zCoord);
	    					}
	        				if(energyTc>0)
	            				break;
	    				}
        			}else if(te!=null && InterMods.hasIc2 && te instanceof IEnergySink ){
        				for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
        					double energyTc = InterMods.injectEnergy(te, fd.getOpposite(), InterMods.convertRFtoEU(maxAc,5), false);
	            			this.extractEnergyWireless(InterMods.convertEUtoRF(InterMods.convertRFtoEU(maxAc,5)-energyTc), false, te.xCoord, te.yCoord, te.zCoord);
	        				if(InterMods.convertEUtoRF(InterMods.convertRFtoEU(maxAc,5)-energyTc)>0)
	            				break;
        				}
        			}
        			
        			/*if(te instanceof IEnergyReceiver && ((IEnergyReceiver)te).getEnergyStored(ForgeDirection.UNKNOWN)<((IEnergyReceiver)te).getMaxEnergyStored(ForgeDirection.UNKNOWN)){
        				for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS){
	        				int maxAc = this.extractEnergy(ForgeDirection.UNKNOWN, this.getEnergyStored(ForgeDirection.UNKNOWN)/this.energyChild.size(), true);
	        				int energyTc = 0;
	        				if(ModConfig.isAMachinesWorksOnlyWithPillar && te instanceof ITileCanTakeRFonlyFromPillars){
	    						ITileCanTakeRFonlyFromPillars iop = (ITileCanTakeRFonlyFromPillars)te;
	    						energyTc = iop.receiveOnlyFromPillars( maxAc, true);
	    						if(energyTc!=0){
	    							energyTc = iop.receiveOnlyFromPillars(maxAc, false);
		            				this.extractEnergyWireless(energyTc, false, te.xCoord, te.yCoord, te.zCoord);
		        				}
	    					}else{
		        				energyTc = ((IEnergyReceiver) te).receiveEnergy(fd.getOpposite(), maxAc, true);
			                	if(energyTc!=0){
			                		energyTc = ((IEnergyReceiver) te).receiveEnergy(fd.getOpposite(), maxAc, false);
		            				this.extractEnergyWireless(energyTc, false, te.xCoord, te.yCoord, te.zCoord);
		        				}
	    					}
	        				if(energyTc>0)
	            				break;
	    				}
        			}*/
        		}
        	}
    	}
    	
    	float structureratio =  (PillarsConfig.getInstance().getPillarFromName(namePillar)!=null)?PillarsConfig.getInstance().getPillarFromName(namePillar).naturalRatio : 1;
    	float ratioHeight = (this.yCoord<this.materialHeightRatio)?(float)this.yCoord/(float)this.materialHeightRatio:1;
    	float ratioRaining = (this.worldObj.isRaining() && this.worldObj.getBiomeGenForCoords(xCoord, yCoord).temperature<2)?this.materialRainRatio:0;
    	//this.passiveEnergy = (int) (this.naturalEnergy*rP+this.naturalEnergy*(ratioHeight+0.1)+this.naturalEnergy*ratioRaining)+1;
    	this.passiveEnergy = (int)( Math.pow( structureratio ,2.2 ) * Math.pow( materialEnergyRatio + Math.pow( ratioHeight * 2 ,4 ) + ratioRaining ,1.5 ) );
    	
    	//this.fill(ForgeDirection.UNKNOWN, new FluidStack(ModFluids.fluidLiquefiedAsgardite,150000), true);
    	if(this.getEnergyStored(ForgeDirection.UNKNOWN)<this.energyCapacity){
    		this.fluidEnergy = 0;
        	FluidStack ds = this.tank.drain(this.amountToExtract, false);
        	if(ds!=null && ds.amount>=this.amountToExtract && this.amountToExtract!=0){
            	//this.fluidEnergy = (int)(rP*this.naturalEnergy+r);
            	this.fluidEnergy = (int) Math.pow( Math.pow(structureratio, 1.1) * Math.pow(ds.amount/20+1,1.5) *  Math.pow(materialEnergyRatio, 1.1) , 0.85 );
        		this.tank.drain(this.amountToExtract, true);
        	}
    	}
    	//this.extractEnergy(ForgeDirection.UNKNOWN, 150000, false);
    	this.receiveEnergy(ForgeDirection.UNKNOWN,(int)this.passiveEnergy+(int)this.fluidEnergy, false);
    	
        PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.lastEnergyStoredAmount),this);
        PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
        PacketHandler.sendToAllAround(new PacketPillar(this), this);
    	this.lastEnergyStoredAmount = this.getEnergyStored(ForgeDirection.UNKNOWN);
    	
    }
    
	@Override
	public void init(){
		super.init();
	}
	
	public boolean isMaster(){
		return this.xCoord==this.getMasterX() && this.yCoord==this.getMasterY() && this.zCoord==this.getMasterZ();
	}
	
    public void setMaterialRatios(float ne, float mhe, float re){
    	this.materialEnergyRatio = ne;
    	this.materialHeightRatio = mhe;
    	this.materialRainRatio = re;
    }
	
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("namePillar", this.namePillar);
        nbt.setInteger("materialId", this.materialId);
        nbt.setInteger("amountToExtract", this.amountToExtract);

        this.storage.writeToNBT(nbt);
        this.tank.writeToNBT(nbt);
        
       	if(this.energyChild.size()>0){
	        int[] tabch = new int[this.energyChild.size()*3];
	        int k = 0;
	        for(Coord4 ch : this.energyChild){
	        	tabch[k] = ch.x;
	        	k+=1;
	        	tabch[k] = ch.y;
	        	k+=1;
	        	tabch[k] = ch.z;
	        	k+=1;
	        }
	    	nbt.setIntArray("tabch", tabch);
    	}
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.namePillar = nbt.getString("namePillar");
        this.materialId = nbt.getInteger("materialId");
		this.energyCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).energyCapacity;
		this.tankCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).fluidCapacity;

		this.storage.setCapacity(this.energyCapacity);
		this.storage.readFromNBT(nbt);
		this.storage.setMaxTransfer(this.energyCapacity/this.ratioTransfer);
		this.lastEnergyStoredAmount = nbt.getInteger("lastEnergyStoredAmount");
		this.tank.setCapacity(this.tankCapacity);
		this.tank.readFromNBT(nbt);
		
		PillarMaterials pm = PillarMaterials.getMaterialFromId(this.materialId);
		this.materialEnergyRatio = pm.energyRatio;
		this.materialHeightRatio = pm.heightRatio;
		this.materialRainRatio = pm.rainRatio;
        this.amountToExtract = nbt.getInteger("amountToExtract");

        int[] tabch = nbt.getIntArray("tabch");
        if(tabch != null && tabch.length>0){
	        int k = 0;
	        for(int i = 0 ; i < tabch.length/3 ; i++){
	        	this.energyChild.add(new Coord4(tabch[k],tabch[k+1],tabch[k+2]));
	        	k = k+3;
	        }
	    }
    }

	@Override
	public List<Coord4> getChildsList() {
		return energyChild;
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
		if(maxReceive>0 && re>0 && !simulate && random.nextFloat()<0.5F)
			PacketHandler.sendToAllAround(new PacketParticleMoving((double)this.xCoord,(double)this.yCoord,(double)this.zCoord,(double)x,(double)y,(double)z),this);
		return this.isRedStoneEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	
	}

	@Override
	public int extractEnergyWireless(int maxExtract, boolean simulate, int x, int y, int z) {
		int ex = this.storage.extractEnergy(maxExtract, true);
		if(maxExtract>0 && ex>0 && !simulate && random.nextFloat()<0.5F){
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
	
	@Override
	public void addToWaila(List<String> list) {
		if(this.hasMaster()){
			super.addToWaila(list);
		}else{
			list.add("Master with no pillar");
			list.add("Energy : "+NumberFormat.getNumberInstance().format(this.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(this.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
			if(this.getFluidTanks().get(0)!=null)
				list.add("Liquefied Asgardite : "+NumberFormat.getNumberInstance().format(this.getFluidTanks().get(0).getFluidAmount())+" MB / "+NumberFormat.getNumberInstance().format(this.getFluidTanks().get(0).getCapacity())+" MB");
		}
	}
}
