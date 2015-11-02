package noelflantier.sfartifacts.common.helpers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;

public enum InjectorRecipe {
	MIGHTYHULKRING(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemAsgardianRing,1));
				add(new ItemStack(ModItems.itemHulkFlesh,4));
			}}
			,
			ModItems.itemMightyHulkRing
			,
			5000
			,
			5000
		),
	VIBRANIUMALLOY(
		new ArrayList(){{
			add(new ItemStack(Items.iron_ingot,1));
			add(new ItemStack(ModItems.itemVibraniumDust,1));
		}}
		,
		ModItems.itemVibraniumAlloy
		,
		1000
		,
		150
	),
	GLASS(
		new ArrayList(){{
			add(new ItemStack(Blocks.glass,1));
		}}
		,

		Item.getItemFromBlock(ModBlocks.blockAsgardianGlass)
		,
		100
		,
		100
	),
	STEEL(
		new ArrayList(){{
			add(new ItemStack(Items.iron_ingot,1));
		}}
		,
		ModItems.itemAsgardianSteelIngot
		,
		500
		,
		100
	),
	BRONZE(
		new ArrayList(){{
			add(new ItemStack(Items.gold_ingot,1));
		}}
		,
		ModItems.itemAsgardianBronzeIngot
		,
		500
		,
		100
	),
	PEARL(
		new ArrayList(){{
			add(new ItemStack(Items.ender_pearl,1));
			add(new ItemStack(Items.diamond,1));
		}}
		,
		ModItems.itemAsgardiumPearl
		,
		10000
		,
		500
	);
	
	public List<ItemStack> recipe;
	public Item result;
	public int fluidAmount = 500;
	public int energyAmount = 100;
	
	private InjectorRecipe(List<ItemStack>recipe, Item item, int fa, int ea){
		this.recipe = recipe;
		this.result = item;
		this.fluidAmount = fa;
		this.energyAmount = ea;
	}
}
