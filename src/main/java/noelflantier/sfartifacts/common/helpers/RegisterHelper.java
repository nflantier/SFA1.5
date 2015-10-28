package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.References;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterHelper {
	public static void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, References.MODID + "_" + block.getUnlocalizedName().substring(5));
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> item)
	{
		GameRegistry.registerBlock(block, item, References.MODID + "_" + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerItem(Item item)
	{
		GameRegistry.registerItem(item, References.MODID + "_" + item.getUnlocalizedName().substring(5));
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> tile, String name)
	{
		GameRegistry.registerTileEntity(tile, References.MODID + "_" + name);
	}
}
