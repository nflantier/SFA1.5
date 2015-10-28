package noelflantier.sfartifacts.common.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;

public enum Molds {
	
	VIBRANIUMSHIELD(
			"Vibranium Shield",
			new int[]{	Integer.parseInt("000111000", 2),
						Integer.parseInt("001111100", 2),
						Integer.parseInt("011111110", 2),
						Integer.parseInt("111111111", 2),
						Integer.parseInt("111111111", 2),
						Integer.parseInt("111111111", 2),
						Integer.parseInt("011111110", 2),
						Integer.parseInt("001111100", 2),
						Integer.parseInt("000111000", 2)},
			new ItemStack(ModItems.itemVibraniumAlloySheet,32,0),
			ModConfig.isShieldBlockOnlyWhenShift?ItemNBTHelper.setBoolean(new ItemStack(ModItems.itemVibraniumShield,1,0), "CanBlock", false):ItemNBTHelper.setBoolean(new ItemStack(ModItems.itemVibraniumShield, 1, 0), "CanBlock", true)
	);

	public final int ID;
	public String name;
	public int recipe[];
	public static int globalID = 0;
	public ItemStack ingredients;
	public ItemStack result;
	
	private Molds(String name, int[] recipe, ItemStack ingredients, ItemStack result){
		this.name = name;
		this.recipe = recipe;
		this.ID = nextGlobalID();
		this.ingredients = ingredients;
		this.result = result;
	}

	private int nextGlobalID(){
		globalID++;
		return globalID;
	}
	
	public static Molds isRecipe(int r[]){
		for(Molds m : Molds.values()){
			for(int i = 0;i<m.recipe.length;i++){
				if(m.recipe[i]!=r[i])
					break;
				if(i==m.recipe.length-1)
					return m;
			}
		}
		return null;
	} 
	
	public static Molds getMold(int id){
		for(Molds m : Molds.values()){
			if(m.ID == id)
				return m;
		}
		return null;
	} 
}
