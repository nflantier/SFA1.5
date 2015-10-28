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
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.BlockLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.helpers.RenderBlocksHelper;

public class RenderBlockLiquefier  extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler,IItemRenderer {

	private Block bl;
	private ResourceLocation objLiquefier= new ResourceLocation(References.MODID+":textures/blocks/models/liquefier.obj");
	private ResourceLocation textureLiquefier= new ResourceLocation(References.MODID+":textures/blocks/models/liquefier.png");
	private IModelCustom modelLiquefier;
	
	public RenderBlockLiquefier(){
		this.modelLiquefier = AdvancedModelLoader.loadModel(this.objLiquefier);
	}

	public RenderBlockLiquefier(Block b){
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
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLiquefier);
				this.modelLiquefier.renderAll();
	        GL11.glPopMatrix();
	        
	        /*GL11.glPushMatrix();
				GL11.glTranslatef((float)0.4F, (float)-0.15F, (float)0F);
				EntityItem itemEntity2 = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(ModItems.itemFluidModule));
				itemEntity2.hoverStart = 0.0F;
				GL11.glScalef(0.9F, 0.9F, 0.9F);
				RenderItem.renderInFrame = true;
				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				RenderItem.renderInFrame = false;
	        GL11.glPopMatrix();*/
	        
        GL11.glPopMatrix();
	}

	public void renderLiquids(TileEntity tile){
        if(tile!=null && tile instanceof TileLiquefier){
        	TileLiquefier t = (TileLiquefier)tile;
        	List<FluidTank> l = t.getFluidTanks();

	        GL11.glPushMatrix();
	        	if(l.get(1)!=null && l.get(1).getFluid()!=null && l.get(1).getFluidAmount()>0){
	        		IIcon ic = l.get(1).getFluid().getFluid().getStillIcon();
	        		int amount = l.get(1).getFluidAmount()>0?l.get(1).getFluidAmount():1;
	        		float ratio = (float)amount /  (float)l.get(1).getCapacity();
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
	                tessellator.startDrawingQuads();
	                tessellator.setNormal(0.0F, 1.0F, 0.0F);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, maxU, maxV);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7, d9);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, -0.18, minU, minV);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, 0.18, d8, d10);
	                tessellator.draw();
	                tessellator.startDrawingQuads();
	                tessellator.addVertexWithUV(0.48, -0.133, -0.18, maxU, maxV);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7, d9);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, minU, minV);
	                tessellator.addVertexWithUV(0.48, -0.133, 0.18, d8, d10);
	                tessellator.draw();
	        	}
	        	GL11.glRotatef(180, 0F, 1F, 0F);
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
	                tessellator.startDrawingQuads();
	                tessellator.setNormal(0.0F, 1.0F, 0.0F);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, maxU, maxV);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7, d9);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, -0.18, minU, minV);
	                tessellator.addVertexWithUV(0.12, -0.133+0.3*ratio, 0.18, d8, d10);
	                tessellator.draw();
	                tessellator.startDrawingQuads();
	                tessellator.addVertexWithUV(0.48, -0.133, -0.18, maxU, maxV);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, -0.18, d7, d9);
	                tessellator.addVertexWithUV(0.48, -0.133+0.3*ratio, 0.18, minU, minV);
	                tessellator.addVertexWithUV(0.48, -0.133, 0.18, d8, d10);
	                tessellator.draw();
	        	}
	        GL11.glPopMatrix();
        }
	}
	
	public void renderItems(TileEntity tile){
        if(tile!=null && tile instanceof TileLiquefier){
        	TileLiquefier t = (TileLiquefier)tile;
        	if(t.items[0]!=null){
    	        GL11.glPushMatrix();
    				GL11.glTranslatef((float)0.F, (float)0.35, (float)0);
    				float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/7))*7F;
    		        GL11.glRotatef(rot ,0F, 1F, 0F);
    				EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, t.items[0]);
    				itemEntity2.hoverStart = 0.0F;
    				GL11.glScalef(0.5F, 0.5F, 0.5F);
    				RenderItem.renderInFrame = true;
    				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
    				RenderItem.renderInFrame = false;
    	        GL11.glPopMatrix();
        	}
        }
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {

        GL11.glPushMatrix();
		GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);        
		
		int side = -1;
		if(tile!=null && tile instanceof TileLiquefier)side=((TileLiquefier)tile).side;
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

		renderLiquids(tile);
		renderItems(tile);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLiquefier);
		this.modelLiquefier.renderAll();
	        GL11.glPushMatrix();
				GL11.glTranslatef((float)-0.3F, (float)-0.15F, (float)0);
				EntityItem itemEntity2 = new EntityItem(tile.getWorldObj(), 0, 0, 0, new ItemStack(ModItems.itemFluidModule));
				itemEntity2.hoverStart = 0.0F;
				GL11.glScalef(0.9F, 0.9F, 0.9F);
				RenderItem.renderInFrame = true;
				RenderManager.instance.renderEntityWithPosYaw(itemEntity2, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				RenderItem.renderInFrame = false;
	        GL11.glPopMatrix();
	        GL11.glPushMatrix();
				GL11.glTranslatef((float)+0.3F, (float)-0.15F, (float)0);
				EntityItem itemEntity3 = new EntityItem(tile.getWorldObj(), 0, 0, 0, new ItemStack(ModItems.itemFluidModule));
				itemEntity3.hoverStart = 0.0F;
				GL11.glScalef(0.9F, 0.9F, 0.9F);
				RenderItem.renderInFrame = true;
				RenderManager.instance.renderEntityWithPosYaw(itemEntity3, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				RenderItem.renderInFrame = false;
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
		if(tile!=null && tile instanceof TileLiquefier)side=((TileLiquefier)tile).side;
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
		return BlockLiquefier.renderId;
	}

}
