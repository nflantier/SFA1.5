package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import noelflantier.sfartifacts.common.container.ContainerInjector;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;

public class GuiInjector extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiInjector.png");
	public TileInjector tile;
	
	public GuiInjector(InventoryPlayer inventory, TileInjector tile) {
		super(new ContainerInjector(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawPopUp(int x, int y, int key){
		super.drawPopUp(x,y,key);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("tank", 
				new GuiToolTips(guiLeft+8, guiTop+23, 14, 47, this.width)
				);

		this.componentList.put("energy", 
				new GuiToolTips(guiLeft+25, guiTop+23, 14, 47, this.width)
				);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Injector :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine is used to inject liquefied asgardite", 0, 0);
			addText("into items, it use RF and liquefied asgardite.", 0, 0);
		}});
	}


	public void updateToolTips(String key){
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, this.tile.tank.getFluidAmount()+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+this.tile.tank.getCapacity()+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, this.tile.getEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+this.tile.getMaxEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				break;
			default:
				break;
		}
	}

	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {

		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		for(int i =0;i<tile.isRunning.length;i++){
			if(tile.isRunning[i]){
				GuiRender.renderTask(tile.tickToInject, tile.currentTickToInject[i], guiLeft+94, guiTop+23+i*25, this.zLevel, 22, 16, 176, 47);
			}
		}
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
