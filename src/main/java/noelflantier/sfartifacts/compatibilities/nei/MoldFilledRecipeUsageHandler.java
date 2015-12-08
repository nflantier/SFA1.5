package noelflantier.sfartifacts.compatibilities.nei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class MoldFilledRecipeUsageHandler  extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Mold";
	}

	@Override
	public String getGuiTexture() {
		return (References.MODID+":textures/gui/nei/guiMoldMaking.png");
	}
	  
	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 167, 169);
	}

	@Override
	public void drawForeground(int recipeIndex) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GuiDraw.drawString(((MoldFilledRecipeNei)arecipes.get(recipeIndex)).moldName, 190, -16, 0x808080, false);
		GL11.glScalef(1F, 1F, 1F);
	}
	
	@Override
	public void drawExtras(int recipeIndex) {
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient == null || ingredient.getItem()!=Item.getItemFromBlock(Blocks.sand)) {
			return;
		}
		for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			arecipes.add(new MoldFilledRecipeNei(RecipeMold.class.cast(entry.getValue())));
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result == null) {
			return;
		}
		for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			if(result.getItem() instanceof ItemMold && RecipeMold.class.cast(entry.getValue()).getMoldMeta()==result.getItemDamage())
				arecipes.add(new MoldFilledRecipeNei(RecipeMold.class.cast(entry.getValue())));
		}
	}
	
	public class MoldFilledRecipeNei extends TemplateRecipeHandler.CachedRecipe {

	    private ArrayList<PositionedStack> input;
	    private PositionedStack output;
	    public String moldName = "";

		public MoldFilledRecipeNei(RecipeMold rm){
			this.moldName = rm.getUid();
		    this.input = new ArrayList<PositionedStack>();
			int[] inv = rm.getTabShape();
			for(int i=0;i<inv.length;i++){
				String bin = Integer.toBinaryString(inv[i]);
				int l = 9-bin.length();
				for(int j=0;j<l;j++)
					bin = "0"+bin;
				for(int k=0;k<bin.length();k++){
					if(bin.substring(k, k+1).equals("1")){
					    this.input.add(new PositionedStack(new ItemStack(Blocks.sand,1), 4+k*18, 4+i*18));
					}
				}
			}
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
