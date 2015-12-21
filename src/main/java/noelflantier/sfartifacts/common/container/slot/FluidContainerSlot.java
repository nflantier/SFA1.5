package noelflantier.sfartifacts.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidContainerSlot  extends Slot{
	
	public FluidContainerSlot(IInventory inv, int id,int x, int y) {
		super(inv, id, x, y);
	}
	
	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return FluidContainerRegistry.isContainer(stack);
    }     

	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}