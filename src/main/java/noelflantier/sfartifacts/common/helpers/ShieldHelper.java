package noelflantier.sfartifacts.common.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;

public class ShieldHelper {

	public static void startThrowing(World w, EntityPlayer player) {
		w.spawnEntityInWorld(new EntityShieldThrow(w, player, player.inventory.currentItem));
	}
}
