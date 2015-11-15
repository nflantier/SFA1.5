package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;

public class ContainerHammerStandNonInvoked  extends Container {

	private TileHammerStand tile;
	public ContainerHammerStandNonInvoked(InventoryPlayer inventory,TileHammerStand tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

}
