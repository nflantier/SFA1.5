package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerMoldMaking extends Container{

	private int slotId = -1;
	public int currentSlot;
	public IInventory itemInv;
	
	public ContainerMoldMaking(InventoryPlayer inventory, IInventory itemInv, int slt){
		this.currentSlot = slt;
		this.itemInv = itemInv;
		for(int x = 0 ; x < 9 ; x++){
			if(x!=slt)
				this.addSlotToContainer(new Slot(inventory,x,8+18*x,176));
		}
		for(int y = 0 ; y < 9 ; y++)
			for(int x = 0 ; x < 9 ; x++)
				this.addSlotToContainer(new MoldMakingSlots(itemInv,nextId(),8+18*x,8+18*y));
		
	}

	private int nextId(){
		this.slotId++;
		return this.slotId;
	}
	
	private class MoldMakingSlots extends Slot{
		
		public MoldMakingSlots(IInventory inv, int id,int x, int y) {
			super(inv, id, x, y);
		}
		
		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return stack.getItem()==Item.getItemFromBlock(Blocks.sand);
	    }    
	
		@Override
		public int getSlotStackLimit()
	    {
	        return 1;
	    }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
		Slot slot = getSlot(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			if (index >= 9){ //To player
				if (!mergeItemStack(stack, 0, 9, false)){
					return null;
				}
			}else{
				return null;
			}

			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, stack);

			return result;
		}

		return null;
    }

}
