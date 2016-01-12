package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;

public class ContainerControlPanel extends Container {

	private TileControlPannel tile;
	
	public ContainerControlPanel(InventoryPlayer inventory, TileControlPannel tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
