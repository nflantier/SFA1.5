package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.items.ItemLightningRod;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketLightningRodStand;

public class TileLightningRodStand extends TileAsgardianMachine implements ITileUsingMaterials{

	public static String[] typeLightningRod = new String[] {"basic", "advanced", "optimal", "ultimate"};
	public static float[] efficiencyLightningRod = new float[]{1.5F,6.6F,11.2F,17.9F};
	//Slots
	public ItemStack[] items = new ItemStack[1];
	
	//ENERGY
	public int lightningRodEnergy = 0;
	public static int energyTick = 10;	    
	public int currentEnergyTick = 0;
	
	//LIGHTNING
	public Random rdL = new Random();
	public int lightningTick = 20;
	public int currentLightningTick = 0;
	public float materialEnergyRatio = -1;
	
    //RENDERING
    public float yrotupring = 0.0F;
	
    public float yrotdownring = 0.0F;
	
    public float yrotpilon = 0.0F;
	public float ytranspilon = 0.0F;
	public float limitytranspilon = 0.6F;
	public float accytranspilon = 0.0F;
	public float pasytranspilon = 0.0001F;
	
	public float yrotball = 0.0F;
	public float limityrotball = 15F;
	public float accyrotball = 0.0F;
	public float pasyrotball = 0.0005F;
	
	public float ytransball = 0.0F;
	public float limitytransball = 1.6F;
	public float accytransball = 0.0F;
	public float pasytransball = 0.0005F;

	public TileLightningRodStand(){
		super("Lightning Rod Stand");
		this.hasFL = false;
		this.hasRF = true;
    	this.energyCapacity = 100000;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity/100);
    	this.storage.setMaxExtract(this.energyCapacity);
	}

	@Override
	public void init(){
		super.init();
    	this.extractSides.add(ForgeDirection.DOWN);
		this.materialEnergyRatio = this.getEnergyRatio(this.getBlockMetadata());
	}
	
	@Override
	public void processPackets() {
		//if(this.getEnergyStored(ForgeDirection.UNKNOWN)!=this.lastEnergyStoredAmount)
			PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this); 
		PacketHandler.sendToAllAround(new PacketLightningRodStand(this), this);
	}

	@Override
	public void processMachine() {        
        if(this.items[0] != null && this.items[0].getItem() instanceof ItemLightningRod){
        	this.lightningRodEnergy = (int) (efficiencyLightningRod[this.items[0].getItemDamage()] * materialEnergyRatio);
        	this.receiveEnergy(ForgeDirection.UNKNOWN,this.lightningRodEnergy/this.energyTick, false);
        	
        	this.currentLightningTick--;
        	if(this.currentLightningTick<=0){
        		this.currentLightningTick = (int)Math.random()*(200-50)+50;
        		int lc = -1;
	        	switch(this.items[0].getItemDamage()){
	        		case 0: lc = 2;
	        			break;
	        		case 1: lc = 5;
	        			break;
	        		case 2: lc = 10;
	        			break;
	        		case 3: lc = 20;
	        			break;
	        		default:
	        			break;
	        	}
            	if(this.rdL.nextInt(100)<lc){
    				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord+6, this.zCoord));
    	    		this.receiveEnergy(ForgeDirection.UNKNOWN,100, false);
            	}        	
            }
        }
    	
		TileEntity tile = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
		if(tile!=null && tile instanceof IEnergyHandler){
    		int maxExtract = this.storage.getMaxExtract();
    		int maxAvailable = this.storage.extractEnergy(maxExtract, true);
    		int energyTransferred = 0;
			energyTransferred = ((IEnergyHandler) tile).receiveEnergy(ForgeDirection.UP, maxAvailable, false);
    		this.extractEnergy(ForgeDirection.DOWN,energyTransferred, false);
		}
	}
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("energyTickTmp", this.currentEnergyTick);
        nbt.setInteger("currentLightningTick", this.currentLightningTick);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.currentEnergyTick = nbt.getInteger("energyTickTmp");
        this.currentLightningTick = nbt.getInteger("currentLightningTick");
    }
	@Override
	public String getInventoryName() {
		return "Lightningrod Stand";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemLightningRod;
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
