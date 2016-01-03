package noelflantier.sfartifacts.common.handlers;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianBronze;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public class ModRecipes {
	
	public static void loadRecipes() {
		loadOreDictionnaryRecipe();
		
		//GENERAL
		
		//THOR
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardianBronze,1,0),  "BBB", "BBB", "BBB", 'B', ModItems.itemAsgardianBronzeIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardianSteel,1,0),  "SSS", "SSS", "SSS", 'S', ModItems.itemAsgardianSteelIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardite,1,0),  "AAA", "AAA", "AAA", 'A', ModItems.itemAsgardite));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAsgardite,9,0), ModBlocks.blockAsgardite));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemFluidModule,1,0),  "GGG", "GTG", "GPG", 'P', Blocks.piston, 'G', Blocks.glass, 'T', Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemEnergyModule,1,0),  "BBB", "BRB", "BDB", 'A', ModItems.itemAsgardite, 'B', ModItems.itemAsgardite, 'R', Blocks.redstone_block, 'D', Items.diamond));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMagnet,1,0),  "B B", "S S", "SDS", 'S',ModItems.itemAsgardianSteelIngot,'B',Items.iron_ingot,'D',ModItems.itemEnergyModule));
		
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockControlPanel,1,0), ModItems.itemEnergyModule, Blocks.redstone_torch, Items.repeater));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemBasicHammer,1,0),  "BBB", "BBB", " S ", 'B', ModBlocks.blockAsgardite, 'S', Items.stick));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockHammerStand,1,0),  "III", "BSB", "SSS", 'B', ModBlocks.blockAsgardianBronze, 'S', ModBlocks.blockAsgardianSteel, 'I', ModItems.itemAsgardianSteelIngot));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemVibraniumShield,1,1),  "RRR", "BTB", "RRR", 'B', new ItemStack(Items.dye,1,4), 'R', new ItemStack(Items.dye,1,1), 'T', new ItemStack(ModItems.itemVibraniumShield,1,0)));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,0),  "D", "S", "S", 'D', Blocks.diamond_block, 'S', ModBlocks.blockAsgardianSteel));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,1),  " D ", " S ", "SSS", 'D', Blocks.diamond_block, 'S', ModBlocks.blockAsgardianSteel));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,2),  " D ", "SSS", "SSS", 'D', Blocks.diamond_block, 'S', ModBlocks.blockAsgardianSteel));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,3),  " DS", "SSS", "SSS", 'D', Blocks.diamond_block, 'S', ModBlocks.blockAsgardianSteel));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemManual,1,0), ModItems.itemAsgardite, Items.book));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemUberMightyFeather,1,0),  "FFF", "FMF", "FFF", 'F', ModItems.itemMightyFeather, 'M', ModItems.itemEnergyModule));
		
    	for(PillarMaterials pm :PillarMaterials.values()){
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockLiquefier,1,pm.ordinal()),  "MPM", "TET", "MPM", 'E', ModItems.itemEnergyModule, 'M',pm.item , 'T',ModItems.itemFluidModule, 'P', Blocks.piston));
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockInjector,1,pm.ordinal()),  "MTM", "EEE", "MPM", 'E', ModItems.itemEnergyModule, 'M',pm.item , 'T',ModItems.itemFluidModule, 'P',Blocks.piston));
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockLightningRodStand,1,pm.ordinal()),  "M M", "M M", "MEM", 'E', ModItems.itemEnergyModule, 'M',pm.item ));
    	}
    	
    	
    	//CAPTAINAMERICA
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMightyFoundry,1,0),  "APA", "ATA", "AEA", 'E', ModItems.itemEnergyModule,'T',ModItems.itemFluidModule, 'A',ModBlocks.blockAsgardianSteel, 'P',Blocks.piston));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemVibraniumAlloySheet,1,0),  "AAA", "AAA", "AAA", 'A', ModItems.itemVibraniumAlloy));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemMold,1,0), Items.paper, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand));
		
		
		//HULK
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSoundEmiter,1,0),  "GGG", "BMB", "BEB", 'G', ModBlocks.blockAsgardianGlass,'M',ModItems.itemMagnet,'B',ModBlocks.blockAsgardianSteel, 'E',ModItems.itemEnergyModule));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemAsgardianRing,1,0),  " S ", "S S", " S ", 'S', ModItems.itemAsgardianSteelIngot));
		
		
		//BACKTOTHEFUTURE
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMrFusion,1,0),  "III", "ICI", "OOO", 'I', Items.iron_ingot,'C',ModItems.itemFusionCore, 'O',Blocks.obsidian, 'E', ModItems.itemEnergyModule));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemFusionCore,1,0),  "PIP", "CNC", "PIP", 'I', Items.iron_ingot,'C', ModItems.itemFusionCasing, 'N', Items.nether_star, 'I', ModItems.itemEnergyModule, 'P', new ItemStack(ModItems.itemCircuitBoard,1,2)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,0),  "RRR", "III", "RRR", 'I', Items.iron_ingot,'R',Items.redstone));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,1),  "RRR", "III", "RRR", 'I', Items.gold_ingot,'R',Items.redstone));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,2),  "RRR", "III", "RRR", 'I', Items.iron_ingot,'R',ModItems.itemAsgardite));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,3),  "RRR", "III", "RRR", 'I', Items.gold_ingot,'R',ModItems.itemAsgardite));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockInductor,2,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0), new ItemStack(ModItems.itemMagnet,1,0)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockInductor,2,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1), new ItemStack(ModItems.itemMagnet,1,0)));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemGlassCutter,1,0),  "DI ", "II ", "  I", 'I', Items.iron_ingot,'D',Items.diamond));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemSilicon,3,0),new ItemStack(Items.water_bucket,1,0),new ItemStack(Items.quartz,1,0),new ItemStack(Items.quartz,1,0),new ItemStack(Blocks.sand,1,0),new ItemStack(Blocks.sand,1,0),new ItemStack(Blocks.sand,1,0),new ItemStack(Blocks.sand,1,0),new ItemStack(Blocks.sand,1,0), new ItemStack(Blocks.sand,1,0)));
		GameRegistry.addSmelting(new ItemStack(ModItems.itemSilicon,1,0), new ItemStack(ModItems.itemSilicon,1,1), 0);
		GameRegistry.addSmelting(new ItemStack(ModItems.itemSilicon,1,1), new ItemStack(ModItems.itemSilicon,1,2), 0);
		GameRegistry.addSmelting(new ItemStack(ModItems.itemSilicon,1,2), new ItemStack(ModItems.itemMicroChip,2,0), 0);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemMicroChip,2,0),new ItemStack(ModItems.itemSilicon,1,2),new ItemStack(ModItems.itemGlassCutter,1,0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMicroChip,1,1),  "QCQ", "QRQ", "QCQ", 'Q', Items.quartz,'C',new ItemStack(ModItems.itemMicroChip,1,0), 'R', Blocks.redstone_block));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMicroChip,1,2),  "CQC", "QRQ", "CQC", 'Q', Items.quartz,'C',new ItemStack(ModItems.itemMicroChip,1,0), 'R', Blocks.redstone_block));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,0),  "RRR", "CPC", "RRR",'P',new ItemStack(ModItems.itemMicroChip,1,0) , 'C', new ItemStack(ModItems.itemCable,1,0),'R',Items.redstone));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,1),  "RRR", "CPC", "RRR",'P',new ItemStack(ModItems.itemMicroChip,1,1) , 'C', new ItemStack(ModItems.itemCable,1,1),'R',Items.redstone));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,2),  "CCC", "RPR", "CCC",'P',new ItemStack(ModItems.itemMicroChip,1,2) , 'C', new ItemStack(ModItems.itemCable,1,1),'R',Items.redstone));
		
		
	}
	
	public static void loadOreDictionnaryRecipe(){

	}

}
