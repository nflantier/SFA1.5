package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterHelper {
		
	public static void registerBlock(Block block)
	{
		if(ModConfig.useOldRegistration)
			GameRegistry.registerBlock(block, References.MODID + "_" + block.getUnlocalizedName().substring(5));
		else
			GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> item)
	{
		if(ModConfig.useOldRegistration)
			GameRegistry.registerBlock(block, item, References.MODID + "_" + block.getUnlocalizedName().substring(5));
		else
			GameRegistry.registerBlock(block, item, block.getUnlocalizedName().substring(5));
	}
	
	public static void registerItem(Item item)
	{
		if(ModConfig.useOldRegistration)
			GameRegistry.registerItem(item, References.MODID + "_" + item.getUnlocalizedName().substring(5));
		else
			GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> tile, String name)
	{
		GameRegistry.registerTileEntity(tile, References.MODID + "_" + name);
	}
}
