package noelflantier.sfartifacts.common.items;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public class ItemBlockMachine extends ItemBlockSFA{

	public ItemBlockMachine(Block b) {
		super(b, "Block Machine");
		this.hasSubtypes = true;
	}

	@Override
    public int getMetadata(int meta){
        return meta;
    }

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
    	int i = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+StringUtils.lowerCase(PillarMaterials.values()[i].name());
    }
	
}
