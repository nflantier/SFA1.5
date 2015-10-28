package noelflantier.sfartifacts.common.gui.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.items.ItemAsgardite;

public class FluidsSlots extends Slot{

	public boolean filled;
	public Fluid theFluid;
	
	public FluidsSlots(IInventory inv, int id,int x, int y, boolean filled, Fluid fluid) {
		super(inv, id, x, y);
		this.filled = filled;
		this.theFluid = fluid;
	}
	
	@Override
    public boolean isItemValid(ItemStack stack)
    {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
        return FluidContainerRegistry.isContainer(stack) && ( this.filled && ( fluid != null && fluid.getFluid()==this.theFluid ) )
        		|| ( !this.filled && ( FluidContainerRegistry.isEmptyContainer(stack)  || ( fluid != null && fluid.getFluid()==this.theFluid )) );
    }     

	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}