package noelflantier.sfartifacts.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerTeleport  extends Container {

	public ContainerTeleport(InventoryPlayer inventory) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
