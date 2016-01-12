package noelflantier.sfartifacts.compatibilities.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;
import noelflantier.sfartifacts.References;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIsfaConfig  implements IConfigureNEI {

	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public String getName() {
		return References.NAME;
	}

	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public String getVersion() {
		return References.VERSION;
	}

	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public void loadConfig() {
	    API.registerRecipeHandler(new InjectorRecipeUsageHandler());
	    API.registerUsageHandler(new InjectorRecipeUsageHandler());
	    API.registerRecipeHandler(new MightyFoundryRecipeUsageHandler());
	    API.registerUsageHandler(new MightyFoundryRecipeUsageHandler());
	    API.registerRecipeHandler(new MoldFilledRecipeUsageHandler());
	    API.registerUsageHandler(new MoldFilledRecipeUsageHandler());
	}

}
