package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import noelflantier.sfartifacts.client.particles.ParticleLightning;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class RenderItemAsgardite implements IItemRenderer {

	private Minecraft mc;
	public RenderItemAsgardite(){
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == type.INVENTORY)return false;
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		GL11.glPushMatrix();
		
			IIcon iicon = item.getIconIndex();
			this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
			TextureUtil.func_147945_b();
		
		GL11.glPopMatrix();
	}

}
