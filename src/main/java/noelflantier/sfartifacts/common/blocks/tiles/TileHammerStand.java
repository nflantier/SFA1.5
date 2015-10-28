package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.HammerStandRecipe;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.baseclasses.ItemHammerBase;
import noelflantier.sfartifacts.common.items.baseclasses.RFHammerBase;

public class TileHammerStand extends TileSFA implements IInventory, ITileCanHavePillar{

	public Coord4 master;
	
	//INVOKING
	public boolean hasInvoked;
	public boolean isInvoking = false;
	public int tickEnergy;
	public int tmpRF;
	public int previousTmpRf;
	
	//UPGRADES
	public HammerStandRecipe curentRecipe;
	public int soundTick = 50;
	
	//SLOTS
	public ItemStack[] items = new ItemStack[1];
	
	public TileHammerStand(){
		super("Hammer Stand");
	}

    @Override
    public void setVariables(Object... params){
    	if(params[0] instanceof Coord4){
    		this.master = (Coord4)params[0];
    	}
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!this.hasMaster())
        	return;
        
        TileMasterPillar t = this.getMasterTile();
		if(t!=null){
			if(this.curentRecipe!=null){
				if(!this.curentRecipe.recipe.itemStillHere())
					this.curentRecipe=null;
			}
			
			//this.spawnParticle(0.02F);
	        
	        if(this.items[0] != null && this.items[0].getItem() instanceof RFHammerBase){
	        	if(ItemNBTHelper.getBoolean(this.items[0], "IsThrown", true))
	        		ItemNBTHelper.setBoolean(this.items[0], "IsThrown", false);//Set the hammer back to reinit if it stuck to thrown
	        	if(ItemNBTHelper.getBoolean(this.items[0], "IsMoving", true)){
					ItemNBTHelper.setBoolean(this.items[0], "IsMoving", false);//Set the hammer back to reinit if it stuck to moving
		        	this.worldObj.markBlockForUpdate(this.xCoord,this.yCoord,this.zCoord);
	        	}
	        	
	        	RFHammerBase it = (RFHammerBase) this.items[0].getItem();
	        	int en = it.getEnergyStored(this.items[0]);
	        	int cap = it.getCapacity(this.items[0]);
	        	if(en<cap){
                	if(t.getEnergyStored(ForgeDirection.UNKNOWN)>0){
	                	int maxAvailable = t.extractEnergy(ForgeDirection.UNKNOWN, 100000, true);
	            		int energyTransferred = 0;
	        			energyTransferred = it.receiveEnergy(this.items[0], maxAvailable, false);
	            		t.extractEnergyWireless(energyTransferred, false, this.xCoord, this.yCoord, this.zCoord);
                	}
	        	}
	        }
	        
	        this.tmpRF = t.getEnergyStored(ForgeDirection.UNKNOWN);
	        
	        if(this.previousTmpRf!=this.tmpRF)
	        	this.previousTmpRf = this.tmpRF;
        }
    }
    
    public boolean checkPillarIsEnough(){
    	TileMasterPillar t = this.getMasterTile();
    	if(t!= null)
    		return t.getEnergyStored(ForgeDirection.UNKNOWN)>=ModConfig.rfNeededThorHammer;
    	return false;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("hasInvoked", this.hasInvoked);
        nbt.setBoolean("isInvoking", this.isInvoking);

        if(this.hasMaster()){
	        nbt.setInteger("masterx", this.getMasterX());
	        nbt.setInteger("mastery", this.getMasterY());
	        nbt.setInteger("masterz", this.getMasterZ());
        	nbt.setBoolean("hasmaster", true);
        }else
        	nbt.setBoolean("hasmaster", false);
        
        NBTTagCompound[] tag = new NBTTagCompound[this.items.length];
		for (int i = 0; i < this.items.length; i++)
		{
			tag[i] = new NBTTagCompound();
			if (this.items[i] != null)
				tag[i] = this.items[i].writeToNBT(tag[i]);
			nbt.setTag("Items" + i, tag[i]);
		}
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.hasInvoked = nbt.getBoolean("hasInvoked");
        this.isInvoking = nbt.getBoolean("isInvoking");

    	if(nbt.getBoolean("hasmaster"))
    		this.master = new Coord4(nbt.getInteger("masterx"),nbt.getInteger("mastery"),nbt.getInteger("masterz"));
    
		NBTTagCompound[] tag = new NBTTagCompound[this.items.length];
		for (int i = 0; i < this.items.length; i++)
		{
			tag[i] = nbt.getCompoundTag("Items" + i);
			this.items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
    }

	@Override
	public int getSizeInventory() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		ItemStack it = this.getStackInSlot(slot);
		if(it!=null){
			if(it.stackSize < i)
				this.setInventorySlotContents(slot, null);
			else{
				it = it.splitStack(i);
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
		this.items[slot] = stack;
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Hammer Stand";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entity) {
		return entity.getDistanceSq(xCoord+0.5F, yCoord+0.5F, zCoord+0.5F)<=64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemHammerBase;
	}

	@Override
	public Coord4 getMasterCoord() {
		return this.master;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}
}
