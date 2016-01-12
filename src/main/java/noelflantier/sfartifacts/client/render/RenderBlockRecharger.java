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
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class RenderBlockRecharger extends TileEntitySpecialRenderer implements IItemRenderer {
	
	
	private ResourceLocation objBase= new ResourceLocation(References.MODID+":textures/blocks/models/recharger-base.obj");
	private ResourceLocation textureBase= new ResourceLocation(References.MODID+":textures/blocks/models/recharger-base.png");
	private IModelCustom modelBase;
	
	private ResourceLocation objPilon= new ResourceLocation(References.MODID+":textures/blocks/models/recharger-pilon.obj");
	private ResourceLocation texturePilon= new ResourceLocation(References.MODID+":textures/blocks/models/recharger-pilon.png");
	private IModelCustom modelPilon;
	public RenderBlockRecharger(){
		this.modelBase = AdvancedModelLoader.loadModel(this.objBase);	
		this.modelPilon = AdvancedModelLoader.loadModel(this.objPilon);
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
	    	GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
			this.modelBase.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.texturePilon);
			this.modelPilon.renderAll();
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
			GL11.glTranslatef(0F, 0.5F, 0F);
			switch(side){
				case 1:
					GL11.glRotatef(180, 1F, 0F, 0F);
					break;
				case 2:
					GL11.glRotatef(90, 1F, 0F, 0F);
					break;
				case 3:
					GL11.glRotatef(-90, 1F, 0F, 0F);
					break;
				case 4:
					GL11.glRotatef(-90, 0F, 0F, 1F);
					break;
				case 5:
					GL11.glRotatef(90, 0F, 0F, 1F);
					break;
				case -1:
					break;
				default:
					break;
			}

		    GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
				this.modelBase.renderAll();
			    GL11.glPushMatrix();
					GL11.glTranslatef(0F, -0.1F, 0F);
					float speed = 2F;
					if(tile instanceof TileRecharger){
						if(((TileRecharger) tile).isRecharging)
							speed = 10F;
						if(!((TileRecharger) tile).wirelessRechargingEnable)
							speed = 0F;
					}
		        	GL11.glRotatef((SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/speed))*speed ,0F, 1F, 0F);
					Minecraft.getMinecraft().renderEngine.bindTexture(this.texturePilon);
					this.modelPilon.renderAll();
				GL11.glPopMatrix();
			GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
