package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;

public class ItemFusionCoreFrame extends ItemSFA{
	public ItemFusionCoreFrame() {
		super("Fusion Core Frame");
		this.setUnlocalizedName("itemFusionCoreFrame");
		this.setTextureName(References.MODID+":fusion_core_frame");
	}

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_)
    {
        return true;
    }
}

