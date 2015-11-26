package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeOutput {
	private ItemStack outputItem;
	private FluidStack outputFluid;
	public RecipeOutput(ItemStack st){
    	this.outputItem = st;
    }
    public RecipeOutput(FluidStack ft){
    	this.outputFluid = ft;
    }
    

    public FluidStack getOutputFluid() {
		return outputFluid;
	}
	public void setOutputFluid(FluidStack outputFluid) {
		this.outputFluid = outputFluid;
	}
    public ItemStack getOutputItem() {
		return outputItem;
	}
	public void setOutputItem(ItemStack outputItem) {
		this.outputItem = outputItem;
	}

    public boolean canStackWithItemStack(ItemStack i){
    	boolean flag = false;
		if(getOutputItem()!=null){
			if(i==null || (i!=null && i.stackSize<i.getMaxStackSize() 
					&& i.stackSize+getOutputItem().stackSize<=i.getMaxStackSize() 
					&& i.getItem()==getOutputItem().getItem()
					&& i.getItemDamage()==getOutputItem().getItemDamage())){
				flag = true;
			}
		}
		return flag;
    }
	
    public List<ItemStack> canStackWithItemStack(List<ItemStack> it){

		Predicate<ItemStack> predicate = (i)->i==null || (i!=null && i.stackSize<i.getMaxStackSize() 
				&& i.stackSize+getOutputItem().stackSize<=i.getMaxStackSize() 
				&& i.getItem()==getOutputItem().getItem()
				&& i.getItemDamage()==getOutputItem().getItemDamage());
		
		if(getOutputItem()!=null && it!=null && !it.isEmpty()){
			it.removeIf(predicate.negate());
			return it;
		}
    	return it;
    }
	public void debug() {
		if(getOutputItem()!=null)
			System.out.println(getOutputItem().getUnlocalizedName()+"  "+getOutputItem().stackSize);
		if(getOutputFluid()!=null)
			System.out.println(getOutputFluid().getUnlocalizedName()+"  "+getOutputFluid().amount);
	}
}
