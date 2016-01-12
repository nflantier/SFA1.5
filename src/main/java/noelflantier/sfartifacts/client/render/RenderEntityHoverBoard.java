package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;

public class RenderEntityHoverBoard extends Render{

	private ResourceLocation objMattelHB = new ResourceLocation(References.MODID+":textures/items/models/mattel_hoverboard.obj");
	private ResourceLocation textMattelHB = new ResourceLocation(References.MODID+":textures/items/models/mattel_hoverboard.png");
	private IModelCustom modelMattelHB;

	private ResourceLocation objPitBullHB = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_board.obj");
	private ResourceLocation textPitBullHB = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_board.png");
	private IModelCustom modelPitBullHB;
	
	private ResourceLocation objThruster = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_thruster.obj");
	private ResourceLocation textThruster = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_thruster.png");
	private IModelCustom modeThruster;
	
	public RenderEntityHoverBoard(){
		this.modelMattelHB = AdvancedModelLoader.loadModel(this.objMattelHB);
		this.modelPitBullHB = AdvancedModelLoader.loadModel(this.objPitBullHB);
		this.modeThruster = AdvancedModelLoader.loadModel(this.objThruster);
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		final EntityHoverBoard hoverboard = (EntityHoverBoard)entity;
		final EntityPlayer owner = hoverboard.getPlayer();
		if (owner == null) return;
		final float rotation = interpolateRotation(hoverboard.prevRotationYaw, hoverboard.rotationYaw, 0);
		
		GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(180.0F - rotation+90, 0.0F, 1.0F, 0.0F);
			
			GL11.glPushMatrix();
			if(hoverboard.getTypeHoverBoard()==ItemHoverBoard.MATTEL_HOVERBOARD){
				GL11.glTranslatef(0, -1.53F, 0);
				Minecraft.getMinecraft().renderEngine.bindTexture(textMattelHB);
				this.modelMattelHB.renderAll();
			}else if(hoverboard.getTypeHoverBoard()==ItemHoverBoard.PITBULL_HOVERBOARD){
				GL11.glTranslatef(+0.3F, -1.45F, 0);
				Minecraft.getMinecraft().renderEngine.bindTexture(textPitBullHB);
				this.modelPitBullHB.renderAll();
				
				Minecraft.getMinecraft().renderEngine.bindTexture(textThruster);
				this.modeThruster.renderAll();
				GL11.glTranslatef(0, 0, 0.69F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textThruster);
				this.modeThruster.renderAll();
			}
	        /*double minU = 0;
	        double maxU = 1;
	        double minV = 0;
	        double maxV = 1;
			Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(x+0.8, y-1.6, z+0.3, maxU, maxV);
	        tessellator.addVertexWithUV(x+0.8, y-1.6, z-0.3, maxU, minV);
	        tessellator.addVertexWithUV(x-0.8, y-1.6, z-0.3, minU, minV);
	        tessellator.addVertexWithUV(x-0.8, y-1.6, z+0.3, minU, maxV);
	        tessellator.draw();*/
	        GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	private static float interpolateRotation(float prevRotation, float nextRotation, float modifier) {
		float rotation = nextRotation - prevRotation;

		while (rotation < -180.0F)
			rotation += 360.0F;

		while (rotation >= 180.0F) {
			rotation -= 360.0F;
		}

		return prevRotation + modifier * rotation;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return textMattelHB;
	}

}
