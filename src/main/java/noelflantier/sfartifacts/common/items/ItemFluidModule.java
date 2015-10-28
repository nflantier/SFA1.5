package noelflantier.sfartifacts.common.items;

import net.minecraft.item.Item;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;

public class ItemFluidModule extends ItemSFA{

	public ItemFluidModule() {
		super("Fluid Module");
		this.setUnlocalizedName("itemFluidModule");
		this.setTextureName(References.MODID+":fluid_module");
	}
}
