package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;
import noelflantier.sfartifacts.common.container.ContainerRecharger;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRechargerGui;

public class GuiRecharger  extends GuiMachine{
	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiRecharger.png");
	TileRecharger tile;
	
	public GuiRecharger(InventoryPlayer inventory, TileRecharger tile) {
		super(new ContainerRecharger(inventory, tile));
		this.xSize = 176;
		this.ySize = 160;
		this.tile = tile;
		hasSidedBt[sidedButton.get(machineButtonO1)] = true;
		sidedBtHasPopUp[sidedButton.get(machineButtonO1)] = true;
		sidedBtTick[sidedButton.get(machineButtonO1)] = "W";
		sidedBtTock[sidedButton.get(machineButtonO1)] = "W";
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);	
		if(button.id==machineButtonO1){
			this.tile.wirelessRechargingEnable = !this.tile.wirelessRechargingEnable;
			PacketHandler.INSTANCE.sendToServer(new PacketRechargerGui(this.tile));
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();

		GuiComponent gc = new GuiToolTips(guiLeft+26, guiTop+15, 14, 70, this.width);
		this.componentList.put("energy", gc);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Recharger :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 67, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine can recharge all your rf items,", 0, 0);
			addText("if you are close enough it will recharge", 0, 0);
			addText("them wirelessly. To disable the wireless", 0, 0);
			addText("recharging click on the W button.", 0, 0);
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
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+27, guiTop+16,this.zLevel, 14, 47, 176, 0);
		if(this.tile.wirelessRechargingEnable)
			GuiRender.renderVerticalRectangle(100, 100, guiLeft+48, guiTop+23, this.zLevel, 18, 16, 176, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}
}
