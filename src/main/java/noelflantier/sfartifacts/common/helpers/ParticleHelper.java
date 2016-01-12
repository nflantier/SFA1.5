package noelflantier.sfartifacts.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noelflantier.sfartifacts.client.particles.ParticleAsgardite;
import noelflantier.sfartifacts.client.particles.ParticleLightning;
import noelflantier.sfartifacts.client.particles.ParticlePillarToWirelessPowerBlock;

@SideOnly(Side.CLIENT)
public class ParticleHelper {
	
	public enum Type{
		LIGHTNING,
		BOLT,
		ENERGYFLOW,
		ASGARDIANORES;
	}
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static World theWorld = mc.theWorld;
	private static Map<String, ResourceLocation> boundTextures = new HashMap();
	private static ResourceLocation defaultParticles;
	
	
	public static void spawnCustomParticle(Type pt, double x, double y, double z, Object... data) {
		spawnCustomParticle(pt, x, y, z, 64.0D, data);
	}
	
	public static void spawnAsgardianParticles(int x, int y, int z, Random random, float ch){
		if(random.nextFloat()>ch)
			return;
		float nx = (float)x+0.5F;
	    float ny = (float)y+0.5F;
	    float nz = (float)z+0.5F; 
		float rdx =	0;
	    float rdy = random.nextFloat()*2-1F;
		float rdz = 0;
	    if(rdy>0.5F || rdy<-0.5F){
	    	rdx = random.nextFloat()*2-1F;
	    	rdz = random.nextFloat()*2-1F;
	    }else{
	    	float tx = random.nextFloat();
	    	if(tx>=0.5F){
	    		rdx = random.nextFloat()*-1;
	    	}else{
	    		rdx = random.nextFloat();
	    	}
	    	
	    	float tz = random.nextFloat();
	    	if(tz>=0.5F){
	    		rdz = random.nextFloat()*-1;
	    	}else{
	    		rdz = random.nextFloat();
	    	}
	    }
	    ParticleHelper.spawnCustomParticle(ParticleHelper.Type.LIGHTNING, nx+rdx, ny+rdy, nz+rdz);
		ParticleHelper.spawnCustomParticle(ParticleHelper.Type.BOLT, nx+rdx, ny+rdy, nz+rdz);
	}
	
	public static void spawnCustomParticle(Type pt, double x, double y, double z, double viewRange,  Object... data){
		if ((mc != null) && (mc.renderViewEntity != null) && (mc.effectRenderer != null)) {

            double d6 = mc.renderViewEntity.posX - x;
            double d7 = mc.renderViewEntity.posY - y;
            double d8 = mc.renderViewEntity.posZ - z;
            if(d6 * d6 + d7 * d7 + d8 * d8 > viewRange*viewRange)return;
            
			int particlesetting = mc.gameSettings.particleSetting;
			if (particlesetting >= 2){
				return;
			}else if(particlesetting == 1){
				if(mc.theWorld.rand.nextFloat()<0.5)
					return;
			}
			
			switch(pt){
				case LIGHTNING :
					spawnit(new ParticleLightning(mc.theWorld, x, y, z, 0.6f, 1f, 1f ));
					break;
				case BOLT :
					spawnit(new ParticleAsgardite(mc.theWorld, x, y, z, 0, 0.1, 0, 20, 6, -2, 0.6, 1, 1, 1, 0));
					break;
				case ENERGYFLOW :
					spawnit(new ParticlePillarToWirelessPowerBlock(mc.theWorld, (double)data[0], (double)data[1], (double)data[2], (double)x, (double)y, (double)z, (int)data[3], 70));
					break;
				default : break;
			}
		}
	}
	
	private static void spawnit(EntityFX particle){
		mc.effectRenderer.addEffect(particle);
	}
	
	public static void bindDefaultParticlesTextures(){
		if (defaultParticles == null)
		{
			try
			{
				defaultParticles = (ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null, "particleTextures", "field_110737_b");
			}
			catch (Exception e) {}
		}
		if (defaultParticles != null) Minecraft.getMinecraft().renderEngine.bindTexture(defaultParticles);
	}
}
