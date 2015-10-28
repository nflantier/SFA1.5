package noelflantier.sfartifacts.common.helpers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;

public class RecipeHelper {

	/*  public static RecipeConfig loadRecipeConfig(String coreFileName, String customFileName, CustomTagHandler customHandler) {
	    File coreFile = new File(Config.configDirectory, coreFileName);

	    String defaultVals = null;
	    try {
	      defaultVals = readRecipes(coreFile, coreFileName, true);
	    } catch (IOException e) {
	      Log.error("Could not load default recipes file " + coreFile + " from EnderIO jar: " + e.getMessage());
	      e.printStackTrace();
	      return null;
	    }

	    if(!coreFile.exists()) {
	      Log.error("Could not load default recipes from " + coreFile + " as the file does not exist.");
	      return null;
	    }

	    RecipeConfig config;
	    try {
	      config = RecipeConfigParser.parse(defaultVals, customHandler);
	    } catch (Exception e) {
	      Log.error("Error parsing " + coreFileName);
	      return null;
	    }

	    File userFile = new File(Config.configDirectory, customFileName);
	    String userConfigStr = null;
	    try {
	      userConfigStr = readRecipes(userFile, customFileName, false);
	      if(userConfigStr == null || userConfigStr.trim().length() == 0) {
	        Log.error("Empty user config file: " + userFile.getAbsolutePath());
	      } else {
	        RecipeConfig userConfig = RecipeConfigParser.parse(userConfigStr, customHandler);
	        config.merge(userConfig);
	      }
	    } catch (Exception e) {
	      Log.error("Could not load user defined recipes from file: " + customFileName);
	      e.printStackTrace();
	    }
	    return config;
	  }

	  public static String readRecipes(File copyTo, String fileName, boolean replaceIfExists) throws IOException {
	    if(!replaceIfExists && copyTo.exists()) {
	      return readStream(new FileInputStream(copyTo));
	    }

	    InputStream in = RecipeConfig.class.getResourceAsStream("/assets/enderio/config/" + fileName);
	    if(in == null) {
	      Log.error("Could load default AlloySmelter recipes.");
	      throw new IOException("Could not resource /assets/enderio/config/" + fileName + " form classpath. ");
	    }
	    String output = readStream(in);
	    BufferedWriter writer = null;
	    try {
	      writer = new BufferedWriter(new FileWriter(copyTo, false));
	      writer.write(output.toString());
	    } finally {
	      IOUtils.closeQuietly(writer);
	    }
	    return output.toString();
	  }

	  private static String readStream(InputStream in) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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
	  }*/
}
