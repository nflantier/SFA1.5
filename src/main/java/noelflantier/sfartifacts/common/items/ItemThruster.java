package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;

public class ItemThruster extends ItemSFA{
	public ItemThruster() {
		super("Thruster");
		this.setUnlocalizedName("itemThruster");
		this.setTextureName(References.MODID+":thruster");
	}
}