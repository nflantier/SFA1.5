package noelflantier.sfartifacts.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.entities.EntityItemParticle;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemUberMightyFeather   extends ItemParticle{
	public ItemUberMightyFeather() {
		super("Uber Mighty Feather");
		this.setUnlocalizedName("itemUberMightyFeather");
		this.setTextureName(References.MODID+":uber_mighty_feather");
	}
}

