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
	}
	
	public static void loadOreDictionnaryRecipe(){

		/*if(ModConfig.useModsStuff){
			if(ModOreDictionary.hasCopperIngot){
				List<ItemStack> ores = OreDictionary.getOres("ingotDarkSteel");
			    if(!ores.isEmpty() && ores.get(0) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardianBronze,1,0), "ddd", "d d", "ddd", 'd', ores.get(0).copy() ));
			    }
			}	
		}*/
	}

}
