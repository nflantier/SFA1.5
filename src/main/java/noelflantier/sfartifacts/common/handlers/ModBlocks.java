package noelflantier.sfartifacts.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianBronze;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianGlass;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianSteel;
import noelflantier.sfartifacts.common.blocks.BlockAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockControlPannel;
import noelflantier.sfartifacts.common.blocks.BlockHammerStand;
import noelflantier.sfartifacts.common.blocks.BlockInjector;
import noelflantier.sfartifacts.common.blocks.BlockLightningRodStand;
import noelflantier.sfartifacts.common.blocks.BlockLiquefier;
import noelflantier.sfartifacts.common.blocks.BlockMightyFoundry;
import noelflantier.sfartifacts.common.blocks.BlockOreAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockOreVibranium;
import noelflantier.sfartifacts.common.blocks.BlockSoundEmitter;
import noelflantier.sfartifacts.common.blocks.FilledBucket;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;
import noelflantier.sfartifacts.common.items.ItemBlockMachine;
import noelflantier.sfartifacts.common.items.ItemHammerStand;
import noelflantier.sfartifacts.common.items.ItemOreVibranium;

public class ModBlocks {

	public static Block blockHammerStand;
	public static Block blockControlPanel;
	public static Block blockAsgardianBronze;
	public static Block blockAsgardianSteel;
	public static Block blockOreAsgardite;
	public static Block blockOreVibranium;
	public static Block blockAsgardite;
	public static Block blockLightningRodStand;
	public static Block blockLiquefier;
	public static Block blockInjector;
	public static Block blockMightyFoundry;
	public static Block blockSoundEmiter;
	public static Block blockAsgardianGlass;
	
	public static void loadBlocks() {

    	blockOreAsgardite = new BlockOreAsgardite(Material.rock);
    	RegisterHelper.registerBlock(blockOreAsgardite);
    	
    	blockOreVibranium = new BlockOreVibranium(Material.rock);
    	RegisterHelper.registerBlock(blockOreVibranium, ItemOreVibranium.class);

    	blockAsgardite = new BlockAsgardite(Material.rock);
    	RegisterHelper.registerBlock(blockAsgardite);
    	
    	blockAsgardianSteel = new BlockAsgardianSteel(Material.rock);
    	RegisterHelper.registerBlock(blockAsgardianSteel);
    	
    	blockAsgardianBronze = new BlockAsgardianBronze(Material.rock);
    	RegisterHelper.registerBlock(blockAsgardianBronze);
    	
    	blockAsgardianGlass = new BlockAsgardianGlass(Material.glass);
    	RegisterHelper.registerBlock(blockAsgardianGlass);
    	
		blockHammerStand = new BlockHammerStand(Material.rock);
    	RegisterHelper.registerBlock(blockHammerStand, ItemHammerStand.class);
    	
    	blockControlPanel = new BlockControlPannel(Material.rock);
    	RegisterHelper.registerBlock(blockControlPanel);
    	
    	blockLightningRodStand = new BlockLightningRodStand(Material.rock);
    	RegisterHelper.registerBlock(blockLightningRodStand, ItemBlockMachine.class);
    	
    	blockLiquefier = new BlockLiquefier(Material.rock);
    	RegisterHelper.registerBlock(blockLiquefier, ItemBlockMachine.class);
    	
    	blockInjector = new BlockInjector(Material.rock);
    	RegisterHelper.registerBlock(blockInjector, ItemBlockMachine.class);
    	
    	blockMightyFoundry = new BlockMightyFoundry(Material.rock);
    	RegisterHelper.registerBlock(blockMightyFoundry);
    	
    	blockSoundEmiter = new BlockSoundEmitter(Material.rock);
    	RegisterHelper.registerBlock(blockSoundEmiter);
    	
	}	

}
