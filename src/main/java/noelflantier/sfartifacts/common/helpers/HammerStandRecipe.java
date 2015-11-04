package noelflantier.sfartifacts.common.helpers;

import java.util.ArrayList;
import java.util.Hashtable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.hammerstandrecipe.CapacityRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.ConfigByHandRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.EnchantmentRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.LightningRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.MagnetRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.RecipeOnHammerStand;
import noelflantier.sfartifacts.common.hammerstandrecipe.TeleportRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.ThrowToMoveRecipe;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;

public enum HammerStandRecipe {
	TELEPORT(
		new TeleportRecipe(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemAsgardiumPearl,8,0));
				add(new ItemStack(ModItems.itemEnergyModule,1,0));
			}}
		),
		"Teleport"
	),
	MAGNET(
		new MagnetRecipe(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemMagnet,1,0));
			}}
		),
		"Magnet"
	),
	CAPACITY(
		new CapacityRecipe(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemEnergyModule,1,0));
			}}
		),
		"Capacity",
		"Unlimited forging"
	),
	LIGHTNING(
		new LightningRecipe(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemLightningRod,1,3));
			}}
		),
		"Lightning"
	),
	ENCHANTMENT(
		new EnchantmentRecipe(
			new ArrayList(){{
				add(new ItemStack(Items.enchanted_book,1,0));
			}}
		),
		"Enchanted Books"	
	),
	CONFIGBYHAND(
		new ConfigByHandRecipe(
			new ArrayList(){{
				add(new ItemStack(Item.getItemFromBlock(ModBlocks.blockControlPanel),1,0));
			}}
		)	,
		"Configurate By Hand"	
	),
	THROWTOMOVE(
		new ThrowToMoveRecipe(
			new ArrayList(){{
				add(new ItemStack(ModItems.itemUberMightyFeather,1,0));
			}}
		),
		"Moving"			
	);
	
	public RecipeOnHammerStand recipe;
	public String sName;
	public String desc = "";
	
	private HammerStandRecipe(RecipeOnHammerStand rohs, String sname){
		this.recipe = rohs;
		this.sName = sname;
	}	
	
	private HammerStandRecipe(RecipeOnHammerStand rohs, String sname, String desc){
		this.recipe = rohs;
		this.sName = sname;
		this.desc = desc;
	}
}
