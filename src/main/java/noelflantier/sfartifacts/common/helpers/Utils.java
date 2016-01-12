package noelflantier.sfartifacts.common.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.IFluidBlock;
import noelflantier.sfartifacts.common.handlers.ModConfig;

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
	
	public static String getFileFromConfig(String fileName, boolean replaceIfExists) throws IOException {    
		if(replaceIfExists || !getConfigFile(fileName).exists()) {
			IOUtils.copy(Utils.class.getResourceAsStream("/assets/sfartifacts/config/" + fileName), new FileOutputStream(getConfigFile(fileName)));
		}            
		return IOUtils.toString(new FileReader(getConfigFile(fileName)));    
	}
	public static File getConfigFile(String name) {
		return new File(ModConfig.configDirectory, name);
	}
	
	public static boolean isInventoryHasItemClass(Class<? extends Item> cla, IInventory bi){
		int size = bi.getSizeInventory();
		for(int i = 0 ; i < size ; i++){
			if(bi.getStackInSlot(i)!=null && bi.getStackInSlot(i).getItem()!=null && bi.getStackInSlot(i).getItem().getClass()==cla)
				return true;
		}
		return false;
	}
	
	
	public static float isPlayerInFluid(EntityPlayer player, float speed){
		float f = 0;
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
	
	public static float isPlayerInFluid(EntityPlayer player, double decx, double decy, double decz, float speed){
		float f = 0;
        int i = MathHelper.floor_double(player.posX+decx);
        int j = player.worldObj.isRemote?MathHelper.floor_double(player.posY-1+decy):MathHelper.floor_double(player.posY+decy);
        int k = MathHelper.floor_double(player.posZ+decz);
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
