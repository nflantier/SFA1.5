package noelflantier.sfartifacts.compatibilities.nei;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.Molds;

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
		for(Molds m : Molds.values()){
			if(ingredient.getItem()==m.ingredients.getItem()){
				arecipes.add(new MightyFoundryRecipeNei(m));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result == null) {
			return;
		}
		for(Molds m : Molds.values()){
			if(result.getItem()==m.result.getItem()){
				arecipes.add(new MightyFoundryRecipeNei(m));
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
	}
	
	public class MightyFoundryRecipeNei extends TemplateRecipeHandler.CachedRecipe {

	    private ArrayList<PositionedStack> input;
	    private PositionedStack output;
	    
		public MightyFoundryRecipeNei(Molds m){
		    this.input = new ArrayList<PositionedStack>();
		    this.input.add(new PositionedStack(m.ingredients, 92, 3));
		    this.input.add(new PositionedStack(m.mold, 38, 3));
	        this.output = new PositionedStack(m.result, 138+4, 51+4);
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
