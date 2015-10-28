package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ISFAInventory {

	boolean isInventoryDroppedOnBreaking();
	ItemStack[] getInventory();
}
