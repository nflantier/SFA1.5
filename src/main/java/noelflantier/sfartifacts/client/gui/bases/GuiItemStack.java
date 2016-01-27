package noelflantier.sfartifacts.client.gui.bases;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiItemStack extends GuiComponentBase{

	public ItemStack stack;
	
	public GuiItemStack(int x, int y) {
		super(x, y);
	}	
	
	public GuiItemStack(int x, int y, ItemStack stack) {
		super(x, y);
		this.stack = stack;
	}
	
	public void draw(int x, int y) {
        RenderItem.getInstance().renderItemAndEffectIntoGUI(FR, Minecraft.getMinecraft().getTextureManager(), stack, this.x, this.y);
	}

	@Override
	public boolean isMouseHover(int mx, int my){
		return mx<=this.x+this.width && mx>=this.x && my<this.y-this.height && my>this.y;
	}

}
