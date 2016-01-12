package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemAsgardiumPearl extends ItemParticle{
	public ItemAsgardiumPearl() {
		super("Asgardium Pearl");
		this.setUnlocalizedName("itemAsgardiumPearl");
		this.setTextureName(References.MODID+":asgardium_pearl");
		this.setMaxStackSize(16);
	}
}
