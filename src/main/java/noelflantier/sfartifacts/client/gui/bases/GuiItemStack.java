package noelflantier.sfartifacts.client.gui.bases;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiItemStack extends GuiComponentBase{

	public ItemStack stack = null;
	public boolean showSize = false;
	public int idRecipe = 0;
	
	public GuiItemStack(int x, int y) {
		super(x, y, 16, 16);
	}	
	
	public GuiItemStack(int x, int y, ItemStack stack) {
		this(x, y);
		this.stack = stack;
	}
	public GuiItemStack(ItemStack stack) {
		this(0, 0, stack);
	}
	public GuiItemStack(int x, int y, ItemStack stack, int id) {
		this(x, y, stack);
		this.idRecipe = id;
	}
	
	public GuiItemStack(int x, int y, int w, int h, ItemStack stack) {
		super(x, y, w, h);
		this.stack = stack;
	}
	
	public void draw(int x, int y) {
		if(stack==null)
			return;
        GL11.glPushMatrix();
	        RenderItem.getInstance().renderItemAndEffectIntoGUI(FR, Minecraft.getMinecraft().getTextureManager(), stack, this.x, this.y);
	        if(showSize || stack.stackSize>1)
	        	RenderItem.getInstance().renderItemOverlayIntoGUI(FR, Minecraft.getMinecraft().getTextureManager(), stack, this.x, this.y , stack.stackSize+"");
	        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
	}

	@Override
	public boolean isMouseHover(int mx, int my){
    	return mx<=this.x+this.width && mx>=this.x && my<this.y+this.height && my>this.y;
	}

}
