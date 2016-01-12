package noelflantier.sfartifacts.client.particles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

@SideOnly(Side.CLIENT)
public class ParticleAsgardite extends EntityFX{

	public ResourceLocation rl  = new ResourceLocation(References.MODID+":textures/particles/pt.png");
	public double gravity,friction,r_e,g_e,b_e,opacity_e;
	public int sens;
	
	public ParticleAsgardite(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	
	public ParticleAsgardite(World w, double xp, double yp, double zp, double xs, double ys, double zs, int scale, int maxAge) {
		super(w, xp, yp, zp, xs, ys, zs);
		this.motionX =xs;
		this.motionY =ys;
		this.motionZ =zs;
		this.particleMaxAge=maxAge;
		this.particleScale=scale;
		this.friction=0.98;
		this.gravity=0;
		this.r_e=1;
		this.g_e=1;
		this.b_e=1;
		this.opacity_e=1;
		
		this.noClip = true;
		this.particleAlpha = 1;
	}

	public ParticleAsgardite(World w, double xp, double yp, double zp, double xs, double ys, double zs, int scale, int maxAge ,double gravit, double Ra,double Ga,double Ba,double opacita,double frictio)
	{
		super(w, xp, yp, zp, xs, ys, zs);
		
		float random = (float) (Math.random()*10);
		if(random>7.5F)this.sens = 0;
		else if(random>5F)this.sens = 8;
		else if(random>2.5F)this.sens =16;
		else this.sens = 24;
		
		this.motionX = xs;
		this.motionY = ys;
		this.motionZ = zs;
        this.friction=frictio;
        this.particleMaxAge=maxAge;
        this.particleScale=scale;
        this.gravity=gravit*0.001;
        this.r_e=Ra;
        this.g_e=Ga;
        this.b_e=Ba;
        this.opacity_e=opacita;
		this.renderDistanceWeight = 250000;

		this.noClip = true;
		this.particleAlpha = 1;
	}
	
	@Override
	public void renderParticle(Tessellator tess, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		tess.draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.rl);
        tess.startDrawingQuads();
		tess.setColorRGBA_F((float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
    	tess.setBrightness(240);
    			
		float textX = (float)this.particleTextureIndexX/8.0F;
		float textXP = textX + 0.125F;
		float textY = (float)this.particleTextureIndexY/8.0F;
		float textYP = textY + 0.125F;
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
        tess.addVertexWithUV((double)(x-par3*PScale-par6*PScale), (double)(y-par4*PScale), (double)(z-par5*PScale-par7*PScale), textX, textY);
        tess.addVertexWithUV((double)(x-par3*PScale+par6*PScale), (double)(y+par4*PScale), (double)(z-par5*PScale+par7*PScale), textXP, textY);
        tess.addVertexWithUV((double)(x+par3*PScale+par6*PScale), (double)(y+par4*PScale), (double)(z+par5*PScale+par7*PScale), textXP, textYP);
        tess.addVertexWithUV((double)(x+par3*PScale-par6*PScale), (double)(y-par4*PScale), (double)(z+par5*PScale-par7*PScale), textX, textYP);
        
        tess.draw();
		ParticleHelper.bindDefaultParticlesTextures();
		tess.startDrawingQuads();
		tess.setColorRGBA_F(1F,1F,1F,1F);
		tess.setBrightness(0);
	}

	@Override
	public void onUpdate()
	{
		if(this.particleAge>this.particleMaxAge)this.setDead();
		if(Minecraft.getMinecraft().gameSettings.particleSetting==2)this.setDead();
		
		if(worldObj.isRemote)this.motionHandeler();
		
		//this.particleScale-=(float)particleMaxAge/10.0;
		
		if(this.isDead){
		}
		this.particleAge++;
		this.setParticleTextureIndex(this.particleAge+this.sens);
	}
	
	/*@Override
    public int getFXLayer() {
        return -3;
    }*/
	
	public void motionHandeler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionX *= this.friction;
		this.motionY *= this.friction;
		this.motionZ *= this.friction;
		
		
		this.motionY +=this.gravity;
		//this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
