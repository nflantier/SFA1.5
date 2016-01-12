package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileMrFusion;
import noelflantier.sfartifacts.common.container.ContainerMrFusion;

public class GuiMrFusion extends GuiMachine{
	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiMrFusion.png");
	TileMrFusion tile;
	
	public GuiMrFusion(InventoryPlayer inventory, TileMrFusion tile) {
		super(new ContainerMrFusion(inventory, tile));
		this.xSize = 232;
		this.ySize = 240;
		this.tile = tile;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();

		GuiComponent gc = new GuiToolTips(guiLeft+145, guiTop+23, 14, 70, this.width);
		this.componentList.put("energy", gc);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Mr Fusion :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 148, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will produce RF with all the garbage", 0, 0);
			addText("you can have, it works better with foods", 0, 0);
			addText("and liquids.", 0, 0);
		}});
	}
	
	@Override
	public void updateToolTips(String key) {
		switch(key){
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
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+210, guiTop+16,this.zLevel, 14, 124, 232, 0);
		if(tile.tank.getFluid()!=null)
			GuiRender.renderFluid(tile.tank, guiLeft+178, guiTop+15, this.zLevel, 14, 95);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}
}
