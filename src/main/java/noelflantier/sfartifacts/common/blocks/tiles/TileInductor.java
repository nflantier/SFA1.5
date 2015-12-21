package noelflantier.sfartifacts.common.blocks.tiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;

public class TileInductor extends TileSFA implements ITileCanBeMaster,ITileWirelessEnergy,ISFAEnergyHandler{
	
	public Coord4 master;
    public List<Coord4> energyChild = new ArrayList<>();
    public boolean canSend = true;
    public boolean canRecieve = true;
    public int energyCapacity = 0;
    public static List<Integer> energyCapacityConfig = new ArrayList(){{add(0,150);add(1,500);add(2,1500);add(3,5000);}};
    public EnergyStorage storage = new EnergyStorage(0,0,0);
	public int lastEnergyStoredAmount = -1;
	public Random rdm = new Random();

	public TileInductor(){
		super("Inductor");
	}
	
	public TileInductor(int meta){
		this();
    	this.energyCapacity = energyCapacityConfig.get(meta)!=null?energyCapacityConfig.get(meta):0;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity);
    	this.storage.setMaxExtract(this.energyCapacity);
	}

	@Override
	public void init(){
		super.init();	
	}

	@Override
	public void updateEntity(){
		super.updateEntity();
		if(this.worldObj.isRemote)
			return;

		ForgeDirection fd = ForgeDirection.getOrientation(side).getOpposite();
		TileEntity tile = worldObj.getTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
		if(tile!=null && tile instanceof IEnergyHandler && tile instanceof TileBlockPillar ==false){
			int maxAvailable = this.extractEnergy(fd, this.getEnergyStored(fd), true);
			int energyTransferred = ((IEnergyHandler) tile).receiveEnergy(fd.getOpposite(), maxAvailable, true);
			if(energyTransferred!=0){
				energyTransferred = ((IEnergyHandler) tile).receiveEnergy(fd.getOpposite(), maxAvailable, false);
				this.extractEnergy(fd, energyTransferred, false);
			}
		}

    	if(!this.energyChild.isEmpty() && this.canSend){
    		this.energyChild.removeIf((d)->worldObj.getTileEntity(d.x, d.y, d.z)==null || worldObj.getTileEntity(d.x, d.y, d.z) instanceof TileInductor == false);
        	if(!this.energyChild.isEmpty()){
        		for(Coord4 c : this.energyChild){
        			TileEntity te = worldObj.getTileEntity(c.x, c.y, c.z);
        			if(te instanceof TileInductor && ((TileInductor)te).getEnergyStored(ForgeDirection.UNKNOWN)<((TileInductor)te).getMaxEnergyStored(ForgeDirection.UNKNOWN) && ((TileInductor)te).canRecieve){
	                	int maxAc = this.storage.extractEnergy(this.getEnergyStored(ForgeDirection.UNKNOWN)/this.energyChild.size(), true);
	                	int energyTc = ((TileInductor) te).storage.receiveEnergy( maxAc, true);
	                	if(energyTc!=0){
	                		energyTc = ((TileInductor) te).storage.receiveEnergy( maxAc, false);
            				this.extractEnergyWireless(energyTc, false, te.xCoord, te.yCoord, te.zCoord);
        				}
        			}
        		}
        	}
    	}
    	if(this.getEnergyStored(ForgeDirection.UNKNOWN)!=this.lastEnergyStoredAmount)
    		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
    	this.lastEnergyStoredAmount = this.getEnergyStored(ForgeDirection.UNKNOWN);
	}
	

	@Override
	public void addToWaila(List<String> list) {
		list.add("Energy : "+NumberFormat.getNumberInstance().format(this.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(this.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
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
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from)?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from)?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int receiveEnergyWireless(int maxReceive, boolean simulate, int x, int y, int z) {
		return 0;
	}

	@Override
	public int extractEnergyWireless(int maxExtract, boolean simulate, int x, int y, int z) {
		int ex = this.storage.extractEnergy(maxExtract, true);
		if(maxExtract>0 && ex>0 && !simulate && rdm.nextFloat()<0.1){
            PacketHandler.sendToAllAround(new PacketParticleMoving((double)x,(double)y,(double)z,(double)this.xCoord,(double)this.yCoord,(double)this.zCoord),this);
		}
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public List<Coord4> getChildsList() {
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
	public boolean canConnectEnergy(ForgeDirection from) {
		if(ForgeDirection.getOrientation(side).getOpposite()==from)
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
	
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        
        nbt.setBoolean("canSend", canSend);
        nbt.setBoolean("canRecieve", canRecieve);
        nbt.setInteger("energyCapacity", energyCapacity);
        this.storage.writeToNBT(nbt);
        
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
        
        this.canSend = nbt.getBoolean("canSend");
        this.canRecieve = nbt.getBoolean("canRecieve");
        this.energyCapacity = nbt.getInteger("energyCapacity");
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.storage.readFromNBT(nbt);

        int[] tabch = nbt.getIntArray("tabch");
        if(tabch != null && tabch.length>0){
            int k = 0;
	        for(int i = 0 ; i < tabch.length/3 ; i++){
	        	this.energyChild.add(new Coord4(tabch[k],tabch[k+1],tabch[k+2]));
	        	k = k+3;
	        }
        }
    }

}
