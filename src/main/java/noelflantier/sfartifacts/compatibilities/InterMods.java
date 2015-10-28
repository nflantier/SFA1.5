package noelflantier.sfartifacts.compatibilities;

import cpw.mods.fml.common.Loader;

public class InterMods {

	public static boolean hasEnderIO = false;
	public static boolean hasNei = false;
	
	
	public static void loadConfig(){
    	hasEnderIO = Loader.isModLoaded("EnderIO");
    	hasNei = Loader.isModLoaded("NotEnoughItems");
	}
}
