package noelflantier.sfartifacts.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import noelflantier.sfartifacts.common.blocks.FilledBucket;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;
import noelflantier.sfartifacts.common.items.ItemAsgardianBronzeIngot;
import noelflantier.sfartifacts.common.items.ItemAsgardianRing;
import noelflantier.sfartifacts.common.items.ItemAsgardianSteelIngot;
import noelflantier.sfartifacts.common.items.ItemAsgardite;
import noelflantier.sfartifacts.common.items.ItemAsgardiumPearl;
import noelflantier.sfartifacts.common.items.ItemBasicHammer;
import noelflantier.sfartifacts.common.items.ItemCable;
import noelflantier.sfartifacts.common.items.ItemEnergyModule;
import noelflantier.sfartifacts.common.items.ItemFluidModule;
import noelflantier.sfartifacts.common.items.ItemFusionCasing;
import noelflantier.sfartifacts.common.items.ItemFusionCoreFrame;
import noelflantier.sfartifacts.common.items.ItemHulkFlesh;
import noelflantier.sfartifacts.common.items.ItemLightningRod;
import noelflantier.sfartifacts.common.items.ItemMagnet;
import noelflantier.sfartifacts.common.items.ItemManual;
import noelflantier.sfartifacts.common.items.ItemMightyFeather;
import noelflantier.sfartifacts.common.items.ItemMightyHulkRing;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.items.ItemUberMightyFeather;
import noelflantier.sfartifacts.common.items.ItemVibraniumAlloy;
import noelflantier.sfartifacts.common.items.ItemVibraniumAlloySheet;
import noelflantier.sfartifacts.common.items.ItemVibraniumDust;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;

public class ModItems {

	public static Item itemBasicHammer;
	public static Item itemThorHammer;
	public static Item itemLightningRod;
	public static Item itemAsgardite;
	public static Item itemAsgardianBronzeIngot;
	public static Item itemAsgardianSteelIngot;
	public static Item itemFilledBucket;
	public static Item itemFluidModule;
	public static Item itemEnergyModule;
	public static Item itemManual;
	public static Item itemAsgardiumPearl;
	public static Item itemMightyFeather;
	public static Item itemUberMightyFeather;
	public static Item itemVibraniumDust;
	public static Item itemVibraniumAlloy;
	public static Item itemVibraniumAlloySheet;
	public static Item itemMold;
	public static Item itemVibraniumShield;
	public static Item itemMagnet;
	public static Item itemHulkFlesh;
	public static Item itemAsgardianRing;
	public static Item itemMightyHulkRing;
	public static Item itemFusionCasing;
	public static Item itemCable;
	public static Item itemFusionCoreFrame;
	
	public static void loadItems() {

    	itemAsgardite = new ItemAsgardite();
    	RegisterHelper.registerItem(itemAsgardite);
    	
    	itemFilledBucket = new FilledBucket(Block.getBlockFromItem(itemFilledBucket));
    	RegisterHelper.registerItem(itemFilledBucket);
    	
    	itemAsgardianSteelIngot = new ItemAsgardianSteelIngot();
    	RegisterHelper.registerItem(itemAsgardianSteelIngot);
    	
    	itemAsgardianBronzeIngot = new ItemAsgardianBronzeIngot();
    	RegisterHelper.registerItem(itemAsgardianBronzeIngot);
    	
    	itemBasicHammer = new ItemBasicHammer();
    	RegisterHelper.registerItem(itemBasicHammer);
    	
    	itemThorHammer = new ItemThorHammer();
    	RegisterHelper.registerItem(itemThorHammer);
    	
    	itemLightningRod = new ItemLightningRod();
    	RegisterHelper.registerItem(itemLightningRod);
		
    	
    	itemFluidModule = new ItemFluidModule();
    	RegisterHelper.registerItem(itemFluidModule);
    	
    	itemEnergyModule = new ItemEnergyModule();
    	RegisterHelper.registerItem(itemEnergyModule);
    	
    	itemManual = new ItemManual();
    	RegisterHelper.registerItem(itemManual);
    	
    	itemAsgardiumPearl = new ItemAsgardiumPearl();
    	RegisterHelper.registerItem(itemAsgardiumPearl);
    	
    	itemMightyFeather = new ItemMightyFeather();
    	RegisterHelper.registerItem(itemMightyFeather);
    	
    	itemUberMightyFeather = new ItemUberMightyFeather();
    	RegisterHelper.registerItem(itemUberMightyFeather);
    	
    	itemVibraniumDust = new ItemVibraniumDust();
    	RegisterHelper.registerItem(itemVibraniumDust);
    	
    	itemVibraniumAlloy = new ItemVibraniumAlloy();
    	RegisterHelper.registerItem(itemVibraniumAlloy);

    	itemVibraniumAlloySheet = new ItemVibraniumAlloySheet();
    	RegisterHelper.registerItem(itemVibraniumAlloySheet);
    	
    	itemMold = new ItemMold();
    	RegisterHelper.registerItem(itemMold);
    	
    	itemVibraniumShield = new ItemVibraniumShield();
    	RegisterHelper.registerItem(itemVibraniumShield);
    	
    	itemMagnet = new ItemMagnet();
    	RegisterHelper.registerItem(itemMagnet);
    	
    	itemHulkFlesh = new ItemHulkFlesh();
    	RegisterHelper.registerItem(itemHulkFlesh);
    	
    	itemAsgardianRing = new ItemAsgardianRing();
    	RegisterHelper.registerItem(itemAsgardianRing);
    	
    	itemMightyHulkRing = new ItemMightyHulkRing();
    	RegisterHelper.registerItem(itemMightyHulkRing);
    	
    	itemFusionCasing = new ItemFusionCasing();
    	RegisterHelper.registerItem(itemFusionCasing);
    	
    	itemCable = new ItemCable();
    	RegisterHelper.registerItem(itemCable);
    	
    	itemFusionCoreFrame = new ItemFusionCoreFrame();
    	RegisterHelper.registerItem(itemFusionCoreFrame);
    	
    	
	}
	
	
}
