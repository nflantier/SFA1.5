package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeInput {
	private ItemStack inputItem;
	private FluidStack inputFluid;
	
	public RecipeInput(ItemStack st){
    	this.inputItem = st;
    	this.inputFluid = null;
    }
    public RecipeInput(FluidStack ft){
    	this.inputFluid = ft;
    	this.inputItem = null;
    }

    public ItemStack getInputItem() {
		return inputItem;
	}
	public void setInputItem(ItemStack inputItem) {
		this.inputItem = inputItem;
	}
    public FluidStack getInputFluid() {
		return inputFluid;
	}
	public void setInputFluid(FluidStack inputFluid) {
		this.inputFluid = inputFluid;
	}
	
	public boolean isInputSame(RecipeInput ri){
		if(ri.getInputItem()!=null){
			return getInputItem()!=null && isItemStackSame(ri.getInputItem());
		}
		if(ri.getInputFluid()!=null){
			return getInputFluid()!=null && isFluidStackSame(ri.getInputFluid());
		}
		return false;
	}
	
	public boolean isItemStackSame(ItemStack stack){

		return getInputItem()!=null && getInputItem().getItem()!=null && getInputItem().getItem()==stack.getItem() 
				&& getInputItem().getItemDamage()==stack.getItemDamage() && getInputItem().stackSize>=stack.stackSize;
	}
	public boolean isFluidStackSame(FluidStack stack){
		return stack.getFluid()==getInputFluid().getFluid() && stack.amount>=getInputFluid().amount;
	}
	
	public void debug(){
		System.out.println(this.inputItem.getItem()+"  "+this.inputItem.stackSize+"  "+this.inputFluid.getFluid().getName()+"  "+this.inputFluid.amount);
	}
	
	public void outIt(){
		String str = "";
		if(this.getInputItem()!=null)
			str += this.inputItem.getItem()+"  "+this.inputItem.stackSize+"  ";
		if(this.getInputFluid()!=null)
			str += this.inputFluid.getFluid().getName()+"  "+this.inputFluid.amount;
		System.out.println(str);
	}
	
    public boolean isContainedIn(List<RecipeInput> lri){
    	boolean flag = false;
    	for (RecipeInput input : lri) {
    		flag = isInputSame(input);
		}
    	return flag;
    }
}
