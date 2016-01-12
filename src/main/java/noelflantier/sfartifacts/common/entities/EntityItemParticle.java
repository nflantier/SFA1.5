package noelflantier.sfartifacts.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class EntityItemParticle extends EntityItem  {

	public EntityItemParticle(World w) {
		super(w);
	}

	public EntityItemParticle(World w, Entity location, ItemStack stack) {
		super(w, location.posX, location.posY, location.posZ, stack);
		this.fireResistance=10000;
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();

		if(this.rand.nextFloat()>0.95 && this.worldObj.isRemote && ModConfig.isItemsEmitParticles)
			ParticleHelper.spawnCustomParticle(ParticleHelper.Type.LIGHTNING, this.posX, this.posY, this.posZ);
	}
}
