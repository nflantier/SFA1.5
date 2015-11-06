package noelflantier.sfartifacts.common.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.IFluidBlock;

public class Utils {
	
	public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }
	
	public static float isPlayerInFluid(EntityPlayer player, float speed){
		float f = 1;
        int i = MathHelper.floor_double(player.posX);
        int j = player.worldObj.isRemote?MathHelper.floor_double(player.posY-1):MathHelper.floor_double(player.posY);
        int k = MathHelper.floor_double(player.posZ);
        Block block = player.worldObj.getBlock(i, j, k);
        Material m= block.getMaterial();
        boolean lavab = player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
        boolean waterb = player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
        if (block instanceof IFluidBlock || m==Material.water || m==Material.lava || lavab || waterb){
        	f = speed;
        	if(m==Material.lava || lavab)
        		f *= 1.6;
        	return f;
        }
        return f;
	}
}
