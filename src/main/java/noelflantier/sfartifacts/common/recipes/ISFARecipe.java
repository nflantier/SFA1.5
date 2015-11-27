package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ISFARecipe {
	String getUid();
	int getEnergyCost();
	int getFluidCost();
	List<RecipeInput> getInputs();
	List<RecipeOutput> getOutputs();
	default boolean isStacksInputs(List<RecipeInput> lri){
		boolean flag = false;
		if(lri==null || lri.isEmpty())
			return flag;
		int size = getInputs().size();
		List<RecipeInput> recipetmp = new ArrayList<RecipeInput>(getInputs());
		for (RecipeInput potentialinput :lri) {
			for (RecipeInput input : recipetmp) {
				if(potentialinput.isRecipeElementSame(input)){
					flag = true;
					size-=1;
					if(size<=0)
						break;
				}
				if(size<=0 && flag)
					return true;
			}
			recipetmp.removeIf((r)->potentialinput.isRecipeElementSame(r));
		}
		return size<=0 && flag;
	}
	default void debug(){
		System.out.println(".............................. "+this.getClass()+"   "+getUid());
	}
}
