package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.References;

public class ItemFusionCore extends ItemSFA{
	public ItemFusionCore() {
		super("Fusion Core");
		this.setUnlocalizedName("itemFusionCore");
		this.setTextureName(References.MODID+":fusion_core");
	}

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_)
    {
        return true;
    }
}

