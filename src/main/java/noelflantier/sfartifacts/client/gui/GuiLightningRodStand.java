package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;
import noelflantier.sfartifacts.common.gui.ContainerLightningRodStand;
import noelflantier.sfartifacts.common.helpers.guis.GuiComponent;
import noelflantier.sfartifacts.common.helpers.guis.GuiRender;
import noelflantier.sfartifacts.common.helpers.guis.GuiSFA;
import noelflantier.sfartifacts.common.helpers.guis.GuiToolTips;

public class GuiLightningRodStand extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiLightningRodStand.png");
	TileLightningRodStand tile;
	
	public GuiLightningRodStand(InventoryPlayer inventory, TileLightningRodStand tile) {
		super(new ContainerLightningRodStand(inventory, tile));
		this.xSize = 176;
		this.ySize = 160;
		this.tile = tile;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		
		this.componentList.put("energy", new GuiToolTips(guiLeft+60, guiTop+15, 14, 47, this.width));
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Lightning Rod Stand :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 68, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will passively generate RF if there is", 0, 0);
			addText("a lightning rod in it.", 0, 0);
		}});
	}


	public void updateToolTips(String key){
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
		GuiRender.renderEnergy(tile.energyCapacity, tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+61, guiTop+16,this.zLevel, 14, 47, 176, 0);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
