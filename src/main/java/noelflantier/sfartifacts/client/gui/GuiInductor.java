package noelflantier.sfartifacts.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.container.ContainerInductor;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketInductorGui;

public class GuiInductor  extends GuiSFA{
	
	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiInductor.png");
	TileInductor tile;
	
	public GuiInductor(InventoryPlayer inventory, TileInductor tile) {
		super(new ContainerInductor(inventory, tile));
		this.xSize = 176;
		this.ySize = 135;
		this.tile = tile;
	}
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id==0){
			this.tile.canWirelesslySend = tile.canWirelesslySend?false:true;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,0,tile.canWirelesslySend));
			this.getButtonById(0).displayString = !tile.canWirelesslySend?"OFF":"ON";
		}else if(button.id==1){
			this.tile.canWirelesslyRecieve = tile.canWirelesslyRecieve?false:true;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,1,tile.canWirelesslyRecieve));
			this.getButtonById(1).displayString = !tile.canWirelesslyRecieve?"OFF":"ON";
		}else if(button.id==2){
			this.tile.canSend = tile.canSend?false:true;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,2,tile.canSend));
			this.getButtonById(2).displayString = !tile.canSend?"OFF":"ON";
		}else if(button.id==3){
			this.tile.canRecieve = tile.canRecieve?false:true;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,3,tile.canRecieve));
			this.getButtonById(3).displayString = !tile.canRecieve?"OFF":"ON";	
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Inductor", 0, 0);
		}});
		this.componentList.put("wiresend", new GuiComponent(10, 20){{
			addText("Send wireless energy is ", 0, 0);
			addButton(0,guiLeft+125,guiTop-5,22,20,!tile.canWirelesslySend?"OFF":"ON");
		}});
		this.componentList.put("wirerec", new GuiComponent(10, 41){{
			addText("Recieve wireless energy is", 0, 0);
			addButton(1,guiLeft+138,guiTop-5,22,20,!tile.canWirelesslyRecieve?"OFF":"ON");
		}});
		this.componentList.put("phsend", new GuiComponent(10, 60){{
			addText("Extract energy to the "+ForgeDirection.getOrientation(tile.side).name(), 0, 0);
			addText("direction is", 0, 0);
			addButton(2,guiLeft+60,guiTop+10,22,20,!tile.canSend?"OFF":"ON");
		}});
		this.componentList.put("phrec", new GuiComponent(10, 94){{
			addText("Recieve energy from the "+ForgeDirection.getOrientation(tile.side).name(), 0, 0);
			addText("direction is", 0, 0);
			addButton(3,guiLeft+60,guiTop+10,22,20,!tile.canRecieve?"OFF":"ON");
		}});
	}
	
	@Override
	public void updateToolTips(String key) {
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}
}