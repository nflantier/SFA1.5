package noelflantier.sfartifacts.common.recipes.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.CustomHandler;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeParser;

public class MightyFoundryRecipesHandler extends AUsageRecipes{
	private static final String FILE_NAME_MIGHTY_FOUNDRY = "MightyFoundryRecipes.xml";
	private static final String USAGE_MIGHTY_FOUNDRY= "MightyFoundry";
		
	public MightyFoundryRecipesHandler() {
		super(FILE_NAME_MIGHTY_FOUNDRY, USAGE_MIGHTY_FOUNDRY);
	}
	
	public static final MightyFoundryRecipesHandler instance = new MightyFoundryRecipesHandler();
	public static MightyFoundryRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public CustomHandler createCustomHandler(){
		return new MightyFoundryCustomHandler();
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeMightyFoundry.class;
	}

	private class MightyFoundryCustomHandler implements CustomHandler {

	    private boolean inTag = false;
	    
		@Override
		public boolean endElement(String uri, String localName, String qName, RecipeParser parser) throws SAXException {
			if(RecipeParser.ELEMENT_RECIPE.equals(localName)) {
		        inTag = false;
		    }
			return inTag;
		}

		@Override
		public boolean startElement(String uri, String localName, String qName, Attributes attributes, RecipeParser parser)throws SAXException {
		    if(RecipeParser.ELEMENT_RECIPE.equals(localName)){
		        if(parser.currentRecipe!=null){
		        	if(parser.classRecipe == getClassOfRecipe()){
		        		RecipeMightyFoundry.class.cast(parser.currentRecipe).setTickPerItem(RecipeParser.getIntValue("tickPerItem", attributes, 1));
			        	inTag = true;
		        	}
		        }
		    }
			return inTag;
		}

		@Override
		public void process() {
			// TODO Auto-generated method stub
			
		}
	
	}
	
}
