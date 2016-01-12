package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class RenderBlockHammerStand extends TileEntitySpecialRenderer  implements IItemRenderer{

	private IModelCustom modelbase;
	private IModelCustom modelbasebroken;
	private IModelCustom modelpied;
	private IModelCustom modelbal;
	private IModelCustom modelbalnormal;
	private IModelCustom modelbalbroken;
	private IModelCustom modeltop;
	private IModelCustom modeltopbroken;

	private ResourceLocation resBaseT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-base.png");
	private ResourceLocation resBaseBrokenT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-basebroken.png");
	private ResourceLocation resPiedT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-piedballante.png");
	private ResourceLocation resBalT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-ballante.png");
	private ResourceLocation resBalBrokenT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-theballantebroken.png");
	private ResourceLocation resTopT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-topedge.png");
	private ResourceLocation resTopBrokenT = new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-topedgebroken.png");
		
	public RenderBlockHammerStand(){
		this.modelbase = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-base.obj"));
		this.modelbasebroken = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-basebroken.obj"));
		this.modelpied = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-piedballante.obj"));
		this.modelbal = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-ballante.obj"));
		this.modelbalnormal = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-ballante.obj"));
		this.modelbalbroken = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-theballantebroken.obj"));
		this.modeltop = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-topedge.obj"));
		this.modeltopbroken = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/blocks/models/hammerstand-topedgebroken.obj"));
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		
			GL11.glTranslatef((float)x+0.5F, (float)y+0.45F, (float)z+0.5F);
	        
			int meta  = tile.getBlockMetadata(); 
			
			if(meta==4)GL11.glRotatef(90, 0F, 1F, 0F);
			if(meta==5)GL11.glRotatef(0, 0F, 1F, 0F);
			if(meta==6)GL11.glRotatef(270, 0F, 1F, 0F);
			if(meta==7)GL11.glRotatef(180, 0F, 1F, 0F);
			
			if(meta>3){
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBaseBrokenT);
				this.modelbasebroken.renderAll();
			}else{
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBaseT);
				this.modelbase.renderAll();
			}
			
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resPiedT);
			this.modelpied.renderAll();
			
	
			if(meta>3){
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalT);
				this.modelbalnormal.renderAll();
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalBrokenT);
				this.modelbalbroken.renderAll();
			}else{
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalT);
				this.modelbal.renderAll();
			}
	
			if(meta>3){
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resTopBrokenT);
				this.modeltopbroken.renderAll();
			}else{
				FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resTopT);
				this.modeltop.renderAll();
			}
			
			if(meta>3){
				if(((TileHammerStand)tile).items[0]!=null){
			        GL11.glPushMatrix();
			        
						EntityItem itemEntity = new EntityItem(tile.getWorldObj(), 0, 0, 0, new ItemStack(ModItems.itemThorHammer));
						itemEntity.hoverStart = 0.0F;
						GL11.glScalef(1.5F, 1.5F, 1.5F);
						GL11.glTranslatef((float)0.0F, (float)0.25F, (float)0.0F);
				        GL11.glRotatef(90, 0F, 1F, 0F);
				        GL11.glRotatef(-5, 0F, 0F, 1F);
				        GL11.glRotatef(-10, 1F, 0F, 0F);
			
						//OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0.1f, 0.1f);
						RenderItem.renderInFrame = true;
						RenderManager.instance.renderEntityWithPosYaw(itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
						RenderItem.renderInFrame = false;
		
			        GL11.glPopMatrix();
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
		int meta = item.getItemDamage();
		
		if(meta>0){
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBaseBrokenT);
			this.modelbasebroken.renderAll();
		}else{
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBaseT);
			this.modelbase.renderAll();
		}
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resPiedT);
		this.modelpied.renderAll();
		

		if(meta>0){
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalT);
			this.modelbalnormal.renderAll();
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalBrokenT);
			this.modelbalbroken.renderAll();
		}else{
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resBalT);
			this.modelbal.renderAll();
		}

		if(meta>0){
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resTopBrokenT);
			this.modeltopbroken.renderAll();
		}else{
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(this.resTopT);
			this.modeltop.renderAll();
		}
		
		if(meta>0){
	        GL11.glPushMatrix();
	        
				IModelCustom modelth = AdvancedModelLoader.loadModel(new ResourceLocation(References.MODID+":textures/items/models/thorhammer.obj"));
				GL11.glScalef(0.9F, 0.9F, 0.9F);
				GL11.glTranslatef((float)0.0F, (float)0.75F, (float)0.0F);
		        GL11.glRotatef(0, 0F, 1F, 0F);
		        GL11.glRotatef(10, 0F, 0F, 1F);
		        GL11.glRotatef(-5, 1F, 0F, 0F);
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MODID+":textures/items/models/thorhammer.png"));
				modelth.renderAll();
				
	        GL11.glPopMatrix();
		}
	}

}
