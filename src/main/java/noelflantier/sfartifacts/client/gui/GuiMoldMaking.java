package noelflantier.sfartifacts.client.gui;

import java.util.Enumeration;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerMoldMaking;

public class GuiMoldMaking  extends GuiSFA{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiMoldMaking.png");
	private static final ResourceLocation moldFilled = new ResourceLocation(References.MODID+":textures/items/mold_filled.png");
	private static final ResourceLocation moldEmpty = new ResourceLocation(References.MODID+":textures/items/mold_empty.png");
	public int currentSlot;
	public int moldMeta;
	
	public GuiMoldMaking(EntityPlayer player) {
		super(new ContainerMoldMaking(player));
		this.currentSlot = player.inventory.currentItem;
		this.moldMeta = player.getCurrentEquippedItem().getItemDamage();
		this.xSize = 176;
		this.ySize = 200;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();this.componentList.put("im", 
				new GuiImage(8+18*this.currentSlot, 176, 16,16 , 0F, 0F, 1F, 1F,moldMeta==0?moldEmpty:moldFilled)
				);
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
