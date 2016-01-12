package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import noelflantier.sfartifacts.common.blocks.BlockLightningRodStand;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

@SideOnly(Side.CLIENT)
public class RenderBlockLightningRodStand extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler,IItemRenderer {
		
	private IModelCustom modelLRpilon;
	private IModelCustom modelLRdownring;
	private IModelCustom modelLRupring;
	private IModelCustom modelLRball;
	
	private ResourceLocation resLRpilonT = new ResourceLocation(References.MODID+":textures/items/models/ltrpilon.png");
	private ResourceLocation resLRdownringT = new ResourceLocation(References.MODID+":textures/items/models/ltrdownring.png");
	private ResourceLocation resLRupringT = new ResourceLocation(References.MODID+":textures/items/models/ltrupring.png");
	private ResourceLocation resLRballT = new ResourceLocation(References.MODID+":textures/items/models/ltrball.png");

	private ResourceLocation objLrs= new ResourceLocation(References.MODID+":textures/blocks/models/lightningrodstand.obj");
	private ResourceLocation textureLrs= new ResourceLocation(References.MODID+":textures/blocks/models/lightningrodstand.png");
	private IModelCustom modelLrsr;

	private Block bl;
	
	public RenderBlockLightningRodStand(){
		this.modelLRpilon = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/items/models/ltrpilon.obj"));
		this.modelLRdownring = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/items/models/ltrdownring.obj"));
		this.modelLRupring = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/items/models/ltrupring.obj"));
		this.modelLRball = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/items/models/ltrball.obj"));
		this.modelLrsr = AdvancedModelLoader.loadModel(this.objLrs);
	}
	
	public RenderBlockLightningRodStand(Block b){		
		this();
		this.bl = b;
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x,double y, double z, float partialTickTime) {

		if(tile instanceof TileLightningRodStand == false)return;
		TileLightningRodStand mtile = (TileLightningRodStand)tile;

		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);
		
	    GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLrs);
			this.modelLrsr.renderAll();
        GL11.glPopMatrix();
        
		if(mtile.items[0]==null){
			GL11.glPopMatrix();
			return;
		}
		
		mtile.yrotpilon += partialTickTime;
		mtile.accytranspilon += mtile.pasytranspilon;
		mtile.ytranspilon += mtile.accytranspilon;
		if( (mtile.pasytranspilon>0 && mtile.ytranspilon>mtile.limitytranspilon/2)  || (mtile.pasytranspilon<0 && mtile.ytranspilon<mtile.limitytranspilon/2) ){
			mtile.pasytranspilon *= -1;
		}
		if(mtile.ytranspilon>mtile.limitytranspilon || mtile.ytranspilon<0){
			mtile.accytranspilon = 0;
		}
		
		GL11.glPushMatrix();
			GL11.glTranslatef(0F,-0.2F,0F);
			GL11.glRotatef(mtile.yrotpilon*0.5F, 0F, 1F, 0F);
			GL11.glTranslatef(0F, mtile.ytranspilon, 0F);
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resLRpilonT);
			this.modelLRpilon.renderAll();
		GL11.glPopMatrix();

		if( mtile.items[0].getItemDamage()>0 ){

			mtile.yrotdownring += partialTickTime;
			
			GL11.glPushMatrix();
				GL11.glRotatef(mtile.yrotdownring*1.2F, 0F, 1F, 0F);
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resLRdownringT);
				this.modelLRdownring.renderAll();
			GL11.glPopMatrix();
			
			if( mtile.items[0].getItemDamage()>1 ){

				mtile.yrotupring += partialTickTime;
				
				GL11.glPushMatrix();
					GL11.glRotatef(mtile.yrotupring*-2.2F, 0F, 1F, 0F);
					FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resLRupringT);
					this.modelLRupring.renderAll();
				GL11.glPopMatrix();
				
				if( mtile.items[0].getItemDamage()>2 ){

					mtile.yrotball += partialTickTime;
					mtile.accytransball += mtile.pasytransball;
					mtile.ytransball += mtile.accytransball;
					if( (mtile.pasytransball>0 && mtile.ytransball>mtile.limitytransball/2)  || (mtile.pasytransball<0 && mtile.ytransball<mtile.limitytransball/2) ){
						//this.pasytransball = (float) (Math.random() * (0.0005F-0.0001F)) + 0.0001F;
						mtile.pasytransball *= -1;
					}
					if( (mtile.limitytransball>0 && mtile.ytransball>mtile.limitytransball) || (mtile.ytransball<mtile.limitytransball && mtile.limitytransball<0) ){
						mtile.accytransball = 0;
						//this.pasytransball = (float) (Math.random() * (0.0005F-0.0001F)) + 0.0001F;
						mtile.limitytransball = (float) (Math.random() * 3.2F - 1.6F);
					}
					
					
					GL11.glPushMatrix();
						GL11.glRotatef(mtile.yrotball*3.2F, 0F, 1F, 0F);
						//GL11.glTranslatef(0F, this.ytransball, 0F);
						
						FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resLRballT);
						this.modelLRball.renderAll();
					GL11.glPopMatrix();
												
				}
				
			}
		}

		GL11.glPopMatrix();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {	
		int meta = world.getBlockMetadata(x, y, z);
		renderer.setOverrideBlockTexture(PillarMaterials.values()[meta].block.getIcon(0,0));
		renderer.renderStandardBlock(block, x,y, z);
		renderer.clearOverrideBlockTexture();
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return BlockLightningRodStand.renderId;
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
		        renderer.renderFaceXPos(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 

		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, -1.0F);
		        renderer.renderFaceXNeg(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 
		        
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, 1.0F);
		        renderer.renderFaceZPos(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 
		        
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, 1.0F);
		        renderer.renderFaceZNeg(this.bl, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(this.bl, 0));
		        tessellator.draw(); 
		        
		    renderer.clearOverrideBlockTexture();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    	    GL11.glPushMatrix();
    			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureLrs);
    			this.modelLrsr.renderAll();
            GL11.glPopMatrix();
	        
        GL11.glPopMatrix();
	}

}
