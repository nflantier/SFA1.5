package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public interface IBlockUsingMaterials {
    void getSubBlocks(Item item, CreativeTabs tabs, List list);
    int damageDropped(int meta);
}
