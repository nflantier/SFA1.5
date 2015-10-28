package noelflantier.sfartifacts.compatibilities.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.InjectorRecipe;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class InjectorRecipeHandler extends TemplateRecipeHandler {

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
		/*for(InjectorRecipe ir : InjectorRecipe.values()){
			ItemStack output = new ItemStack(ir.result);
			if(ingredient.getItem() == ir.result && ingredient.getItemDamage()==output.getItemDamage()){
				for(Hashtable <Item, Integer> h : ir.recipe){
					InjectorRecipeNei res = new InjectorRecipeNei(h, ir.result);
					arecipes.add(res);
				}
			}
		}*/
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result == null) {
			return;
		}
		for(InjectorRecipe ir : InjectorRecipe.values()){
			ItemStack output = new ItemStack(ir.result);
			if(result.getItem() == ir.result && result.getItemDamage()==output.getItemDamage()){
				InjectorRecipeNei res = new InjectorRecipeNei(ir, ir.result);
				arecipes.add(res);
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
	    private PositionedStack output;
	    private int energy;
	    private int fluid;
	    
		public InjectorRecipeNei(InjectorRecipe ir, Item result){
		    this.input = new ArrayList<PositionedStack>();
		    Iterator<ItemStack> it = ir.recipe.iterator();
		    this.energy = ir.energyAmount;
		    this.fluid = ir.fluidAmount;
		    int i = 0;
		    while (it.hasNext()) {
		    	ItemStack entry = it.next();
		        this.input.add(new PositionedStack(entry, 49+i*18, 17));
		        i+=1;
		    }
	        this.output = new PositionedStack(new ItemStack(result), 119, 17);
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
	      return output;
	    }
		
	}
}
