package noelflantier.sfartifacts.client.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;

public class RenderBlockMightyFoundry  extends TileEntitySpecialRenderer implements IItemRenderer {

	private Block bl;
	private ResourceLocation objBase= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrybase.obj");
	private ResourceLocation textureBase= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrybase.png");
	private ResourceLocation objGlass= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundryglass.obj");
	private ResourceLocation textureGlass= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundryglass.png");
	private ResourceLocation objMold= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrymold.obj");
	private ResourceLocation textureMold= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrymold.png");
	private ResourceLocation objMoldDone= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrymolddone.obj");
	private ResourceLocation textureMoldDone= new ResourceLocation(References.MODID+":textures/blocks/models/mightyfoundrymolddone.png");
	
	private IModelCustom modelBase;
	private IModelCustom modelGlass;
	private IModelCustom modelMold;
	private IModelCustom modelMoldDone;
	
	public RenderBlockMightyFoundry(){
		this.modelBase = AdvancedModelLoader.loadModel(this.objBase);
		this.modelGlass = AdvancedModelLoader.loadModel(this.objGlass);
		this.modelMold = AdvancedModelLoader.loadModel(this.objMold);
		this.modelMoldDone = AdvancedModelLoader.loadModel(this.objMoldDone);
	}
	
	public RenderBlockMightyFoundry(Block block) {
		this();
		this.bl = block;
	}
	
	public void renderItems(TileEntity tile){
        if(tile!=null && tile instanceof TileMightyFoundry){
        	TileMightyFoundry t = (TileMightyFoundry)tile;

        	for(int i =0;i<4;i++){
            	if(t.items[i+2]!=null){
        	        GL11.glPushMatrix();
    				GL11.glTranslatef((float)0.2, (float)0.62, (float)0);
    				float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/7))*7F;
    		        GL11.glRotatef(rot ,0F, 1F, 0F);
    				EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, t.items[i+2]);
    				itemEntity2.hoverStart = 0.0F;
    				GL11.glScalef(0.25F, 0.25F, 0.25F);
    				RenderItem.renderInFrame = true;
    				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
    				RenderItem.renderInFrame = false;
        	        GL11.glPopMatrix();
            	}
        	}
        }
		
	}
	public void renderLiquids(TileEntity tile){
        if(tile!=null && tile instanceof TileMightyFoundry){
        	TileMightyFoundry t = (TileMightyFoundry)tile;
        	List<FluidTank> l = t.getFluidTanks();

	        GL11.glPushMatrix();
	        	if(l.get(0)!=null && l.get(0).getFluid()!=null && l.get(0).getFluidAmount()>0){
	        		IIcon ic = l.get(0).getFluid().getFluid().getStillIcon();
	        		int amount = l.get(0).getFluidAmount()>0?l.get(0).getFluidAmount():1;
	        		float ratio = (float)amount /  (float)l.get(0).getCapacity();
	        		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	                Tessellator tessellator = Tessellator.instance;
	                double ts = 16.0D;
	                double minU = (double)ic.getInterpolatedU(0 * ts);
	                double maxU = (double)ic.getInterpolatedU(1 * ts);
	                double minV = (double)ic.getInterpolatedV(0 * ts);
	                double maxV = (double)ic.getInterpolatedV(1 * ts);
	                double d7 = maxU;
	                double d8 = minU;
	                double d9 = minV;
	                double d10 = maxV;
	                double rt = 0.001953125D;
	                tessellator.startDrawingQuads();
	                tessellator.setNormal(0.0F, 1.0F, 0.0F);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, 0.47, maxU, maxV);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, -0.47, maxU, minV);
	                tessellator.addVertexWithUV(-0.48, -0.39+0.5*ratio, -0.47, minU, minV);
	                tessellator.addVertexWithUV(-0.48, -0.39+0.5*ratio, 0.47, minU, maxV);
	                /*tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, 0.47, maxU-ts*0.64*rt, maxV-ts*0.64*rt);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, -0.47, d7-ts*0.64*rt, d9);
	                tessellator.addVertexWithUV(-0.48, -0.39+0.5*ratio, -0.47, minU, minV);
	                tessellator.addVertexWithUV(-0.48, -0.39+0.5*ratio, 0.47, d8, d10-ts*0.64*rt);*/
	                tessellator.draw();
	                tessellator.startDrawingQuads();
	                tessellator.addVertexWithUV(0.42, -0.39, -0.47, maxU, maxV);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, -0.47, maxU, minV);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, 0.47, minU, minV);
	                tessellator.addVertexWithUV(0.42, -0.39, 0.47, minU, maxV);
	                /*tessellator.addVertexWithUV(0.42, -0.39, -0.47, maxU-ts*0.64*rt, maxV-ts*(1-0.3*ratio)*rt);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, -0.47, d7-ts*0.64*rt, d9);
	                tessellator.addVertexWithUV(0.42, -0.39+0.5*ratio, 0.47, minU, minV);
	                tessellator.addVertexWithUV(0.42, -0.39, 0.47, d8, d10-ts*(1-0.3*ratio)*rt);*/
	                tessellator.draw();
	        }
	        	
	    GL11.glPopMatrix();
		}
	}
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);        
			
			int side = -1;
			if(tile!=null && tile instanceof TileMightyFoundry)side=((TileMightyFoundry)tile).side;
			switch(side){
				case 2:GL11.glRotatef(90, 0F, 1F, 0F);
					break;
				case 3:GL11.glRotatef(270, 0F, 1F, 0F);
					break;
				case 4:GL11.glRotatef(180, 0F, 1F, 0F);
					break;
				case 5:GL11.glRotatef(0, 0F, 1F, 0F);
					break;
				case -1:
					break;
				default:
					break;
			}

			renderItems(tile);
			renderLiquids(tile);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
			this.modelBase.renderAll();

	        GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 0.5F);
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGlass);
				this.modelGlass.renderAll();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
	        
	        if(tile!=null && tile instanceof TileMightyFoundry){
	        	TileMightyFoundry t = (TileMightyFoundry)tile;
	        	if(t.items[6]!=null){

	    	        GL11.glPushMatrix();
		    			GL11.glTranslatef((float)0, (float)-0.22, (float)-0.62);
		    			float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/7))*7F;
		    	        GL11.glRotatef(rot ,0F, 1F, 0F);
		    	        GL11.glRotatef(45 ,1F, 0F, 0F);
		    			EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, t.items[6]);
		    			itemEntity2.hoverStart = 0.0F;
		    			GL11.glScalef(0.15F, 0.15F, 0.15F);
		    			RenderItem.renderInFrame = true;
		    			RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		    			RenderItem.renderInFrame = false;
	    	        GL11.glPopMatrix();
	    	        
	    			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureMoldDone);
	    			this.modelMoldDone.renderAll();
	        	}else{
	    			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureMold);
	    			this.modelMold.renderAll();
	        	}
	        }
			
		GL11.glPopMatrix();
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

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	    GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
			this.modelBase.renderAll();
	
	        GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 0.5F);
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGlass);
				this.modelGlass.renderAll();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
	
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureMold);
			this.modelMold.renderAll();
        GL11.glPopMatrix();
	}

}
