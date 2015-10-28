package noelflantier.sfartifacts.common.items.baseclasses;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.entities.EntityItemParticle;
import noelflantier.sfartifacts.common.items.ItemSFA;

public class ItemParticle extends ItemSFA{
	
	public ItemParticle() {
		super();
	}

	public ItemParticle(String name) {
		super(name);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return false;
    }
    
    @Override
	public boolean hasCustomEntity(ItemStack stack){
    	return true;
	}
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack stack)
    {
    	EntityItem eqi = new EntityItemParticle(world, location, stack);
		eqi.motionX = location.motionX;
		eqi.motionY = location.motionY;
		eqi.motionZ = location.motionZ;
		eqi.delayBeforeCanPickup = ( (EntityItem) location ).delayBeforeCanPickup;
        return eqi;
    }
}
