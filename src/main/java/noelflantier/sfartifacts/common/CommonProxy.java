package noelflantier.sfartifacts.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModAchievements;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModEntities;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.handlers.ModNetworkMessages;
import noelflantier.sfartifacts.common.handlers.ModOreDictionary;
import noelflantier.sfartifacts.common.handlers.ModRecipes;
import noelflantier.sfartifacts.common.handlers.ModTileEntities;
import noelflantier.sfartifacts.common.helpers.SoundEmitterHelper;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.LiquefierRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.world.ModWorldGenOre;
import noelflantier.sfartifacts.common.world.village.ComponentPillar;
import noelflantier.sfartifacts.common.world.village.VillagePillarHandler;
import noelflantier.sfartifacts.compatibilities.InterMods;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CommonProxy {

	public ModEvents sfaEvents = new ModEvents();
	
	public void preInit(FMLPreInitializationEvent event) {
		//ModConfig.init(event.getSuggestedConfigurationFile());
		ModConfig.init(event);
        MinecraftForge.EVENT_BUS.register(sfaEvents);
        FMLCommonHandler.instance().bus().register(sfaEvents);
    	ModItems.loadItems();
    	ModBlocks.loadBlocks();
    	ModFluids.loadFluids();

    	GameRegistry.registerWorldGenerator(new ModWorldGenOre(), 0);
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePillarHandler());
        MapGenStructureIO.func_143031_a(ComponentPillar.class, References.MODID+":PillarStructure");
        
    	ModTileEntities.loadTileEntities();
    	ModNetworkMessages.loadMessages();

    	FMLInterModComms.sendMessage("Waila", "register", "noelflantier.sfartifacts.compatibilities.WailaHandler.load");

		ModAchievements.addModAchievements();
	}

	public void init(FMLInitializationEvent event) {
    	ModEntities.loadEntities();
    	NetworkRegistry.INSTANCE.registerGuiHandler(SFArtifacts.instance, new ModGUIs());
	}

	public void postinit(FMLPostInitializationEvent event) {
    	InterMods.loadConfig();
    	SoundEmitterHelper.loadSpawningRules();
		ModAchievements.registerAchievementPane();
    	ModOreDictionary.checkOreDictionary();
    	ModOreDictionary.loadOres();
    	ModRecipes.loadRecipes();
    	
    	InjectorRecipesHandler.getInstance().loadRecipes();
    	//InjectorRecipesHandler.getInstance().getRecipes().forEach((s)->s.debug());
    	HammerUpgradesRecipesHandler.getInstance().loadRecipes();
    	//HammerUpgradesRecipesHandler.getInstance().getRecipes().forEach((s)->s.debug());
    	//LiquefierRecipesHandler.getInstance().loadRecipes();
    	//MightyFoundryRecipesHandler.getInstance().loadRecipes();
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
}
