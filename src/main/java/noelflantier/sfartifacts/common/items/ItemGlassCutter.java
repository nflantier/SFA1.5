package noelflantier.sfartifacts.common.items;

import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;

public class ItemGlassCutter extends ItemSFA{

	public ItemGlassCutter() {
		super("Glass cutter");
		this.setUnlocalizedName("itemGlassCutter");
		this.setTextureName(References.MODID+":glass_cutter");
		this.setMaxStackSize(1);
		this.setContainerItem(this);
	}
	 
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_){
        return false;
    }
}
