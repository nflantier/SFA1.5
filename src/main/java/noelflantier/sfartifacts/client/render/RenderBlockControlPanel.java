package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.handlers.ModBlocks;

public class RenderBlockControlPanel extends TileEntitySpecialRenderer  implements IItemRenderer {

	private ResourceLocation objControlPanel= new ResourceLocation(References.MODID+":textures/blocks/models/controlpanel.obj");
	private ResourceLocation textureControlPanel= new ResourceLocation(References.MODID+":textures/blocks/models/controlpanel.png");
	private IModelCustom modelControlPanel;
	
	private ResourceLocation textureScreen= new ResourceLocation(References.MODID+":textures/blocks/models/screen.png");
	
	private long theTick;
	private int screenIndex = 0;
	
	public RenderBlockControlPanel(){
		this.modelControlPanel = AdvancedModelLoader.loadModel(this.objControlPanel);
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
			GL11.glTranslatef(0.3F, 0F, 0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureControlPanel);
			this.modelControlPanel.renderAll();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
		long ct = SFArtifacts.instance.myProxy.sfaEvents.getClientTick();
		if(this.theTick==0)this.theTick = ct;
        GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);        

			int side = -1;
			if(tile!=null && tile instanceof TileControlPannel)side=((TileControlPannel)tile).side;
			switch(side){
				case 2:GL11.glRotatef(90, 0F, 1F, 0F);
					break;
				case 3:GL11.glRotatef(270, 0F, 1F, 0F);
					break;
				case 4:GL11.glRotatef(180, 0F, 1F, 0F);
					break;
				case 5:
					break;
				case -1:
					break;
				default:
					break;
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureControlPanel);
			this.modelControlPanel.renderAll();
			
			if(tile!=null && tile instanceof TileControlPannel &&  ((TileControlPannel)tile).hasMaster() && ((TileControlPannel)tile).isMasterStillMaster()){
				GL11.glTranslatef((float)-1.24F, (float)-0.5F, (float)-0.5F); 
				
	    	    if(ct-1>this.theTick){
	    	    	this.theTick = ct;
	    	    	this.screenIndex = this.screenIndex>=31?0:this.screenIndex+1;
	    	    }
				
	    		Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureScreen);
	            Tessellator tessellator = Tessellator.instance;
	            IIcon ic = ModBlocks.blockAsgardite.getIcon(0, 0);
	            double minU = (double)ic.getMinU();
	            double maxU = (double)ic.getMaxU();
	            double minV = (double)ic.getMinV();
	            double maxV = (double)ic.getMaxV();
	            double d7 = maxU;
	            double d8 = minU;
	            double d9 = minV;
	            double d10 = maxV;
	            float f = 0.03125F;
	            
	            tessellator.startDrawingQuads();
	            tessellator.setNormal(0.0F, 0.0F, 1.0F);
	            tessellator.addVertexWithUV(1, 0.1, 0.1, 1, 0.03125F+(f*this.screenIndex));
	            tessellator.addVertexWithUV(1, 0.9, 0.1, 1, 0+(f*this.screenIndex));
	            tessellator.addVertexWithUV(1, 0.9, 0.9, 0, 0+(f*this.screenIndex));
	            tessellator.addVertexWithUV(1, 0.1, 0.9, 0, 0.03125F+(f*this.screenIndex));
	            tessellator.draw();
				//TextureUtil.func_147945_b();
			}
			
	    GL11.glPopMatrix();
	}

}
