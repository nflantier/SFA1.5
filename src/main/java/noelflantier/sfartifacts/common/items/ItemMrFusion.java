package noelflantier.sfartifacts.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
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
			((TileSFA)te).side = ForgeDirection.getOrientation(side).getOpposite().ordinal();
			world.markBlockForUpdate(x, y,z);
		}
		return flag;
	}

	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add("1.21 GigaWatts");
	}
	
}