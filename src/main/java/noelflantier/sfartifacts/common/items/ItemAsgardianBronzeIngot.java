package noelflantier.sfartifacts.common.items;

import net.minecraft.item.Item;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemAsgardianBronzeIngot  extends ItemSFA{
	public ItemAsgardianBronzeIngot() {
		super("Asgardian Bronze Ingot");
		this.setUnlocalizedName("itemAsgardianBronzeIngot");
		this.setTextureName(References.MODID+":asgardian_bronze_ingot");
	}
}
