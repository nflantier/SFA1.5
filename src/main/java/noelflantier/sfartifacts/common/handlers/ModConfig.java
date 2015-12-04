package noelflantier.sfartifacts.common.handlers;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import noelflantier.sfartifacts.References;

public class ModConfig {
	

	public static boolean useOldRegistration;
	
	public static int capacityLiquefier;
	public static int capacityWaterLiquefier;
	public static int capacityAsgarditeLiquefier;
	public static int capacityInjector;
	public static int capacityAsgarditeInjector;
	public static int capacityMightyFoundry;
	public static int capacityLavaMightyFoundry;
	public static int capacitySoundEmiter;
	public static int capacityAsgarditeSoundEmitter;
	
	public static int rfNeededThorHammer;
	public static int rfMining;
	public static int rfLightning;
	public static int rfMoving;
	public static int rfEntityHit;
	public static int rfThorhammer;
	public static int rfTeleporting;
	public static int rfAddedPerCapacityUpgrade;
	public static int distanceLightning;
	public static int shieldProtection;
	public static boolean isManualSpawning;
	public static boolean isPillarEmitParticles;
	public static boolean isOresEmitParticles;
	public static boolean isItemsEmitParticles;
	public static double chanceToSpawnMightyFeather;
	public static int tickToCookVibraniumOres;
	public static boolean isShieldBlockOnlyWhenShift;
	
	public static int rfNeededToSpawnHulk;
	public static int rfNeededToSpawnDefault;
	public static int fluidNeededToSpawnHulk;
	public static int fluidNeededToSpawnDefault;
	public static boolean areFrequenciesShown;

	public final static String CAT_UTILS = "Utils";
	public final static String CAT_THOR_HAMMER = "Thor s Hammer";
	public final static String CAT_SOUND_EMITTER = "Sound Emiter";
	public final static String CAT_VIBRANIUM_SHIELD = "Vibranium Shield";
	public final static String CAT_MACHINES = "Machines";
	
	public static Configuration config;
	public static File configDirectory;
	
	public static void init(FMLPreInitializationEvent event) {
		if( configDirectory == null){
			configDirectory = new File(event.getModConfigurationDirectory(), References.MODID.toLowerCase());
		    if(!configDirectory.exists()) {
		    	configDirectory.mkdir();
		    }
		}
		if (config == null) {
			config = new Configuration(new File(configDirectory, References.NAME+".cfg"));
			syncConfig();
		}
	}
	
	public static void syncConfig() {
		try {
			
			distanceLightning = config.get(CAT_THOR_HAMMER, "block/distance", 60, "The distance in block at which you can throw lightnings").getInt();
			rfNeededThorHammer = config.get(CAT_THOR_HAMMER, "rf/invok", 1000000, "The amount of RF energy needed to invok thor's hammer").getInt();
			rfMining = config.get(CAT_THOR_HAMMER, "rf/block mined", 50, "The amount of RF used by thor's hammer for each block mined").getInt();
			rfTeleporting = config.get(CAT_THOR_HAMMER, "rf/teleport", 500, "The amount of RF used by thor's hammer when you use the teleport mod").getInt();
			rfLightning = config.get(CAT_THOR_HAMMER, "rf/lightning", 50, "The amount of RF used by thor's hammer when you throw a lightning").getInt();
			rfMoving = config.get(CAT_THOR_HAMMER, "rf/throw", 150, "The amount of RF used by thor's hammer when you are using the moving mod").getInt();
			rfEntityHit = config.get(CAT_THOR_HAMMER, "rf/entity hit", 20, "The amount of RF used by thor's hammer when you hit an entity").getInt();
			rfThorhammer = config.get(CAT_THOR_HAMMER, "capacity", 100000, "Thor's Hammer RF capacity").getInt();
			rfAddedPerCapacityUpgrade = config.get(CAT_THOR_HAMMER, "capacity added/capacity upgrade", 10000, "The amount of RF capacity upgrade add to the hammer").getInt();
			
			shieldProtection = config.get(CAT_VIBRANIUM_SHIELD, "angle", 90, "The Angle in degrees your shield deflect damages around you ex:90 = 45 to your left 45 to your right").getInt();
			isShieldBlockOnlyWhenShift = config.get(CAT_VIBRANIUM_SHIELD, "shift", true, "Is the shield block damages only when you shift with it").getBoolean();
			
			rfNeededToSpawnDefault = config.get(CAT_SOUND_EMITTER, "rf/spawnD", 10, "The amount of RF energy needed to spawn a living entity by default").getInt();
			fluidNeededToSpawnDefault = config.get(CAT_SOUND_EMITTER, "fluid/spawnD", 10, "The amount of Milibucket needed to spawn a living entity by default").getInt();
			rfNeededToSpawnHulk = config.get(CAT_SOUND_EMITTER, "rf/spawnH", 495000, "The amount of RF energy needed to spawn HULK").getInt();
			fluidNeededToSpawnHulk = config.get(CAT_SOUND_EMITTER, "fluid/spawnH", 95000, "The amount of Milibucket needed to spawn HULK").getInt();
			areFrequenciesShown = config.get(CAT_SOUND_EMITTER, "show frequencies", false, "If set to true you'll have all the frequencies for each mobs shown in your list").getBoolean();
			
			isPillarEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "pillars particles", true, "Is the pillars emit particles").getBoolean();
			isOresEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "ores particles", true, "Is asgardian ores emit particles").getBoolean();
			isItemsEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "items particles", true, "Is asgardian items emit particles").getBoolean();
			
			isManualSpawning = config.get(Configuration.CATEGORY_GENERAL, "manual spawn", true, "Is the manual spawn at player on new log in").getBoolean();
			chanceToSpawnMightyFeather = config.get(Configuration.CATEGORY_GENERAL, "chance to drop mighty feather", 0.35, "Chance that chickens hit by lightning drop mighty feather [0-1]").getDouble();
			tickToCookVibraniumOres = config.get(Configuration.CATEGORY_GENERAL, "ticks", 6000, "The number of ticks needed to vibranium ores to be cooked 20 tick = 1 s").getInt();
			
			capacityLiquefier = config.get(CAT_MACHINES, "Liquefier capacity", 20000, "The liquefier energy capacity").getInt();
			capacityWaterLiquefier = config.get(CAT_MACHINES, "Liquefier water capacity", 20000, "The liquefier water capacity").getInt();
			capacityAsgarditeLiquefier = config.get(CAT_MACHINES, "Liquefier liquefied asgardite capacity", 20000, "The liquefier liquefied asgardite capacity").getInt();
			
			capacityInjector = config.get(CAT_MACHINES, "Injector capacity", 20000, "The injector energy capacity").getInt();
			capacityAsgarditeInjector = config.get(CAT_MACHINES, "Injector liquefied asgardite capacity", 20000, "The injector liquefied asgardite capacity").getInt();
			
			capacityMightyFoundry = config.get(CAT_MACHINES, "MightyFoundry capacity", 50000, "The mightyfoundry energy capacity").getInt();
			capacityLavaMightyFoundry = config.get(CAT_MACHINES, "MightyFoundry lava capacity", 20000, "The mightyfoundry lava capacity").getInt();
			
			capacitySoundEmiter = config.get(CAT_MACHINES, "SoundEmitter capacity", 500000, "The soundemitter energy capacity").getInt();
			capacityAsgarditeSoundEmitter = config.get(CAT_MACHINES, "SoundEmitter liquefied asgardite capacity", 100000, "The soundemitter liquefied asgardite capacity").getInt();

			useOldRegistration = config.get(CAT_UTILS, "Use old registration", false, "In the newer version registered name have changed so if you have an id error set this to true").getBoolean();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (config.hasChanged()) config.save();
		}
	}
}
