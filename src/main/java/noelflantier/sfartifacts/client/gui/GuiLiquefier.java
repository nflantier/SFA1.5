package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.container.ContainerLiquefier;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class GuiLiquefier extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiLiquefier.png");
	TileLiquefier tile;
	
	public GuiLiquefier(InventoryPlayer inventory, TileLiquefier tile) {
		super(new ContainerLiquefier(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		GuiComponent gc = new GuiToolTips(guiLeft+8, guiTop+23, 14, 47, this.width);
		this.componentList.put("tankmelt", gc);
		
		GuiComponent gc2 = new GuiToolTips(guiLeft+25, guiTop+23, 14, 47, this.width);
		this.componentList.put("energy", gc2);
		
		GuiComponent gc3 = new GuiToolTips(guiLeft+135, guiTop+23, 27, 47, this.width);
		this.componentList.put("tank", gc3);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Liquefier :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will turn asgardite into liquefied", 0, 0);
			addText("asgardite, it will use RF an water to do so.", 0, 0);
			addText("If you put water source under this machine it will", 0, 0);
			addText("passively drain from it, it can drain from a 3x3", 0, 0);
			addText("zone right under it.", 0, 0);
		}});
	}
	
	public void updateToolTips(String key){
		switch(key){
			case "tankmelt" :
				((GuiToolTips)this.componentList.get("tankmelt")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tankmelt")).addContent(this.fontRendererObj, this.tile.tankMelt.getFluidAmount()+" MB");
				((GuiToolTips)this.componentList.get("tankmelt")).addContent(this.fontRendererObj, "/ "+this.tile.tankMelt.getCapacity()+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, this.tile.getEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+this.tile.getMaxEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				break;
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, this.tile.tank.getFluidAmount()+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+this.tile.tank.getCapacity()+" MB");
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		
		GuiRender.renderEnergy(tile.energyCapacity, tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		if(tile.isRunning)
			GuiRender.renderTask(tile.tickToMelt, tile.currentTickToMelt, guiLeft+92, guiTop+37, this.zLevel, 22, 16, 176, 47);
		GuiRender.renderFluid(tile.tank, guiLeft+135, guiTop+23, this.zLevel, 27, 47);
		GuiRender.renderFluid(tile.tankMelt, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
