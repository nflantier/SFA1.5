package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.entities.EntityHulk;

@SideOnly(Side.CLIENT)
public class RenderEntityHulk extends RenderLiving{

    private ModelHulk hulkModel;
    private static final ResourceLocation hulkTexture = new ResourceLocation(References.MODID+":textures/entities/hulk.png");
   
    public RenderEntityHulk(ModelBase model, float shadow){
        super(model, shadow);
    }

    public void doRender(EntityHulk p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_){
        BossStatus.setBossStatus(p_76986_1_, true);
        super.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return hulkTexture;
	}

    protected void preRenderCallback(EntityHulk p_77041_1_, float p_77041_2_){
        GL11.glScalef(1F, 1F, 1F);
    }
}
