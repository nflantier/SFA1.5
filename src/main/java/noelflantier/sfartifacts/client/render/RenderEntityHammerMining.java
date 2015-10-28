package noelflantier.sfartifacts.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.handlers.ModItems;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityHammerMining extends Render{
	

    public RenderEntityHammerMining()
    { }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		doRender((EntityHammerMinning) entity, x, y, z, f2);
		GL11.glPopMatrix();
	}
	
	public void doRender(EntityHammerMinning entity, double x, double y, double z, float f2) {
		
		EntityItem itemEntity = new EntityItem(entity.worldObj, 0, 0, 0, new ItemStack(ModItems.itemThorHammer));
		itemEntity.hoverStart = 0.0F;
		
        GL11.glPushMatrix();
		GL11.glRotatef(entity.rotationYaw,0F, 1F,0F);
		GL11.glRotatef(180,0F, 0F,0F);
		GL11.glRotatef(-entity.rotationPitch+90,1F, 0F,0F);
		GL11.glScalef(1.5F, 1.5F,1.5F);
		
		RenderItem.renderInFrame = true;
		RenderManager.instance.renderEntityWithPosYaw(itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		RenderItem.renderInFrame = false;
		
        GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(References.MODID+":thor_hammer");
	}

}
