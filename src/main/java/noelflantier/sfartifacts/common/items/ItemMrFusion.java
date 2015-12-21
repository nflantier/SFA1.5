package noelflantier.sfartifacts.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class ItemMrFusion extends ItemBlockSFA{
	
	public ItemMrFusion(Block block) {
		super(block, "Mr Fusion");
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata){
		boolean flag = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileSFA){
			((TileSFA)te).side = side;
			world.markBlockForUpdate(x, y,z);
		}
		return flag;
	}
}
