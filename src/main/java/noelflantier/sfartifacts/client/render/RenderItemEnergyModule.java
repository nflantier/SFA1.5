package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public class RenderItemEnergyModule  implements IItemRenderer{

	private ResourceLocation objFrame= new ResourceLocation(References.MODID+":textures/items/models/energymoduleframe.obj");
	private ResourceLocation textureFrame= new ResourceLocation(References.MODID+":textures/items/models/energymoduleframe.png");
	private IModelCustom modelFrame;

	private ResourceLocation objCube= new ResourceLocation(References.MODID+":textures/items/models/energymodulecube.obj");
	private ResourceLocation textureCube= new ResourceLocation(References.MODID+":textures/items/models/energymodulecube.png");
	private IModelCustom modelCube;
	
	public RenderItemEnergyModule(){
		this.modelFrame = AdvancedModelLoader.loadModel(this.objFrame);
		this.modelCube = AdvancedModelLoader.loadModel(this.objCube);
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
		
	        GL11.glScalef(1.5F, 1.5F, 1.5F);
	        GL11.glRotatef(20F,0F, 1F, 0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureFrame);
			this.modelFrame.renderAll();
			
	        GL11.glScalef(2F, 2F, 2F);

			//float rot = (SFArtifacts.instance.myProxy.sfaEvents.getClientTick()%(360/7))*7F;
			//GL11.glRotatef(rot,0F,1F,0F);
	        //GL11.glRotatef((SFArtifacts.instance.myProxy.sfaFMLEvents.getClientTick()%360)*4F,0F, 1F, 0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureCube);
			this.modelCube.renderAll();
			
		GL11.glPopMatrix();

	}

}
