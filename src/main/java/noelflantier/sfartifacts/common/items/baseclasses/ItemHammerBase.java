package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraft.item.Item;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.items.ItemSFA;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHammerBase extends ItemSFA{
	
	public ItemHammerBase(){
		super("Thor Hammer");
		this.setMaxStackSize(1);
	}
	
	public String getUnwrappedUnlocalizedName(final String unlocalizedName){
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

    @SideOnly(Side.CLIENT)
	@Override
    public boolean isFull3D()
    {
        return true;
    }
}
