package noelflantier.sfartifacts.common.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;
import noelflantier.sfartifacts.common.entities.EntityShieldThrowCaptain;

public class ShieldHelper {

	public static void startThrowing(World w, EntityPlayer player, ItemStack shield) {
		if(shield.getItemDamage()==0){
			w.spawnEntityInWorld(new EntityShieldThrow(w, player, player.inventory.currentItem));
			return;
		}
		if(shield.getItemDamage()==1){
			w.spawnEntityInWorld(new EntityShieldThrowCaptain(w, player, player.inventory.currentItem));
			return;
		}
	}
}
