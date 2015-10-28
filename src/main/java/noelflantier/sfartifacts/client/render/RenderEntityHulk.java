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

    private final ModelHulk hulkModel;
    private static final ResourceLocation hulkTexture = new ResourceLocation(References.MODID+":textures/entities/hulk.png");
    
	public RenderEntityHulk() {
        super(new ModelHulk(), 1.0F);
        this.hulkModel = (ModelHulk)this.mainModel;
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

    protected void rotateCorpse(EntityHulk p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);

        if ((double)p_77043_1_.limbSwingAmount >= 0.01D)
        {
            float f3 = 13.0F;
            float f4 = p_77043_1_.limbSwing - p_77043_1_.limbSwingAmount * (1.0F - p_77043_4_) + 6.0F;
            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
        }
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.rotateCorpse((EntityHulk)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    protected int shouldRenderPass(EntityHulk p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return -1;
    }

    protected int inheritRenderPass(EntityHulk p_77035_1_, int p_77035_2_, float p_77035_3_)
    {
        return -1;
    }

    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityHulk)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((EntityHulk)p_77041_1_, p_77041_2_);
    }

    protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return this.shouldRenderPass((EntityHulk)p_77032_1_, p_77032_2_, p_77032_3_);
    }

    protected int inheritRenderPass(EntityLivingBase p_77035_1_, int p_77035_2_, float p_77035_3_)
    {
        return this.inheritRenderPass((EntityHulk)p_77035_1_, p_77035_2_, p_77035_3_);
    }
    
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityHulk)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityHulk)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
