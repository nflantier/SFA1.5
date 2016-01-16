package noelflantier.sfartifacts.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.tiles.ITileGlobalNBT;

public abstract class ABlockNBT extends BlockSFAContainer {

	public ABlockNBT(Material material, String name) {
		super(material, name);
	}

	public abstract boolean dropWithNBT(int meta);

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean doHarvest) {
		if(world.isRemote || !dropWithNBT(world.getBlockMetadata(x, y, z)))
			return super.removedByPlayer(world, player, x, y, z, false);
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof ITileGlobalNBT){
		    int met = damageDropped(world.getBlockMetadata(x, y, z));
		    ItemStack itemStack = new ItemStack(this, 1, met);
		    if(itemStack.stackTagCompound == null) {
		    	itemStack.stackTagCompound = new NBTTagCompound();
		    }
		    ((ITileGlobalNBT) te).writeToItem(itemStack);
		    dropBlockAsItem(world, x, y, z, itemStack);
		}
		return super.removedByPlayer(world, player, x, y, z, false);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return (dropWithNBT(metadata))?new ArrayList<ItemStack>():super.getDrops(world, x, y, z, metadata, fortune);
	}
}
