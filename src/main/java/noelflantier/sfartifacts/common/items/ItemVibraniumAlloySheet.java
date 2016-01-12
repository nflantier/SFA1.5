package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.References;

public class ItemVibraniumAlloySheet  extends ItemSFA{

	public ItemVibraniumAlloySheet() {
		super("Vibranium Alloy Sheet");
		this.setUnlocalizedName("itemVibraniumAlloySheet");
		this.setTextureName(References.MODID+":vibranium_alloy_sheet");
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
}
