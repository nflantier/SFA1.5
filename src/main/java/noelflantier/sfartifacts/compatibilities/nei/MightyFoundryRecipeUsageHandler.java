package noelflantier.sfartifacts.compatibilities.nei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;

public class MightyFoundryRecipeUsageHandler extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Mighty Foundry";
	}

	@Override
	public String getGuiTexture() {
		return (References.MODID+":textures/gui/nei/guiMightyFoundry.png");
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient == null) {
			return;
		}
		List<ItemStack> st = new ArrayList<ItemStack>();
		ItemStack it = ingredient.copy();
		it.stackSize = it.getMaxStackSize();
		st.add(it);
		List<ISFARecipe> recipes = RecipesRegistry.instance.getRecipesForUsageAndInputs(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY, RecipesRegistry.instance.getInputFromItemStacks(st));
		if(recipes!=null){
			for(ISFARecipe recipe : recipes){
				arecipes.add(new MightyFoundryRecipeNei(RecipeMightyFoundry.class.cast(recipe)));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result == null) {
			return;
		}
		for (Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY).entrySet()){
			for(RecipeOutput ro : entry.getValue().getOutputs()){
				if(ro.getItemStack()!=null && ro.getItemStack().getItem()==result.getItem() && ro.getItemStack().getItemDamage()==result.getItemDamage()){
					arecipes.add(new MightyFoundryRecipeNei(RecipeMightyFoundry.class.cast(entry.getValue())));
				}
			}
		}
	}
	  
	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 78);
	}

	@Override
	public void drawForeground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		//drawProgressBar(90, 17, 170, 0, 22, 16, 25, 0);
		GuiDraw.drawTexturedModalRect(2, 3, 180, 0, 14, 47);
		GuiDraw.drawTexturedModalRect(19, 3, 166, 0, 14, 47);	
		GuiDraw.drawString(((MightyFoundryRecipeNei)arecipes.get(recipeIndex)).getEnergyString(), 40, 60, 0x808080, false);
		
	}
	
	public class MightyFoundryRecipeNei extends TemplateRecipeHandler.CachedRecipe {

	    private ArrayList<PositionedStack> input;
	    private PositionedStack output;
	    private String energyneeded;
	    
		public MightyFoundryRecipeNei(RecipeMightyFoundry rmf){
		    this.input = new ArrayList<PositionedStack>();
		    this.input.add(new PositionedStack(ItemNBTHelper.setInteger(rmf.getMold(), "idmold", rmf.getMold().getItemDamage()), 38, 3));
		    this.energyneeded = rmf.getEnergyCost()+" RF * "+rmf.getItemQuantity();
		    ItemStack st = rmf.getInputs().get(0).getItemStack().copy();
	    	st.stackSize = rmf.getItemQuantity();
		    this.input.add(new PositionedStack(st, 92, 3));
		    
	        this.output = new PositionedStack(rmf.getOutputs().get(0).getItemStack(), 138+4, 51+4);
			
		}
		
	    public String getEnergyString() {
			return energyneeded;
		}

		@Override
	    public List<PositionedStack> getIngredients() {
	      return getCycledIngredients(cycleticks / 20, input);
	    }

	    @Override
	    public PositionedStack getResult() {
	      return output;
	    }
		
	}
}
