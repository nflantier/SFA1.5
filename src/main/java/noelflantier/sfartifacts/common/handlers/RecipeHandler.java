package noelflantier.sfartifacts.common.handlers;

import java.util.ArrayList;

public class RecipeHandler {
	//private final List<IManyToOneRecipe> recipes = new ArrayList<IManyToOneRecipe>();

	private final String modFileName;
	private final String userFileName;
	private final String forName;

	  public RecipeHandler(String modFileName, String userFileName, String forName) {  
	    this.modFileName = modFileName;
	    this.userFileName = userFileName;
	    this.forName = forName;
	  }
}
