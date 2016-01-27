package noelflantier.sfartifacts.client.gui.bases;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GuiRecipe{
	
	public static enum TYPE{
		VANILLA,
		INJECTOR,
		MIGHTYFOUNDRY,
		LIQUEFIER,
		MOLD,
		HAMMERSTAND
	}
	
	public List<GuiItemStack> itemStackList = new ArrayList<GuiItemStack>();
	public ItemStack stackToCraft;
	public List<IRecipe> recipes = new ArrayList<IRecipe>();
	public TYPE recipeType;
	
	public GuiRecipe(ItemStack toCraft, TYPE type){
		this.stackToCraft = toCraft;
		this.recipeType = type;
		getAndSetRecipe(this.stackToCraft);
	}
	
	public boolean isStackSame(ItemStack st1, ItemStack st2){
		return st1!=null && st2!=null && ( st1.getItem() == st2.getItem() ) && ( st1.getItemDamage() == st2.getItemDamage() );
	}
	
	public void computeRecipes(IRecipe recipe){
		if(recipe instanceof ShapedRecipes){
			
		}else if (recipe instanceof ShapedOreRecipe){
			
		}
	}
	
	public boolean getAndSetRecipe(ItemStack stack){
		if(TYPE.VANILLA==recipeType){
			recipes = (List<IRecipe>) CraftingManager.getInstance().getRecipeList().stream().filter((s)->isStackSame(((IRecipe)s).getRecipeOutput(),stack)).collect(Collectors.toList());
			if(recipes.isEmpty())
				return false;
			recipes.forEach((r)->computeRecipes(r));
		}
		return true;
	}
}
