package noelflantier.sfartifacts.client.gui.bases;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

public class GuiRender {

	public static void renderTask(int capacity, int amount, double x, double y, double zLevel, double width, double height, double xT, double yT){
		renderHorizontalRectangle(capacity, amount, x, y, zLevel, width, height, xT, yT);
	}
	
	public static void renderHorizontalRectangle(int capacity, int amount, double x, double y, double zLevel, double width, double height, double xT, double yT){
	    int renderAmount = (int) ((capacity-amount) * width / capacity);
	    int posX = (int) (x); 
	    
	    GL11.glEnable(GL11.GL_BLEND);
	    float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(posX + 0), (double)(y + height), (double)zLevel, (double)((float)(xT + 0) * f), (double)((float)(yT + height) * f1));
        tessellator.addVertexWithUV((double)(posX + renderAmount), (double)(y + height), (double)zLevel, (double)((float)(xT + renderAmount) * f), (double)((float)(yT + height) * f1));
        tessellator.addVertexWithUV((double)(posX + renderAmount), (double)(y + 0), (double)zLevel, (double)((float)(xT + renderAmount) * f), (double)((float)(yT + 0) * f1));
        tessellator.addVertexWithUV((double)(posX + 0), (double)(y + 0), (double)zLevel, (double)((float)(xT + 0) * f), (double)((float)(yT + 0) * f1));
        tessellator.draw();
	    GL11.glDisable(GL11.GL_BLEND);
	}

	public static void renderEnergy(int capacity, int amount, double x, double y, double zLevel, double width, double height, double xT, double yT){
		renderVerticalRectangle(capacity, amount, x, y, zLevel, width, height, xT, yT);
	}
	
	public static void renderVerticalRectangle(int capacity, int amount, double x, double y, double zLevel, double width, double height, double xT, double yT){

	    int renderAmount = (int) Math.max(Math.min(height, amount * height / capacity), 0);
	    int posY = (int) (y + height - renderAmount); 
	    
	    GL11.glEnable(GL11.GL_BLEND);
	    float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(posY + renderAmount), (double)zLevel, (double)((float)(xT + 0) * f), (double)((float)(yT + renderAmount) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(posY + renderAmount), (double)zLevel, (double)((float)(xT + width) * f), (double)((float)(yT + renderAmount) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(posY + 0), (double)zLevel, (double)((float)(xT + width) * f), (double)((float)(yT + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(posY + 0), (double)zLevel, (double)((float)(xT + 0) * f), (double)((float)(yT + 0) * f1));
        tessellator.draw();
	    GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void renderFluid(FluidTank tank, double x, double y, double zLevel, double width, double height) {
		renderFluidStack(tank.getFluid(), tank.getCapacity(), tank.getFluidAmount(), x, y, zLevel, width, height);
	}
	public static void renderFluidStack(FluidStack fluid, int capacity, int amount, double x, double y, double zLevel, double width, double height) {
	    if(fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
	      return;
	    }

	    IIcon icon = fluid.getFluid().getStillIcon();
	    if(icon == null) {
	      icon = fluid.getFluid().getIcon();
	      if(icon == null) {
	        return;
	      }
	    }

	    int renderAmount = (int) Math.max(Math.min(height, amount * height / capacity), 1);
	    int posY = (int) (y + height - renderAmount);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	    int color = fluid.getFluid().getColor(fluid);
	    GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));

	    GL11.glEnable(GL11.GL_BLEND);
	    for (int i = 0; i < width; i += 16) {
	      for (int j = 0; j < renderAmount; j += 16) {
	        int drawWidth = (int) Math.min(width - i, 16);
	        int drawHeight = Math.min(renderAmount - j, 16);

	        int drawX = (int) (x + i);
	        int drawY = posY + j;

	        double minU = icon.getMinU();
	        double maxU = icon.getMaxU();
	        double minV = icon.getMinV();
	        double maxV = icon.getMaxV();

	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(drawX, drawY + drawHeight, 0, minU, minV + (maxV - minV) * drawHeight / 16F);
	        tessellator.addVertexWithUV(drawX + drawWidth, drawY + drawHeight, 0, minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F);
	        tessellator.addVertexWithUV(drawX + drawWidth, drawY, 0, minU + (maxU - minU) * drawWidth / 16F, minV);
	        tessellator.addVertexWithUV(drawX, drawY, 0, minU, minV);
	        tessellator.draw();
	      }
	    }
	    GL11.glDisable(GL11.GL_BLEND);
	  }
}
