package noelflantier.sfartifacts.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.handlers.ModItems;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityHammerInvoking extends Render {

    public RenderEntityHammerInvoking()
    {
    }
    
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		EntityHammerInvoking te = (EntityHammerInvoking)entity;
		EntityItem itemEntity = new EntityItem(entity.worldObj, 0, 0, 0, new ItemStack(ModItems.itemThorHammer));
		itemEntity.hoverStart = 0.0F;
		
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glScalef(2F, 2F, 2F);
		
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
