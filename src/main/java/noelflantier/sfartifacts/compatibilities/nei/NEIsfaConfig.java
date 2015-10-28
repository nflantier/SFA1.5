package noelflantier.sfartifacts.compatibilities.nei;

import noelflantier.sfartifacts.References;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;

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
	    API.registerRecipeHandler(new InjectorRecipeHandler());
	    API.registerUsageHandler(new InjectorRecipeHandler());
	}

}
