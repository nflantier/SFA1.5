package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemUberMightyFeather   extends ItemParticle{
	public ItemUberMightyFeather() {
		super("Uber Mighty Feather");
		this.setUnlocalizedName("itemUberMightyFeather");
		this.setTextureName(References.MODID+":uber_mighty_feather");
	}
}

