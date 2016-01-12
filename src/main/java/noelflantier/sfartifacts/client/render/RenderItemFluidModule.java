package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;

public class RenderItemFluidModule  implements IItemRenderer{

	private ResourceLocation objFrame= new ResourceLocation(References.MODID+":textures/items/models/fluidmoduleframe.obj");
	private ResourceLocation textureFrame= new ResourceLocation(References.MODID+":textures/items/models/fluidmoduleframe.png");
	private IModelCustom modelFrame;

	private ResourceLocation objGlass= new ResourceLocation(References.MODID+":textures/items/models/fluidmoduleglass.obj");
	private ResourceLocation textureGlass= new ResourceLocation(References.MODID+":textures/items/models/fluidmoduleglass.png");
	private IModelCustom modelGlass;
	
	public RenderItemFluidModule(){
		this.modelFrame = AdvancedModelLoader.loadModel(this.objFrame);
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
		
		Minecraft.getMinecraft().renderEngine.bindTexture(this.textureFrame);
		this.modelFrame.renderAll();

        GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, 0.5F);
			Minecraft.getMinecraft().renderEngine.bindTexture(this.textureGlass);
			this.modelGlass.renderAll();
			
			
			/*RenderBlocks renderer = (RenderBlocks)data[0]; 
			renderer.setOverrideBlockTexture(item.getIconIndex());
			switch(type){
				case EQUIPPED_FIRST_PERSON:
			        GL11.glTranslatef(0.2F, 0.7F, 0.3F);
					break;
				default:
					break;
			}
		    renderer.renderBlockAsItem(Blocks.stone, 0, 1);
		    renderer.clearOverrideBlockTexture();*/
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
	    GL11.glPopMatrix();

	}

}
