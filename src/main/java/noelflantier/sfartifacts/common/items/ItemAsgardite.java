package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemAsgardite extends ItemParticle{
	
	public ItemAsgardite() {
		super("Asgardite");
		this.setUnlocalizedName("itemAsgardite");
		this.setTextureName(References.MODID+":asgardite");
		
	}
}
