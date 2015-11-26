package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ISFARecipe {
	String getUid();
	int getEnergyCost();
	int getFluidCost();
	List<RecipeInput> getInputs();
	List<RecipeOutput> getOutputs();
	default boolean isInputs(List<RecipeInput> lri){
		boolean flag = false;
		int size = getInputs().size();
		List<RecipeInput> riDone = new ArrayList<RecipeInput>();
		for (RecipeInput potentialinput :lri) {
			if(riDone.contains(potentialinput))
				continue;
			for (RecipeInput input : getInputs()) {
				if(potentialinput.isInputSame(input)){
					riDone.add(potentialinput);
					flag = true;
					size-=1;
				}
				if(size<=0 && flag)
					return true;
			}
		}
		return size<=0 && flag;
	}
	default void debug(){
		System.out.println(".............................. "+this.getClass()+"   "+getUid());
	}
}
