package noelflantier.sfartifacts.compatibilities.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;

public class InjectorRecipeUsageHandler extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
	    return "Injector";
	}

	@Override
	public String getGuiTexture() {
		return (References.MODID+":textures/gui/nei/guiInjector.png");
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(149, 32, 16, 16), "SFArtifactsInjector", new Object[0]));
	}
	  
	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient == null) {
			return;
		}

		for (Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(InjectorRecipesHandler.USAGE_INJECTOR).entrySet()){
			for(RecipeInput ri : entry.getValue().getInputs()){
				if(ri.isSameItem(ingredient)){
					arecipes.add(new InjectorRecipeNei(entry.getValue()));
					break;
				}
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result == null) {
			return;
		}
		for (Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(InjectorRecipesHandler.USAGE_INJECTOR).entrySet()){
			for(RecipeOutput ro : entry.getValue().getOutputs()){
				if(ro.isSameItem(result)){
					arecipes.add(new InjectorRecipeNei(entry.getValue()));
					break;
				}
			}
		}
	}
	  
	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 168, 70);
	}

	@Override
	public void drawForeground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		drawProgressBar(90, 17, 170, 0, 22, 16, 25, 0);
		GuiDraw.drawTexturedModalRect(5, 2, 184, 16, 14, 47);
		GuiDraw.drawTexturedModalRect(22, 2, 170, 16, 14, 47);
		GuiDraw.drawString(((InjectorRecipeNei)arecipes.get(recipeIndex)).getEnergy()+"RF "+((InjectorRecipeNei)arecipes.get(recipeIndex)).getFluid()+"MB", 75, 42, 0x808080, false);
	}
	
	@Override
	public void drawExtras(int recipeIndex) {
	}
	
	public class InjectorRecipeNei extends TemplateRecipeHandler.CachedRecipe {

	    private ArrayList<PositionedStack> input;
	    private ArrayList<PositionedStack> output;
	    private int energy;
	    private int fluid;
	    
		public InjectorRecipeNei(ISFARecipe ir){
		    this.input = new ArrayList<PositionedStack>();
		    this.output = new ArrayList<PositionedStack>();
		    int i = 0;
		    for(RecipeInput ri :ir.getInputs()){
		    	if(ri.isItem())
		    		this.input.add(new PositionedStack(ri.getItemStack(), 49+i*18, 17));
		        i+=1;
		    }
		    int k = 0;
		    for(RecipeOutput ro :ir.getOutputs()){
		    	if(ro.isItem())
		    		this.output.add(new PositionedStack(ro.getItemStack(), 119+k*18, 17));
			    k+=1;
		    }
		    this.energy = ir.getEnergyCost();
		    this.fluid = ir.getFluidCost();
		}

		public int getEnergy(){
			return this.energy;
		}
		
		public int getFluid(){
			return this.fluid;
		}
		
	    @Override
	    public List<PositionedStack> getIngredients() {
	      return getCycledIngredients(cycleticks / 20, input);
	    }
	    
	    @Override
	    public PositionedStack getResult() {
	    	return output.get(0);
	    }
		
	}
}
