package noelflantier.sfartifacts.common.items;

import net.minecraft.item.Item;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemAsgardianSteelIngot  extends ItemSFA{
	public ItemAsgardianSteelIngot() {
		super("Asgardian Steel Ingot");
		this.setUnlocalizedName("itemAsgardianSteelIngot");
		this.setTextureName(References.MODID+":asgardian_steel_ingot");
	}
}
