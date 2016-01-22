package noelflantier.sfartifacts.common.blocks.tiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import baubles.api.BaublesApi;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRecharger;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileRecharger extends TileMachine implements ITileGlobalNBT{

	//INVENTORY
	public ItemStack[] items = new ItemStack[8];

	public boolean wirelessRechargingEnable = true;
	public boolean isRecharging = false;
	public boolean tmpRecharging = false;
	
	public TileRecharger(){
		super("Recharger");
		hasRF = true;
		hasFL = false;
		this.storage.setCapacity(500000);
		this.storage.setMaxTransfer(5000);
	}
	
	@Override
	public void init(){
		super.init();
		this.recieveSides.add(ForgeDirection.getOrientation(this.side));
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
	}
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from)?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public void addToWaila(List<String> list) {
		list.add("Energy : "+NumberFormat.getNumberInstance().format(this.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(this.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}

	@Override
	public void processMachine() {
		if(this.getEnergyStored(ForgeDirection.UNKNOWN)<=0)
			return;

		tmpRecharging = false;
		for(int i=0;i<this.items.length;i++){
			if(this.items[i]!=null && ( this.items[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && this.items[i].getItem() instanceof IElectricItem) )){
				this.rechargeItemStack(this.items[i]);
			}
		}
		if(wirelessRechargingEnable)
			getAllPlayerAround(xCoord,yCoord,zCoord,ModConfig.rangeOfRecharger*10).forEach((p)->rechargePlayerInv(p));
		if(this.isRecharging != tmpRecharging){
			this.isRecharging = tmpRecharging;
			PacketHandler.sendToAllAround(new PacketRecharger(this),this);
		}
		
	}

	public void rechargeItemStack(ItemStack stack){
		if(stack.getItem() instanceof IEnergyContainerItem){
			int s = ((IEnergyContainerItem)stack.getItem()).getEnergyStored(stack);
			int c = ((IEnergyContainerItem)stack.getItem()).getMaxEnergyStored(stack);
			if(s<c){
	    		int maxAvailable = this.extractEnergy(ForgeDirection.UNKNOWN, this.getEnergyStored(ForgeDirection.UNKNOWN), true);
	    		int energyTransferred = ((IEnergyContainerItem)stack.getItem()).receiveEnergy(stack, maxAvailable, true);
	    		if(energyTransferred!=0){
	    			this.tmpRecharging = true;
					energyTransferred = ((IEnergyContainerItem)stack.getItem()).receiveEnergy(stack, maxAvailable, false);
					this.extractEnergy(ForgeDirection.UNKNOWN, energyTransferred, false);
				}
			}
		}else if(InterMods.hasIc2 && stack.getItem() instanceof IElectricItem){
			double s = ElectricItem.manager.getCharge(stack);
			double c = ((IElectricItem)stack.getItem()).getMaxCharge(stack);
			if(s<c){
	    		int maxAvailable = this.extractEnergy(ForgeDirection.UNKNOWN, this.getEnergyStored(ForgeDirection.UNKNOWN), true);
	    		double energyTransferred = ElectricItem.manager.charge(stack, InterMods.convertRFtoEU(maxAvailable,5), 5, true, true);
	    		if(energyTransferred!=0){
	    			this.tmpRecharging = true;
					energyTransferred = ElectricItem.manager.charge(stack, InterMods.convertRFtoEU(maxAvailable,5), 5, true, false);
					this.extractEnergy(ForgeDirection.UNKNOWN, InterMods.convertEUtoRF(energyTransferred), false);
				}
			}
			
		}
	}
	
	public void rechargePlayerInv(EntityPlayer p){
		for(int i = 0;i<p.inventory.armorInventory.length;i++){
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)<=0)
				break;
			if(p.inventory.armorInventory[i]!=null && ( p.inventory.armorInventory[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && p.inventory.armorInventory[i].getItem() instanceof IElectricItem)) )
				this.rechargeItemStack(p.inventory.armorInventory[i]);
		}
		for(int i = 0;i<p.inventory.mainInventory.length;i++){
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)<=0)
				break;
			if(p.inventory.mainInventory[i]!=null && ( p.inventory.mainInventory[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && p.inventory.mainInventory[i].getItem() instanceof IElectricItem)))
				this.rechargeItemStack(p.inventory.mainInventory[i]);	
		}
		
		if(BaublesApi.getBaubles(p)!=null){
			for(int i = 0;i<BaublesApi.getBaubles(p).getSizeInventory();i++){
				if(this.getEnergyStored(ForgeDirection.UNKNOWN)<=0)
					break;
				if(BaublesApi.getBaubles(p).getStackInSlot(i)!=null && ( BaublesApi.getBaubles(p).getStackInSlot(i).getItem() instanceof IEnergyContainerItem 
						|| (InterMods.hasIc2 && BaublesApi.getBaubles(p).getStackInSlot(i).getItem() instanceof IElectricItem)))
					this.rechargeItemStack(BaublesApi.getBaubles(p).getStackInSlot(i));	
			}
		}
	}
	
	public List<EntityPlayer> getAllPlayerAround(int x, int y, int z, int r){
		ArrayList<EntityPlayer> a = new ArrayList<EntityPlayer>();	
        for (int i = 0; i < this.worldObj.playerEntities.size(); ++i){
	        EntityPlayer entityplayer1 = (EntityPlayer)this.worldObj.playerEntities.get(i);
	        if(entityplayer1.getDistanceSq(x, y, z)<r)
	        	a.add(entityplayer1);
        }
		return a;
	}
	
	@Override
	public ItemStack[] getItems() {
		return this.items;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem || ItemNBTHelper.verifyExistance(stack, "Energy") || stack.getItem() instanceof IElectricItem;
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
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
        nbt.setBoolean("isRecharging", this.isRecharging);
        nbt.setBoolean("wirelessRechargingEnable", this.wirelessRechargingEnable);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isRecharging = nbt.getBoolean("isRecharging");
        this.wirelessRechargingEnable = nbt.getBoolean("wirelessRechargingEnable");
    }

	@Override
	public void processPackets() {
		// TODO Auto-generated method stub
		
	}

}
