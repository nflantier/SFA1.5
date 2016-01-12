package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class RenderBlockMrFusion  extends TileEntitySpecialRenderer implements IItemRenderer {

	private ResourceLocation objBottom= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-bottom.obj");
	private ResourceLocation textureBottom= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-bottom.png");
	private IModelCustom modelBottom;
	private ResourceLocation objTop= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-top.obj");
	private ResourceLocation textureTop= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-top.png");
	private IModelCustom modelTop;
	private ResourceLocation objLock= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-lock.obj");
	private ResourceLocation textureLock= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-lock.png");
	private IModelCustom modelLock;
	private ResourceLocation objGouj= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-gouj.obj");
	private ResourceLocation textureGouj= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-gouj.png");
	private IModelCustom modelGouj;
	private ResourceLocation objGourd= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-gourd.obj");
	private ResourceLocation textureGourd= new ResourceLocation(References.MODID+":textures/blocks/models/mrfusion-gourd.png");
	private IModelCustom modelGourd;
	
	public RenderBlockMrFusion(){
		this.modelBottom = AdvancedModelLoader.loadModel(this.objBottom);
		this.modelTop = AdvancedModelLoader.loadModel(this.objTop);
		this.modelLock = AdvancedModelLoader.loadModel(this.objLock);
		this.modelGouj = AdvancedModelLoader.loadModel(this.objGouj);
		this.modelGourd = AdvancedModelLoader.loadModel(this.objGourd);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
	    GL11.glPushMatrix();
	    	GL11.glTranslatef(0.5F, 0.1F, 0.5F);
	    	GL11.glScalef(1.2F, 1.2F, 1.2F);
	    	Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBottom);
			this.modelBottom.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureTop);
			this.modelTop.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLock);
			this.modelLock.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGouj);
			this.modelGouj.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGourd);
			this.modelGourd.renderAll();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
		if(tile==null)
			return;
		 GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y, (float)z+0.5F);

			int side = -1;
			if(tile instanceof TileSFA)side=((TileSFA)tile).side;
			switch(side){
				case 0:
					GL11.glTranslatef(0F, +0.2F, 0F);
					break;
				case 1:
					GL11.glRotatef(180, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -0.8F, 0F);
					break;
				case 2:
					GL11.glRotatef(90, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -0.3F, -0.5F);
					break;
				case 3:
					GL11.glRotatef(-90, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -0.3F, +0.5F);
					break;
				case 4:
					GL11.glRotatef(-90, 0F, 0F, 1F);
					GL11.glTranslatef(-0.5F, -0.3F, 0F);
					break;
				case 5:
					GL11.glRotatef(90, 0F, 0F, 1F);
					GL11.glTranslatef(+0.5F, -0.3F, 0F);
					break;
				case -1:
					break;
				default:
					break;
			}
	        GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBottom);
				this.modelBottom.renderAll();
			GL11.glPopMatrix();
	        GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureTop);
				this.modelTop.renderAll();
			GL11.glPopMatrix();
	        GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLock);
				this.modelLock.renderAll();
			GL11.glPopMatrix();
	        GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGouj);
				this.modelGouj.renderAll();
			GL11.glPopMatrix();
	        GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 0.5F);
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGourd);
				this.modelGourd.renderAll();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		GL11.glPopMatrix(); 
	}
}
