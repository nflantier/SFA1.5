package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;


public class RenderItemThorHammer implements IItemRenderer{

	private ResourceLocation objThorHammer = new ResourceLocation(References.MODID+":textures/items/models/thorhammer.obj");
	private ResourceLocation textThorHammer = new ResourceLocation(References.MODID+":textures/items/models/thorhammer.png");
	ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private IModelCustom modelThorHammer;
	
	public RenderItemThorHammer(){
		this.modelThorHammer = AdvancedModelLoader.loadModel(this.objThorHammer);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case ENTITY:
			return true;
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {	

		GL11.glPushMatrix();
			switch(type){
			case ENTITY:
				GL11.glScalef(1F, 1F, 1F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textThorHammer);
				this.modelThorHammer.renderAll();
				break;
			case EQUIPPED_FIRST_PERSON:
				if(ItemNBTHelper.getBoolean(stack, "IsMoving", false)){
					GL11.glRotatef(50,0F, 1F,0F);
					GL11.glRotatef(190,1F, 0F,0F);
					GL11.glRotatef(50,0F, 0F,1F);
					GL11.glTranslatef(0F, -1.5F, -1F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textThorHammer);
					this.modelThorHammer.renderAll();
				}else if(!ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
					GL11.glRotatef(50,0F, 1F,0F);
					GL11.glRotatef(180,1F, 0F,0F);
					GL11.glTranslatef(0F, -1.5F, -1F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textThorHammer);
					this.modelThorHammer.renderAll();
				}
				break;
			case INVENTORY:
				GL11.glScalef(1F, 1F, 1F);
				//GL11.glTranslatef(0F, -0.3F, 0F);
				GL11.glTranslatef(0F, 0.4F, 0F);
				GL11.glRotatef(180,1F, 0F,0F);
				//GL11.glRotatef(-10,0F, 1F,0F);
				GL11.glRotatef(0,0F, 0F,1F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textThorHammer);
				this.modelThorHammer.renderAll();
				break;
			case EQUIPPED:
				if(!ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
					GL11.glRotatef(130,0F, 1F,0F);
					GL11.glRotatef(180,1F, 0F,0F);
					GL11.glRotatef(60,0F, 0F,1F);
					GL11.glTranslatef(-1F, -0.3F,0F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textThorHammer);
					this.modelThorHammer.renderAll();
				}
				break;
			default:
			}

			if(stack.hasEffect(0)){				
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				Minecraft.getMinecraft().renderEngine.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				float f7 = 0.76F;
				GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float f8 = 0.125F;
				GL11.glScalef(f8, f8, f8);
				float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(f9, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				this.modelThorHammer.renderAll();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(f8, f8, f8);
				f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-f9, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				this.modelThorHammer.renderAll();
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
				TextureUtil.func_147945_b();
			}
			
		GL11.glPopMatrix();
		
	}
}
