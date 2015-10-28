package noelflantier.sfartifacts.common.handlers;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary {

	public static String copperIngotName = "ingotCopper";
	public static String bronzeIngotName = "ingotBronze";
	public static boolean hasCopperIngot = false;
	public static boolean hasBronzeIngot = false;
	
	public static boolean isRegistered(String name) {
		if(!OreDictionary.getOres(name).isEmpty()) {
			return true;
		}
		return false;
	}

	public static void loadOres(){
		 OreDictionary.registerOre("asgarditeOre", ModBlocks.blockOreAsgardite);
		 OreDictionary.registerOre("vibraniumOre", ModBlocks.blockOreVibranium);
	}
	
	public static void checkOreDictionary(){
		hasCopperIngot =  isRegistered(copperIngotName);
		hasBronzeIngot =  isRegistered(bronzeIngotName);
	}
}
