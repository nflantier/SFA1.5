package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class RecipesRegistry {
	public static final RecipesRegistry instance = new RecipesRegistry();
	private final Map<String, Map<String, ISFARecipe>> recipes = new HashMap<String, Map<String, ISFARecipe>>();// name machine => name recipe -> recipe

	public void registerRecipe(String usageName, ISFARecipe recipe){
		getRecipesForUsage(usageName).put(recipe.getUid(), recipe);
	}

	public void registerRecipes(String usageName, AUsageRecipes container){
		for(ISFARecipe ir: container.getRecipes()){
			registerRecipe(usageName, ir);
		}
	}

	public ISFARecipe getRecipeForUsage(String usageName, String recipe) {
		ISFARecipe re = null;
		if(recipes.get(usageName)!=null)
			re = recipes.get(usageName).get(recipe);
		return re;
	}
	
	public Map<String, ISFARecipe> getRecipesForUsage(String usageName) {
		Map<String, ISFARecipe> res = recipes.get(usageName);
		if(res == null) {
			res = new HashMap<String, ISFARecipe>();
			recipes.put(usageName, res);
		}
		return res;
	}
	
	public List<RecipeInput> getInputFromEntityItem(List<EntityItem> items){
		List<RecipeInput> ri = null;
		if(items.size()>0){
			ri = new ArrayList<RecipeInput>();
			for(EntityItem ei: items){
				ri.add(new RecipeInput(ei.getEntityItem()));
			}
		}
		return ri;
	}	
	public List<RecipeInput> getInputFromItemStack(List<ItemStack> list){
		List<RecipeInput> ri = null;
		if(list.isEmpty())
			return ri;
		
		ri = new ArrayList<RecipeInput>();
		for(ItemStack ei: list){
			ri.add(new RecipeInput(ei));
		}
		return ri;
	}
	
	public List<RecipeOutput> getOutputs(IUseSFARecipes iuse, List<RecipeInput> inputs){
		ISFARecipe r = getBestRecipe(iuse, inputs);
		return r!=null?r.getOutputs():null;
	}
	public ISFARecipe getBestRecipe(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return getBestRecipe(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid());
	}
	public List<ISFARecipe> getRecipes(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return getRecipes(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid());
	}
	public List<ISFARecipe> getOrderedRecipes(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return getOrderedRecipes(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid());
	}
	
	public List<ISFARecipe> getOrderedRecipes(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		List<ISFARecipe> r = getRecipes(usageName, inputs, energy, fluid);
		if(r!=null && !r.isEmpty()){
			Comparator<ISFARecipe> comparator = (i1,i2)-> i1.getInputs().size()-i2.getInputs().size();
			r.sort(comparator.reversed());
		}
		return r;
	}

	public List<ISFARecipe> getRecipes(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		if(inputs==null || inputs.isEmpty())
			return null;
		List<ISFARecipe> list = getRecipesForUsageAndInputs(usageName, inputs);
		if(list == null || list.size()<=0)
			return null;
		Predicate<ISFARecipe> predicate = (r) -> r.getEnergyCost() > energy || r.getFluidCost() > fluid;
		list.removeIf(predicate);
		return list;
	}
	
	public ISFARecipe getBestRecipe(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		ISFARecipe daRecipe =null;
		List<ISFARecipe> list = getRecipes(usageName, inputs, energy, fluid);
		if(list.size()<=0)
			return daRecipe;
		int size = 0;
		for(int i = 0;i < list.size();i++){
			int psize = list.get(i).getInputs().size();
			if(psize>size){
				size = psize;
				daRecipe = list.get(i);
			}
		}
		return daRecipe;
	}
	
	public static List<ISFARecipe> getRecipesForUsageAndInputs(String usageName, List<RecipeInput> inputs){
		ArrayList<ISFARecipe> flag = new ArrayList<ISFARecipe>();
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(usageName).entrySet()){
			if(entry.getValue().isInputs(inputs)){
				flag.add(entry.getValue());
			}
		}
		return flag.size()>0?flag:null;
	}

	public boolean canRecipeStack(ISFARecipe recipe, List<ItemStack> outputStacks) {
		recipe.getOutputs().forEach((o)->o.canStackWithItemStack(outputStacks));
		return outputStacks!=null && !outputStacks.isEmpty() && recipe.getOutputs().size()<=outputStacks.size();
	}

	public ItemStack processRecipeOnInput(ISFARecipe recipe, ItemStack i) {
		for(RecipeInput ri : recipe.getInputs()){
			if( i!=null	){
				if(i.getItem()==ri.getInputItem().getItem()
					&& i.getItemDamage()==ri.getInputItem().getItemDamage()){
					i.stackSize -= ri.getInputItem().stackSize;
					if(i.stackSize<=0){
						i=null;
					}
				}
			}
		}
		return i;
	}

	public ItemStack processRecipeOnOutput(ISFARecipe recipe, ItemStack i) {
		int size = recipe.getOutputs().size();
		for(RecipeOutput ro : recipe.getOutputs()){
			if(ro.canStackWithItemStack(i)){
				if(i==null){
					return ro.getOutputItem();
				}else{
					i.stackSize+=ro.getOutputItem().stackSize;
					return i;
				}
			}
		}
		return null;
	}
	
	/*public class RecipePredicate implements Predicate<ISFARecipe>{
		Integer energy;
		Integer fluid;
		public boolean test(ISFARecipe recipe){
			if(recipe.getEnergyCost()>energy || recipe.getFluidCost()>fluid){
				return true;
			}
			return false;
		}
	}*/
}
