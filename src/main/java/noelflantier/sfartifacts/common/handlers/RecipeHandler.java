package noelflantier.sfartifacts.common.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.CustomHandler;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeParser;

public class RecipeHandler {

	public static RecipeHandler loadRecipeConfig(String fileName, CustomHandler customHandler, Class<? extends RecipeBase> classRecipe){
		File file = new File(ModConfig.configDirectory, fileName);
	    String defaultVals = null;
    	try{
    		defaultVals = readRecipes(file,fileName);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        if(!file.exists()) {
        	System.out.println("Could not load default recipes from " + file + " as the file does not exist.");
        	return null;
        }
        
        RecipeHandler rh;
        try {
        	rh = RecipeParser.parse(defaultVals, customHandler, classRecipe);
        } catch (Exception e) {
        	return null;
        }
        
        return rh;
	}

	public static String readRecipes(File file, String fileName) throws IOException {
		if(file.exists()) {
			return readStream(new FileInputStream(file));
		}
		InputStream ins = RecipeHandler.class.getResourceAsStream("/assets/sfartifacts/config/" + fileName);
		if(ins == null) {
		    throw new IOException("Could not find resource /assets/sfartifacts/config/" + fileName + " form classpath. ");
		}

	    String output = readStream(ins);
	    BufferedWriter writer = null;
	    try {
	    	writer = new BufferedWriter(new FileWriter(file, false));
	    	writer.write(output.toString());
	    } finally {
	    	IOUtils.closeQuietly(writer);
	    }
	    return output.toString();
	}
	
	private static String readStream(InputStream ins) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		StringBuilder output = new StringBuilder();
		try {
			String line = reader.readLine();
		    while (line != null) {
		    	output.append(line);
		        output.append("\n");
		        line = reader.readLine();
		    }
	    } finally {
	    	IOUtils.closeQuietly(reader);
	    }
		return output.toString();
	}

	public List<ISFARecipe> recipes = new ArrayList<ISFARecipe>();
	public RecipeHandler() {
		
	}
	
	public void addRecipe(ISFARecipe recipe){
		recipes.add(recipe);
	}
}
