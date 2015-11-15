package noelflantier.sfartifacts.common.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;

public class ContainerControlPanel extends Container {

	private TileControlPannel tile;
	
	public ContainerControlPanel(InventoryPlayer inventory, TileControlPannel tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
}
