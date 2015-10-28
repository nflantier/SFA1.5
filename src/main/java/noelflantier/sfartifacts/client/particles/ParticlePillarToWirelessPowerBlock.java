package noelflantier.sfartifacts.client.particles;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

@SideOnly(Side.CLIENT)
public class ParticlePillarToWirelessPowerBlock extends EntityFX{

	public ResourceLocation rl  = new ResourceLocation(References.MODID+":textures/particles/pt.png");
	public double xO, yO, zO, xT, yT, zT; 
	public float opa = 1;
	public float opapas = -0.01F;
	public float sca = 0.010F;
	public float scapas = -0.0001F;
	
	public ParticlePillarToWirelessPowerBlock(World w, double xp,double yp, double zp) {
		super(w, xp, yp, zp);
	}
	
	public ParticlePillarToWirelessPowerBlock(World w, double xp, double yp, double zp, double xtarget, double ytarget, double ztarget, int scale, int maxAge) {
		super(w, xp, yp, zp, 0.0D, 0.0D, 0.0D);
		this.motionX =0.0D;
		this.motionY =0.0D;
		this.motionZ =0.0D;
		this.xO = xp;
		this.yO = yp;
		this.zO = zp;
		this.xT = xtarget;
		this.yT = ytarget;
		this.zT = ztarget;
		this.particleMaxAge=maxAge;
		this.particleScale=MathHelper.clamp_int(scale, 20, 100);
		this.noClip = true;
		this.particleAlpha = 1;
		this.opapas = (float) (Math.random()/30);
		this.scapas = (float) (Math.random()*0.5/1500);
	}

	@Override
	public void renderParticle(Tessellator tess, float par2, float par3, float par4, float par5, float par6, float par7){
		tess.draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.rl);
        tess.startDrawingQuads();
		tess.setColorRGBA_F((float)1, (float)1, (float)1, this.opa);
    	tess.setBrightness(240);
    	
    	this.opa += this.opapas;
    	if(this.opa<0.5 || this.opa>1)this.opapas *= -1;
    	
		float textX = (float)this.particleTextureIndexX/8.0F;
		float textXP = textX + 0.25F;
		float textY = (float)this.particleTextureIndexY/8.0F;
		float textYP = textY + 0.25F;
    	float PScale = this.sca*this.particleScale;
    	
    	this.sca += this.scapas;
    	if(this.sca<0.001 || this.sca>0.01)this.scapas *= -1;
    	
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
		if(worldObj.isRemote)this.motionHandeler();
		this.particleAge++;
		this.setParticleTextureIndex(64);
	}
	
	public void motionHandeler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		this.motionX = (this.xT-this.xO)/50;
		this.motionY = (this.yT-this.yO)/50;
		this.motionZ = (this.zT-this.zO)/50;
		
		double curhyp = Math.sqrt( (this.posX-this.xT)*(this.posX-this.xT) + (this.posY-this.yT)*(this.posY-this.yT) + (this.posZ-this.zT)*(this.posZ-this.zT) );
		
		if(curhyp<0.1)
			this.setDead();
		
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
