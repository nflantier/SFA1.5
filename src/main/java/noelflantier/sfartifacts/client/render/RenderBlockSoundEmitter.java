package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianGlass;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianSteel;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.blocks.BlockSoundEmitter;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.helpers.RenderBlocksHelper;

public class RenderBlockSoundEmitter  extends TileEntitySpecialRenderer  implements IItemRenderer,ISimpleBlockRenderingHandler {

	private ResourceLocation objTop= new ResourceLocation(References.MODID+":textures/blocks/models/topsound.obj");
	private ResourceLocation textureTop= new ResourceLocation(References.MODID+":textures/blocks/models/topsound.png");
	private IModelCustom modelTop;
	private ResourceLocation objAmp= new ResourceLocation(References.MODID+":textures/blocks/models/amplifier.obj");
	private ResourceLocation textureAmp= new ResourceLocation(References.MODID+":textures/blocks/models/amplifier.png");
	private IModelCustom modelAmp;
	private ResourceLocation objGlass= new ResourceLocation(References.MODID+":textures/blocks/models/glasssound.obj");
	private ResourceLocation textureGlass= new ResourceLocation(References.MODID+":textures/blocks/models/glasssound.png");
	private IModelCustom modelGlass;
	
	public RenderBlockSoundEmitter(){
		this.modelTop = AdvancedModelLoader.loadModel(this.objTop);
		this.modelAmp = AdvancedModelLoader.loadModel(this.objAmp);
		this.modelGlass = AdvancedModelLoader.loadModel(this.objGlass);
		
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
		GL11.glPushMatrix();
			renderer.setOverrideBlockTexture(ModBlocks.blockSoundEmiter.getIcon(0, 0));
			Tessellator tessellator = Tessellator.instance;
        	renderer.setRenderBoundsFromBlock(ModBlocks.blockSoundEmiter);
         	GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	        
	        tessellator.startDrawingQuads();
	        tessellator.setNormal(0.0F, 0.0F, 1.0F);
	        renderer.renderFaceZPos(ModBlocks.blockSoundEmiter, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(ModBlocks.blockSoundEmiter, 2));
	        tessellator.draw(); 
	        
	        tessellator.startDrawingQuads();
	        tessellator.setNormal(0.0F, 0.0F, -1.0F);
	        renderer.renderFaceZNeg(ModBlocks.blockSoundEmiter, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(ModBlocks.blockSoundEmiter, 2));
	        tessellator.draw(); 
	        
	        tessellator.startDrawingQuads();
	        tessellator.setNormal(1.0F, 0.0F, 0.0F);
	        renderer.renderFaceXPos(ModBlocks.blockSoundEmiter, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(ModBlocks.blockSoundEmiter, 2));
	        tessellator.draw(); 

	        tessellator.startDrawingQuads();
	        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	        renderer.renderFaceXNeg(ModBlocks.blockSoundEmiter, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(ModBlocks.blockSoundEmiter, 2));
	        tessellator.draw(); 

	        renderer.clearOverrideBlockTexture();
        	renderer.setRenderBoundsFromBlock(ModBlocks.blockAsgardianSteel);
	        tessellator.startDrawingQuads();
	        tessellator.setNormal(0.0F, -1.0F, 0.0F);
	        renderer.renderFaceYNeg(ModBlocks.blockSoundEmiter, 0.0D, 0.0D, 0.0D, ModBlocks.blockAsgardianSteel.getIcon(0, 0));
	        tessellator.draw(); 
	        
	        renderer.clearOverrideBlockTexture();
	    	GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	        GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureTop);
				this.modelTop.renderAll();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureAmp);
				this.modelAmp.renderAll();
	        GL11.glPopMatrix();
	   GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {

        GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);

			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureTop);
			this.modelTop.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureAmp);
			this.modelAmp.renderAll();

	        /*GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGlass);
				this.modelGlass.renderAll();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();*/
        GL11.glPopMatrix();
		RenderBlocksHelper.renderSimpleFace(x, y, z, "y+", ((BlockAsgardianGlass)ModBlocks.blockAsgardianGlass).getIcon(0, 0), 1F ,0.03F, 0.0F);
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, "x", block.getIcon(0, 0));
		RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, "z", block.getIcon(0, 0));
		RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, "y-", ((BlockAsgardianSteel)ModBlocks.blockAsgardianSteel).blockCIcon);
		//RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, "y+", ((BlockAsgardianGlass)ModBlocks.blockAsgardianGlass).getIcon(0, 0));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return BlockSoundEmitter.renderId;
	}
	
}
