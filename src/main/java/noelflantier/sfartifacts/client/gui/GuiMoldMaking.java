package noelflantier.sfartifacts.client.gui;

import java.util.Enumeration;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.gui.ContainerMoldMaking;
import noelflantier.sfartifacts.common.helpers.guis.GuiImage;
import noelflantier.sfartifacts.common.helpers.guis.GuiSFA;
import noelflantier.sfartifacts.common.helpers.guis.GuiToolTips;

public class GuiMoldMaking  extends GuiSFA{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiMoldMaking.png");
	private static final ResourceLocation moldFilled = new ResourceLocation(References.MODID+":textures/items/mold_filled.png");
	private static final ResourceLocation moldEmpty = new ResourceLocation(References.MODID+":textures/items/mold_empty.png");
	public int currentSlot;
	
	public GuiMoldMaking(InventoryPlayer inventory, IInventory itemInv, int slt) {
		super(new ContainerMoldMaking(inventory, itemInv, slt));
		this.currentSlot = slt;
		this.xSize = 176;
		this.ySize = 200;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		int meta = this.inventorySlots.getSlot(this.currentSlot).getStack().getItemDamage();
		this.componentList.put("im", 
			new GuiImage(8+18*this.currentSlot, 176, 16,16 , 0F, 0F, 1F, 1F,meta==0?moldEmpty:moldFilled)
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
