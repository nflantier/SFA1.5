package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeOutput extends RecipeElement{
    public RecipeOutput(){
    	super();
    }
	public RecipeOutput(ItemStack st){
    	super(st);
    }
    public RecipeOutput(FluidStack ft){
    	super(ft);
    }
}
