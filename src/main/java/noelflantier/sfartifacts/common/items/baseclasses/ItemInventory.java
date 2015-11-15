package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Molds;

public class ItemInventory implements IInventory{
	
	public ItemStack[] items = new ItemStack[81];
	public EntityPlayer player;
	
	public ItemInventory(EntityPlayer player){
		this.player = player;
		this.setInv();
	}

	public ItemInventory(EntityPlayer player, int id){
		this(player);
	}
	public void setInv(){
		if(this.player.getCurrentEquippedItem()==null)
			return;
		int[] inv = ItemNBTHelper.getIntegerArray(this.player.getCurrentEquippedItem(), "moldstructure", new int[]{});
		for(int i=0;i<inv.length;i++){
			String bin = Integer.toBinaryString(inv[i]);
			int l = 9-bin.length();
			for(int j=0;j<l;j++)
				bin = "0"+bin;
			for(int k=0;k<bin.length();k++){
				if(bin.substring(k, k+1).equals("1")){
					this.setInventorySlotContentsN(k+9*i, new ItemStack(Blocks.sand,1,0));
				}
			}
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

	public void setInventorySlotContentsN(int slot, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.items[slot] = stack;
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
		return "Mold";
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
	public void markDirty() {
		int[] tab = new int[9];
		for(int i =0;i<9;i++){
			String bin="";
			for(int j =0;j<9;j++){
				if(this.getStackInSlot(j+9*i)!=null)
					bin=bin+"1";
				else
					bin=bin+"0";
			}
			tab[i]=Integer.parseInt(bin, 2);
		}
		Molds m = Molds.isRecipe(tab);
		if(m!=null){
			this.player.getCurrentEquippedItem().setItemDamage(1);
			ItemNBTHelper.setInteger(this.player.getCurrentEquippedItem(), "idmold", m.ID);
			ItemNBTHelper.setIntegerArray(this.player.getCurrentEquippedItem(), "moldstructure", m.recipe);
		}else{
			this.player.getCurrentEquippedItem().setItemDamage(0);
			ItemNBTHelper.setInteger(this.player.getCurrentEquippedItem(), "idmold", -1);
			ItemNBTHelper.setIntegerArray(this.player.getCurrentEquippedItem(), "moldstructure", tab);
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem()==Item.getItemFromBlock(Blocks.sand);
	}

}
