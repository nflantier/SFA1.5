package noelflantier.sfartifacts.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.ITileGlobalNBT;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class ItemRecharger extends ItemBlockSFA{

	public ItemRecharger(Block block) {
		super(block, "Recharger", true);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata){
		boolean flag = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		TileEntity te = world.getTileEntity(x, y, z);
	    if(te instanceof TileSFA && te instanceof ITileGlobalNBT) {
	    	((ITileGlobalNBT)te).readFromItem(stack);
			te.xCoord = x;
			te.yCoord = y;
			te.zCoord = z;
			((TileSFA) te).side=ForgeDirection.getOrientation(side).getOpposite().ordinal();
			world.markBlockForUpdate(x, y,z);
		}
		return flag;
	}
	
}
