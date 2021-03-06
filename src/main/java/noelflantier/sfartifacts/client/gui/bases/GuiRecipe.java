package noelflantier.sfartifacts.client.gui.bases;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.LiquefierRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;
import scala.actors.threadpool.Arrays;

public class GuiRecipe extends GuiComponentBase{
	
	public static enum TYPE{
		VANILLA("VANILLA",70,60),
		INJECTOR(InjectorRecipesHandler.USAGE_INJECTOR,60,40),
		MIGHTYFOUNDRY(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY,60,40),
		LIQUEFIER(LiquefierRecipesHandler.USAGE_LIQUEFIER,60,40),
		MOLD(MoldRecipesHandler.USAGE_MOLD,180,150),
		HAMMERSTAND(HammerUpgradesRecipesHandler.USAGE_HAMMER_UPGRADES,60,40);
		
		public String usageName;
		public int width;
		public int height;
		private TYPE(String usageName, int width, int height){
			this.usageName = usageName;
			this.width = width;
			this.height = height;
		}
	}
	
	public boolean isRenderingItems = false;
	public boolean isRenderingName = true;
	public String name;
	public List<GuiItemStack> guiItemStackList = new ArrayList<GuiItemStack>();
	public List<GuiItemStack> guiItemStackToCraft = new ArrayList<GuiItemStack>();
	public List<IRecipe> vanillaRecipes = new ArrayList<IRecipe>();
	public List<ISFARecipe> sfaRecipes = new ArrayList<ISFARecipe>();
	public TYPE recipeType;
	public static int heightSlot = 16;
	public static int widthSlot = 16;
	public static int heightDSlot = 17;
	public static int widthDSlot = 17;
	public static final ResourceLocation guiselements = new ResourceLocation(References.MODID+":textures/gui/guisElements.png");
	public GuiImage horizontal = new GuiImage(0, 0, 32,2 , 0F, 0.75F, 0.25F, 0.75F+2/128F, guiselements);
	public GuiImage vertical = new GuiImage(0, 0, 2,32 , 0F, 0.75F, 2/128F, 1F, guiselements);
	
	public GuiRecipe(String name, int x, int y, ItemStack toCraft, TYPE type){
		super(x,y);
		this.name = name;
		this.recipeType = type;
		getAndSetRecipe(toCraft);
		this.guiItemStackToCraft.addAll(getGuiItemStack(new ArrayList<ItemStack>(){{add(toCraft);}}));
	}
	public GuiRecipe(String name, int x, int y, List<GuiItemStack> toCraft, TYPE type, List<GuiItemStack> li){
		super(x,y);
		this.name = name;
		this.recipeType = type;
		this.guiItemStackList.addAll(li);
		this.guiItemStackToCraft.addAll(toCraft);
	}
	
	public static List<GuiItemStack> getGuiItemStack(List<ItemStack> l){
		List<GuiItemStack> gis = new ArrayList<GuiItemStack>();
		l.forEach((g)->gis.add(new GuiItemStack(g)));
		return gis;
	}
	
	public boolean isStackSame(ItemStack st1, ItemStack st2){
		return st1!=null && st2!=null && ( st1.getItem() == st2.getItem() ) && ( st1.getItemDamage() == st2.getItemDamage() );
	}
	
	public void computeVanillaRecipes(IRecipe recipe){
		if(recipe instanceof ShapedRecipes){
			this.guiItemStackList.addAll(getGuiItemStack(new ArrayList<ItemStack>(Arrays.asList(((ShapedRecipes)recipe).recipeItems))));
		}else if (recipe instanceof ShapelessOreRecipe){
			this.guiItemStackList.addAll(getGuiItemStack(new ArrayList<ItemStack>(Arrays.asList(((ShapelessOreRecipe)recipe).getInput().toArray()))));
		}else if (recipe instanceof ShapedOreRecipe){
			this.guiItemStackList.addAll(getGuiItemStack(new ArrayList<ItemStack>(Arrays.asList(((ShapedOreRecipe)recipe).getInput()))));
		}
	}
	
	public void computeSFARecipes(ISFARecipe recipe) {
		List<ItemStack> it = new ArrayList<ItemStack>();
		if(TYPE.MIGHTYFOUNDRY==recipeType){
			recipe.getInputs().forEach((ge)->it.add(new ItemStack(ge.getItemStack().getItem(),RecipeMightyFoundry.class.cast(recipe).getItemQuantity(),ge.getItemStack().getItemDamage())));
		}else{
			recipe.getInputs().forEach((ge)->it.add(ge.getItemStack()));
		}
		this.guiItemStackList.addAll(getGuiItemStack(it));
	}
	
	public boolean getAndSetRecipe(ItemStack stack){
		if(TYPE.VANILLA==recipeType){
			vanillaRecipes = (List<IRecipe>) CraftingManager.getInstance().getRecipeList().stream().filter((s)->isStackSame(((IRecipe)s).getRecipeOutput(),stack)).collect(Collectors.toList());
			if(vanillaRecipes.isEmpty())
				return false;
			vanillaRecipes.forEach((r)->computeVanillaRecipes(r));
		}else{
			sfaRecipes = RecipesRegistry.instance.getRecipeForUsageAndOutputsItemStacks(recipeType.usageName, new ArrayList<ItemStack>(){{add(stack);}});
			if(sfaRecipes==null || sfaRecipes.isEmpty())
				return false;
			sfaRecipes.forEach((r)->computeSFARecipes(r));
		}
		return true;
	}
	
	public void drawBorder(){
		float ha = horizontal.alpha;
		float va = vertical.alpha;
		horizontal.alpha = 0.2F;
		vertical.alpha = 0.2F;
		horizontal.width = recipeType.width+8;
		horizontal.height = 1;
		horizontal.x = this.x-4;
		horizontal.y = this.y-4;
		horizontal.draw(x, y);

		horizontal.x = this.x-4;
		horizontal.y = this.y+recipeType.height+4;
		horizontal.draw(x, y);
		
		vertical.width = 1;
		vertical.height = recipeType.height+8+1;
		vertical.x = this.x-4;
		vertical.y = this.y-4;
		vertical.draw(x, y);
		
		vertical.x = this.x+recipeType.width+4;
		vertical.y = this.y-4;
		vertical.draw(x, y);
		
		horizontal.alpha = ha;
		vertical.alpha = va;
	}
	
	public void drawGrid(int x, int y){
		drawBorder();
		if(this.recipeType==TYPE.VANILLA){			
			horizontal.width = widthSlot*3+2;
			horizontal.height = 1;
			horizontal.x = this.x;
			horizontal.y = this.y+heightSlot;
			horizontal.draw(x, y);
			horizontal.y = this.y+heightSlot*2+1;
			horizontal.draw(x, y);
			vertical.width = 1;
			vertical.height = heightSlot*3+2;
			vertical.x = this.x+widthSlot;
			vertical.y = this.y;
			vertical.draw(x, y);
			vertical.x = this.x+widthSlot*2+1;
			vertical.draw(x, y);
		}else if(this.recipeType==TYPE.INJECTOR){
			horizontal.width = widthSlot*2+1;
			horizontal.height = 1;
			horizontal.x = this.x;
			horizontal.y = this.y+heightSlot;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.HAMMERSTAND){
			horizontal.width = widthSlot*2+1;
			horizontal.height = 1;
			horizontal.x = this.x;
			horizontal.y = this.y+heightSlot;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.MIGHTYFOUNDRY){
			horizontal.width = widthSlot+1;
			horizontal.height = 1;
			horizontal.x = this.x;
			horizontal.y = this.y+heightSlot;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.MOLD){
			horizontal.width = widthSlot*9+8;
			horizontal.height = 1;
			horizontal.x = this.x;
			vertical.width = 1;
			vertical.height = heightSlot*9+8;
			vertical.y = this.y;
			for(int i = 1 ; i<9;i++){
				horizontal.y = this.y+heightSlot*i+(i-1)*1;
				horizontal.draw(x, y);
				vertical.x = this.x+widthSlot*i+(i-1)*1;
				vertical.draw(x, y);
			}
		}
	}
	
	public void drawResult(int x, int y){
		if(isRenderingItems)
			this.guiItemStackToCraft.forEach((g)->g.draw(x, y));
		if(this.recipeType==TYPE.VANILLA){
			for(int i = 0 ; i<this.guiItemStackToCraft.size();i++){
				horizontal.width = widthSlot;
				horizontal.height = 1;
				horizontal.x = this.x+TYPE.VANILLA.width-widthSlot;
				horizontal.y = this.y+TYPE.VANILLA.height/2;
				horizontal.draw(x, y);
			}
		}else if(this.recipeType==TYPE.INJECTOR){
			horizontal.width = widthSlot;
			horizontal.height = 1;
			horizontal.x = this.x+TYPE.INJECTOR.width-widthSlot;
			horizontal.y = this.y+TYPE.INJECTOR.height/2;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.HAMMERSTAND){
			horizontal.width = widthSlot;
			horizontal.height = 1;
			horizontal.x = this.x+TYPE.HAMMERSTAND.width-widthSlot;
			horizontal.y = this.y+TYPE.HAMMERSTAND.height/2;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.MIGHTYFOUNDRY){
			horizontal.width = widthSlot;
			horizontal.height = 1;
			horizontal.x = this.x+TYPE.MIGHTYFOUNDRY.width-widthSlot;
			horizontal.y = this.y+TYPE.MIGHTYFOUNDRY.height/2;
			horizontal.draw(x, y);
		}else if(this.recipeType==TYPE.MOLD){
			horizontal.width = widthSlot;
			horizontal.height = 1;
			horizontal.x = this.x+TYPE.MOLD.width-widthSlot;
			horizontal.y = this.y+TYPE.MOLD.height/2;
			horizontal.draw(x, y);
		}
	}
	
	public void drawRecipe(int x, int y){
		if(isRenderingItems)
			this.guiItemStackList.forEach((g)->g.draw(x, y));
	}
	
	@Override
	public void draw(int x, int y) {
		drawGrid(x,y);
		drawRecipe(x,y);
		drawResult(x,y);
		drawName(x,y);
	}
	
	public int getScaledValue(int v, float scale){
		return (int) (v+(v-v*scale)/scale);
	}
	
	public void drawAutoWidthName(String n, int x, int y, int width, float scale){
		String[] t = n.split("\\s+");
		String str = "";
		int suc = 0;
		for(int i = 0 ; i < t.length ; i++){
			if((float)FR.getStringWidth(str+" "+t[i])*scale>width){	
				FR.drawString(String.format("%s%s%s", EnumChatFormatting.DARK_GRAY, str, EnumChatFormatting.RESET), getScaledValue(x,scale), getScaledValue(y+(int)(10*scale*suc),scale), 4210752);
				suc+=1;
				str = "";
			}
			if(str.equals(""))
				str+=t[i];
			else
				str+=" "+t[i];
		}
		if(!str.equals("") && !str.equals(" ")){
			FR.drawString(String.format("%s%s%s", EnumChatFormatting.DARK_GRAY, str, EnumChatFormatting.RESET), getScaledValue(x,scale), getScaledValue(y+(int)(10*scale*suc),scale), 4210752);
		}
	}
	
	public void drawName(int x, int y) {
		if(!isRenderingName)
			return;
		float scale = 0.6F;
		GL11.glPushMatrix();
			GL11.glScalef(scale, scale, 1);
			int xName = 0;
			int yName = 0;
			int w = FR.getStringWidth(name);
			if(this.recipeType==TYPE.VANILLA){
				yName = 52;
				drawAutoWidthName(name, this.x+xName, this.y+yName, TYPE.VANILLA.width, scale);
			}else if(this.recipeType==TYPE.INJECTOR){
				yName = 25;
				drawAutoWidthName(name, this.x+xName, this.y+yName, TYPE.INJECTOR.width, scale);
			}else if(this.recipeType==TYPE.HAMMERSTAND){
				yName = 25;
				drawAutoWidthName(name, this.x+xName, this.y+yName, TYPE.HAMMERSTAND.width, scale);
			}else if(this.recipeType==TYPE.MIGHTYFOUNDRY){
				yName = 25;
				drawAutoWidthName(name, this.x+xName, this.y+yName, TYPE.MIGHTYFOUNDRY.width, scale);
			}else if(this.recipeType==TYPE.MOLD){
				xName = 160;
				yName = TYPE.MOLD.height/2+5;
				drawAutoWidthName(name, this.x+xName, this.y+yName, TYPE.MOLD.width-160, scale);
			}
		GL11.glPopMatrix();
	}
	
	@Override
	public boolean isMouseHover(int mx, int my) {
		return false;
	}
}
