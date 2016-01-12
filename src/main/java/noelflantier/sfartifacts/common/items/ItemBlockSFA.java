package noelflantier.sfartifacts.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class ItemBlockSFA extends ItemBlock{

	public String name;
	public boolean oriented = false;
	public ItemBlockSFA(Block block) {
		super(block);
	}
	public ItemBlockSFA(Block block, String name) {
		this(block);
		this.name = name;
	}
	public ItemBlockSFA(Block block, String name, boolean oriented) {
		this(block);
		this.name = name;
		this.oriented = oriented;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata){
		boolean flag = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		if(!oriented)
			return flag;
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileSFA){
			((TileSFA)te).side = ForgeDirection.getOrientation(side).getOpposite().ordinal();
			world.markBlockForUpdate(x, y,z);
		}
		return flag;
	}

}
