package noelflantier.sfartifacts.client.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
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
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.BlockInjector;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.helpers.RenderBlocksHelper;

public class RenderBlockInjector extends TileEntitySpecialRenderer  implements ISimpleBlockRenderingHandler,IItemRenderer {

	private Block bl;
	private ResourceLocation objFrame= new ResourceLocation(References.MODID+":textures/blocks/models/injectorframe.obj");
	private ResourceLocation textureFrame= new ResourceLocation(References.MODID+":textures/blocks/models/injectorframe.png");
	private IModelCustom modelFrame;
	
	private ResourceLocation objGlass= new ResourceLocation(References.MODID+":textures/blocks/models/injectorglass.obj");
	private ResourceLocation textureGlass= new ResourceLocation(References.MODID+":textures/blocks/models/injectorglass.png");
	private IModelCustom modelGlass;
	
	public RenderBlockInjector(){
		this.modelFrame = AdvancedModelLoader.loadModel(this.objFrame);
		this.modelGlass = AdvancedModelLoader.loadModel(this.objGlass);
	}
	
	public RenderBlockInjector(Block b) {
		this();
		this.bl = b;
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
		RenderBlocks renderer = (RenderBlocks)data[0];
		int metadata = item.getItemDamage();
        GL11.glPushMatrix();
			renderer.setOverrideBlockTexture(PillarMaterials.values()[metadata].block.getIcon(0,0));
	        Tessellator tessellator = Tessellator.instance;
	        	renderer.setRenderBoundsFromBlock(this.bl);
	         	GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, -1.0F);
		        renderer.renderFaceZNeg(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 
		        
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, 1.0F);
		        renderer.renderFaceZPos(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 
		        
		    renderer.clearOverrideBlockTexture();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		    GL11.glPushMatrix();
		    	GL11.glRotatef(180, 0F, 1F, 0F);
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureFrame);
				this.modelFrame.renderAll();
	        GL11.glPopMatrix();

	        
        GL11.glPopMatrix();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileEntity tile = world.getTileEntity(x, y, z);
		int side = -1;
		int meta = world.getBlockMetadata(x, y, z);
		if(tile!=null && tile instanceof TileInjector)side=((TileInjector)tile).side;
		switch(side){
			case 2:
				RenderBlocksHelper.renderFace(world, renderer, block, x, y, z, "x", PillarMaterials.values()[meta].block.getIcon(0,0));
				break;
			case 3:
				RenderBlocksHelper.renderFace(world, renderer, block, x, y, z, "x", PillarMaterials.values()[meta].block.getIcon(0,0));
				break;
			case 4:
				RenderBlocksHelper.renderFace(world, renderer, block, x, y, z, "z", PillarMaterials.values()[meta].block.getIcon(0,0));
				break;
			case 5:
				RenderBlocksHelper.renderFace(world, renderer, block, x, y, z, "z", PillarMaterials.values()[meta].block.getIcon(0,0));
				break;
			case -1:
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return BlockInjector.renderId;
	}
	public void renderLiquids(TileEntity tile){
        if(tile!=null && tile instanceof TileInjector){
        	TileInjector t = (TileInjector)tile;
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
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, maxU-ts*0.64*rt, maxV-ts*0.64*rt);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7-ts*0.64*rt, d9);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, -0.18, minU, minV);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, 0.18, d8, d10-ts*0.64*rt);
	                tessellator.draw();
	                tessellator.startDrawingQuads();
	                tessellator.addVertexWithUV(0.48, -0.133, -0.18, maxU-ts*0.64*rt, maxV-ts*(1-0.3*ratio)*rt);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7-ts*0.64*rt, d9);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, minU, minV);
	                tessellator.addVertexWithUV(0.48, -0.133, 0.18, d8, d10-ts*(1-0.3*ratio)*rt);
	                tessellator.draw();
	                
		        	GL11.glRotatef(180, 0F, 1F, 0F);
	                tessellator.startDrawingQuads();
	                tessellator.setNormal(0.0F, 1.0F, 0.0F);
	                tessellator.addVertexWithUV(0.435, -0.41+0.4*ratio, 0.40, maxU-ts*0.465*rt, maxV-ts*0.20*rt);
	                tessellator.addVertexWithUV(0.435, -0.41+0.4*ratio, -0.40, d7-ts*0.465*rt, d9);
	                tessellator.addVertexWithUV(-0.1, -0.41+0.4*ratio, -0.40, minU, minV);
	                tessellator.addVertexWithUV(-0.1, -0.41+0.4*ratio, 0.40, d8, d10-ts*0.20*rt);
	                tessellator.draw();
	                tessellator.startDrawingQuads();
	                tessellator.addVertexWithUV(0.435, -0.41, -0.40, maxU-ts*0.20*rt, maxV-ts*(1-0.4*ratio)*rt);
	                tessellator.addVertexWithUV(0.435, -0.41+0.4*ratio, -0.40, d7-ts*0.20*rt, d9);
	                tessellator.addVertexWithUV(0.435, -0.41+0.4*ratio, 0.40, minU, minV);
	                tessellator.addVertexWithUV(0.435, -0.41, 0.40, d8, d10-ts*(1-0.4*ratio)*rt);
	                tessellator.draw();
	        	}
	        GL11.glPopMatrix();
        }
	}

	public void renderItems(TileEntity tile){
        if(tile!=null && tile instanceof TileInjector){
        	TileInjector t = (TileInjector)tile;
        	float tz[] = new float[]{-0.25F,-0.25F,-0F,-0F,+0.25F,+0.25F};
        	float tx[] = new float[]{-0.1F,-0.25F,-0.1F,-0.25F,-0.1F,-0.25F};
        	
        	double yit = -0.133F;
        	List<FluidTank> l = t.getFluidTanks();
        	int amount = l.get(0).getFluidAmount()>0?l.get(0).getFluidAmount():1;
    		float ratio = (float)amount /  (float)l.get(0).getCapacity();
    		if(amount>0)yit = -0.41+0.4*ratio;

        	for(int i =0;i<6;i++){
            	if(t.items[i+1]!=null){
        	        GL11.glPushMatrix();
        				GL11.glTranslatef((float)tx[i], (float)yit-0.08F, (float)tz[i]);
        		        GL11.glRotatef(45 ,0F, 1F, 0F);
        				EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, t.items[i+1]);
        				itemEntity2.hoverStart = 0.0F;
        				GL11.glScalef(0.5F, 0.5F, 0.5F);
        				RenderItem.renderInFrame = true;
        				GL11.glColor4f(1F, 1F, 1F, 1F);
        				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        				GL11.glColor4f(1F, 1F, 1F, 1F);
        				RenderItem.renderInFrame = false;
        	        GL11.glPopMatrix();
            	}
        	}
        }
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {

        GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);        
			
			int side = -1;
			if(tile!=null && tile instanceof TileInjector)side=((TileInjector)tile).side;
			switch(side){
				case 2:GL11.glRotatef(270, 0F, 1F, 0F);
					break;
				case 3:GL11.glRotatef(90, 0F, 1F, 0F);
					break;
				case 4:
					break;
				case 5:GL11.glRotatef(180, 0F, 1F, 0F);
					break;
				case -1:
					break;
				default:
					break;
			}
			

			renderLiquids(tile);
			renderItems(tile);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureFrame);
			this.modelFrame.renderAll();
	
	        GL11.glPushMatrix();
				GL11.glTranslatef((float)+0.3F, (float)-0.15F, (float)0);
				EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, new ItemStack(ModItems.itemFluidModule));
				itemEntity2.hoverStart = 0.0F;
				GL11.glScalef(0.9F, 0.9F, 0.9F);
				RenderItem.renderInFrame = true;
				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				RenderItem.renderInFrame = false;
	        GL11.glPopMatrix();

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
			
        GL11.glPopMatrix();
	}

}
