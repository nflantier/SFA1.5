package noelflantier.sfartifacts.common.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {
	
	
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
	
	public String catThorhammer = "Thor s Hammer";
	public static Configuration config;
	
	public static void init(File confFile) {
		if (config == null) {
			config = new Configuration(confFile);
			syncConfig();
		}
	}
	
	public static void syncConfig() {
		try {
			
			distanceLightning = config.get("Thor s Hammer", "block/distance", 60, "The distance in block at which you can throw lightnings").getInt();
			rfNeededThorHammer = config.get("Thor s Hammer", "rf/invok", 1000000, "The amount of RF energy needed to invok thor's hammer").getInt();
			rfMining = config.get("Thor s Hammer", "rf/block mined", 50, "The amount of RF used by thor's hammer for each block mined").getInt();
			rfTeleporting = config.get("Thor s Hammer", "rf/teleport", 500, "The amount of RF used by thor's hammer when you use the teleport mod").getInt();
			rfLightning = config.get("Thor s Hammer", "rf/lightning", 50, "The amount of RF used by thor's hammer when you throw a lightning").getInt();
			rfMoving = config.get("Thor s Hammer", "rf/throw", 150, "The amount of RF used by thor's hammer when you are using the moving mod").getInt();
			rfEntityHit = config.get("Thor s Hammer", "rf/entity hit", 20, "The amount of RF used by thor's hammer when you hit an entity").getInt();
			rfThorhammer = config.get("Thor s Hammer", "capacity", 100000, "Thor's Hammer RF capacity").getInt();
			rfAddedPerCapacityUpgrade = config.get("Thor s Hammer", "capacity added/capacity upgrade", 10000, "The amount of RF capacity upgrade add to the hammer").getInt();
			
			shieldProtection = config.get("Vibranium Shield", "angle", 90, "The Angle in degrees your shield deflect damages around you ex:90 = 45 to your left 45 to your right").getInt();
			isShieldBlockOnlyWhenShift = config.get("Vibranium Shield", "shift", true, "Is the shield block damages only when you shift with it").getBoolean();

			rfNeededToSpawnDefault = config.get("Sound Emiter", "rf/spawnD", 10, "The amount of RF energy needed to spawn a living entity by default").getInt();
			fluidNeededToSpawnDefault = config.get("Sound Emiter", "fluid/spawnD", 10, "The amount of Milibucket needed to spawn a living entity by default").getInt();
			
			rfNeededToSpawnHulk = config.get("Sound Emiter", "rf/spawnH", 495000, "The amount of RF energy needed to spawn HULK").getInt();
			fluidNeededToSpawnHulk = config.get("Sound Emiter", "fluid/spawnH", 95000, "The amount of Milibucket needed to spawn HULK").getInt();
			
			areFrequenciesShown = config.get("Sound Emiter", "show frequencies", false, "If set to true you'll have all the frequencies for each mobs shown in your list").getBoolean();
			
			isPillarEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "pillars particles", true, "Is the pillars emit particles").getBoolean();
			isOresEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "ores particles", true, "Is asgardian ores emit particles").getBoolean();
			isItemsEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "items particles", true, "Is asgardian items emit particles").getBoolean();
			
			isManualSpawning = config.get(Configuration.CATEGORY_GENERAL, "manual spawn", true, "Is the manual spawn at player on new log in").getBoolean();
			chanceToSpawnMightyFeather = config.get(Configuration.CATEGORY_GENERAL, "chance to drop mighty feather", 0.35, "Chance that chickens hit by lightning drop mighty feather [0-1]").getDouble();
			tickToCookVibraniumOres = config.get(Configuration.CATEGORY_GENERAL, "ticks", 6000, "The number of ticks needed to vibranium ores to be cooked 20 tick = 1 s").getInt();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (config.hasChanged()) config.save();
		}
	}
}
