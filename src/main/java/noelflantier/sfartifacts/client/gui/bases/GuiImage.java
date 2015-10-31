package noelflantier.sfartifacts.client.gui.bases;

import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends GuiComponent{

	public ResourceLocation rl;
	public boolean useUV = false;
	public float minu = 0;
	public float minv = 0;
	public float maxu = 1;
	public float maxv = 1;
	
	public GuiImage(int x, int y, ResourceLocation rl){
		super(x, y);
		this.rl = rl;
	}
	
	public GuiImage(int x, int y, int w, int h, ResourceLocation rl){
		super(x,y,w,h);
		this.rl = rl;
	}

	public GuiImage(int x, int y, int w, int h, float minu, float minv, float maxu, float maxv, ResourceLocation rl){
		super(x,y,w,h);
		this.rl = rl;
		this.maxu = maxu;
		this.maxv  = maxv;
		this.minu = minu;
		this.minv  = minv;
	}
	
	@Override
	public void draw(int x, int y){
		renderImage(x,y,minu,minv,maxu,maxv);
	}
	
	public void renderImage(int x, int y) {
		Minecraft.getMinecraft().renderEngine.bindTexture(this.rl);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1f, 1f, 1f, 1f);
	        float f = 0.0625F;
	        float f1 = 0.0625F;
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV((double)(this.x + 0), (double)(this.y + this.height), (double)1, (double)((float)0), (double)((float)1));
	        tessellator.addVertexWithUV((double)(this.x + this.width), (double)(this.y + this.height), (double)1, (double)((float)1), (double)((float)1));
	        tessellator.addVertexWithUV((double)(this.x + this.width), (double)(this.y + 0), (double)1, (double)((float)1), (double)((float)0));
	        tessellator.addVertexWithUV((double)(this.x + 0), (double)(this.y + 0), (double)1, (double)((float)0), (double)((float)0));
	        tessellator.draw();
			//gui.drawTexturedModalRect(this.x, this.y, 0, 0, this.width, this.height);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}	
	
	public void renderImage(int x, int y, float minu, float minv, float maxu, float maxv) {
		Minecraft.getMinecraft().renderEngine.bindTexture(this.rl);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1f, 1f, 1f, 1f);
	        float f = 0.015625F;
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV((double)(this.x + 0), (double)(this.y + this.height), (double)1, (double)((float)minu), (double)((float)maxv));
	        tessellator.addVertexWithUV((double)(this.x + this.width), (double)(this.y + this.height), (double)1, (double)((float)maxu), (double)((float)maxv));
	        tessellator.addVertexWithUV((double)(this.x + this.width), (double)(this.y + 0), (double)1, (double)((float)maxu), (double)((float)minv));
	        tessellator.addVertexWithUV((double)(this.x + 0), (double)(this.y + 0), (double)1, (double)((float)minu), (double)((float)minv));
	        tessellator.draw();
			//gui.drawTexturedModalRect(this.x, this.y, 0, 0, this.width, this.height);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}