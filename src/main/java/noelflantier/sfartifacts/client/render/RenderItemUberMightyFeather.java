package noelflantier.sfartifacts.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModItems;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItemUberMightyFeather implements IItemRenderer{

	private ResourceLocation textureUber= new ResourceLocation(References.MODID+":textures/items/uber_mighty_feather32.png");
	public RenderItemUberMightyFeather(){
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
		if(type==type.INVENTORY){
			GL11.glPushMatrix();
				GL11.glRotatef(225, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(1.6F, 1.6F, 1.6F);
				GL11.glTranslatef(-0.5F,-0.5F,0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				//GL11.glDepthMask(false);
		        GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				IIcon iicon = item.getIconIndex();
				Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
				Tessellator tessellator = Tessellator.instance;
				float f = iicon.getMinU();
				float f1 = iicon.getMaxU();
				float f2 = iicon.getMinV();
				float f3 = iicon.getMaxV();
				ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
				//GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
				TextureUtil.func_147945_b();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_BLEND);
				//GL11.glDepthMask(true);
		        GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}else {
			GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				if(type==type.ENTITY)
					GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
				//GL11.glDepthMask(false);
		        GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				
				switch(type){
					case ENTITY:
						GL11.glTranslatef(-1.5F,-0.7F,-0.1F);
						GL11.glScalef(3F, 3F, 3F);
						break;
					case EQUIPPED_FIRST_PERSON:
						GL11.glTranslatef(-0.0F,0.4F,0.0F);
						GL11.glRotatef(225, 0.0F, 1.0F, 0.0F);
						GL11.glScalef(2F, 2F, 2F);
						break;
					case EQUIPPED:
						GL11.glScalef(3F, 3F, 3F);
						GL11.glTranslatef(-0.2F,-0.0F,-0.1F);
						GL11.glRotatef(-40, 0.0F, 1.0F, 0.0F);
						break;
					default:
						break;
				}
				Minecraft.getMinecraft().renderEngine.bindTexture(this.textureUber);
				ItemRenderer.renderItemIn2D(Tessellator.instance, 1, 0, 0, 1, 32, 32, 0.03125F);
				TextureUtil.func_147945_b();
				
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_BLEND);
				//GL11.glDepthMask(true);
		        GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glPopMatrix();
		}
	}
}
