package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class RenderBlockInductor extends TileEntitySpecialRenderer implements IItemRenderer {

	private Block bl;
	private ResourceLocation objBase= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-base.obj");
	private ResourceLocation textureBase= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-base.png");
	private IModelCustom modelBase;
	private ResourceLocation objStaff= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-staff.obj");
	private ResourceLocation textureStaff= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-staff0.png");
	private IModelCustom modelStaff;
	
	public RenderBlockInductor(){
		this.modelBase = AdvancedModelLoader.loadModel(this.objBase);
		this.modelStaff = AdvancedModelLoader.loadModel(this.objStaff);
	}
	public RenderBlockInductor(Block block) {
		this();
		this.bl = block;
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
		textureStaff= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-staff"+meta+".png");
	    GL11.glPushMatrix();
	    	GL11.glTranslatef(0.5F, 0.0F, 0.5F);
	    	GL11.glScalef(1.5F, 1.5F, 1.5F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
			this.modelBase.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureStaff);
			this.modelStaff.renderAll();
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
				case 1:
					GL11.glRotatef(180, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -1F, 0F);
					break;
				case 2:
					GL11.glRotatef(90, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -0.5F, -0.5F);
					break;
				case 3:
					GL11.glRotatef(-90, 1F, 0F, 0F);
					GL11.glTranslatef(0F, -0.5F, +0.5F);
					break;
				case 4:
					GL11.glRotatef(-90, 0F, 0F, 1F);
					GL11.glTranslatef(-0.5F, -0.5F, 0F);
					break;
				case 5:
					GL11.glRotatef(90, 0F, 0F, 1F);
					GL11.glTranslatef(+0.5F, -0.5F, 0F);
					break;
				case -1:
					break;
				default:
					break;
			}
			boolean send = true;
			boolean recieve = true;
			int meta = tile.getBlockMetadata();
			float vr = meta==0||meta==1?2F:5F;
			textureStaff= new ResourceLocation(References.MODID+":textures/blocks/models/inductor-staff"+meta+".png");
			if(tile instanceof TileInductor){
				send = ((TileInductor)tile).canWirelesslySend;
				recieve = ((TileInductor)tile).canWirelesslyRecieve;
			}

	        GL11.glPushMatrix();	        	
		        if(recieve){
					float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/vr))*vr;
			        GL11.glRotatef(-rot ,0F, 1F, 0F);
	        	}
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureStaff);
				this.modelStaff.renderAll();
			GL11.glPopMatrix();
	        GL11.glPushMatrix();
	        	if(send){
					float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/vr))*vr;
			        GL11.glRotatef(rot ,0F, 1F, 0F);
	        	}
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureBase);
				this.modelBase.renderAll();
			GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
