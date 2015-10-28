package noelflantier.sfartifacts.common.handlers;

public class InjectorRecipeHandler extends RecipeHandler{
	private static final String MOD_RECIPE_FILE_NAME = "InjectorRecipes_Mod.xml";
	private static final String USER_RECIPE_FILE_NAME = "InjectorRecipes_User.xml";

	public InjectorRecipeHandler() {
		super(MOD_RECIPE_FILE_NAME, USER_RECIPE_FILE_NAME, "Injector");
	}
	
	public void loadRecipes() {
		
    }
}
