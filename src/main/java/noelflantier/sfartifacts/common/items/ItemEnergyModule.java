package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;

public class ItemEnergyModule extends ItemSFA{
	public ItemEnergyModule() {
		super("Energy Module");
		this.setUnlocalizedName("itemEnergyModule");
		this.setTextureName(References.MODID+":energy_module");
	}
}
