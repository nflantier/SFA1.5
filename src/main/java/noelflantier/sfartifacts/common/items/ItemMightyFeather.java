package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemMightyFeather  extends ItemParticle{
	public ItemMightyFeather() {
		super("Mighty Feather");
		this.setUnlocalizedName("itemMightyFeather");
		this.setTextureName(References.MODID+":mighty_feather");
	}
}
