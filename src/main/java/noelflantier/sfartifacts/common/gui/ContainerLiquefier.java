package noelflantier.sfartifacts.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.gui.slots.FluidsSlots;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.items.ItemAsgardite;

public class ContainerLiquefier extends ContainerMachine{

	private int slotId = -1;
	
	public ContainerLiquefier(InventoryPlayer inventory,TileLiquefier tile){
		super(inventory,tile);
		
		for(int x = 0 ; x < 9 ; x++){
			this.addSlotToContainer(new Slot(inventory,x,8+18*x,176));
		}
		for(int x = 0 ; x < 9 ; x++)
			for(int y = 0 ; y < 3 ; y++)
				this.addSlotToContainer(new Slot(inventory,x+y*9+9,8+18*x,118+18*y));
		
		this.addSlotToContainer(new LiquefierSlots(tile, nextId(),57,36));//REAL ID 36
		this.addSlotToContainer(new FluidsSlots(tile, nextId(),15,75,true,FluidRegistry.WATER));//REAL ID 37
		this.addSlotToContainer(new FluidsSlots(tile, nextId(),141,75,false,ModFluids.fluidLiquefiedAsgardite));//REAL ID 38
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	private int nextId(){
		this.slotId++;
		return this.slotId;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
		Slot slot = getSlot(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			if (index >= 36){ //To player
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}else{
				boolean success = false;
				for(int i = 0 ; i <= this.slotId ; i++){
					if(this.tmachine.getStackInSlot(i)==null){
						if(!this.tmachine.isItemValidForSlot(i, stack) || !mergeItemStack(stack, 36+i, 37+i, false))
							success = false;
						else{
							success = true;
							break;
						}
					}else if(this.tmachine.getStackInSlot(i).getItem()==slot.getStack().getItem()){
						if(!mergeItemStack(stack, 36+i, 37+i, false))
							success = false;
						else{
							success = true;
							break;
						}
					}
				}
				if(!success)
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

	@Override
    protected boolean mergeItemStack(ItemStack stack, int p_75135_2_, int p_75135_3_, boolean p_75135_4_)
    {
        boolean flag1 = false;
        int k = p_75135_2_;

        if (p_75135_4_)
        {
            k = p_75135_3_ - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1))
                {
                    int l = itemstack1.stackSize + stack.stackSize;

                    if (l <= stack.getMaxStackSize())
                    {
                    	stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < stack.getMaxStackSize())
                    {
                    	stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (stack.stackSize > 0)
        {
            if (p_75135_4_)
            {
                k = p_75135_3_ - 1;
            }
            else
            {
                k = p_75135_2_;
            }

            while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {	
                	ItemStack t = stack.copy();
                	t.stackSize = Math.min(t.stackSize, slot.getSlotStackLimit());
                    slot.putStack(t.copy());
                    slot.onSlotChanged();
                    stack.stackSize -= t.stackSize;
                    flag1 = true;
                    break;
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
	
	private class LiquefierSlots extends Slot{

		public LiquefierSlots(IInventory inv, int id,int x, int y) {
			super(inv, id, x, y);
		}
		
		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return stack.getItem() instanceof ItemAsgardite;
	    }    

		@Override
		public int getSlotStackLimit()
	    {
	        return 64;
	    }
	}
}
