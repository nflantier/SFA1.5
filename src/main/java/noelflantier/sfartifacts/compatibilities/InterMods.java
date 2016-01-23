package noelflantier.sfartifacts.compatibilities;

import cpw.mods.fml.common.Loader;

public class InterMods {
	public static boolean hasNei = false;
	public static boolean hasIc2 = false;
	
	public static void loadConfig(){
    	hasNei = Loader.isModLoaded("NotEnoughItems");
    	hasIc2 = Loader.isModLoaded("IC2");
	}
}
