package noelflantier.sfartifacts.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOreVibranium  extends ItemBlockSFA{
	public static String[] typeVibranium = new String[] {"0", "35", "75", "100"};

	public ItemOreVibranium(Block b) {
		super(b, "Ore Vibranium");
		this.hasSubtypes = true;
	}

	@Override
    public int getMetadata(int meta){
        return meta;
    }

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
		int meta = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+typeVibranium[meta<15?meta<8?meta<1?0:1:2:3];
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it0 = new ItemStack(item, 1, 0);
		list.add(it0);
		
		ItemStack it100 = new ItemStack(item, 1,15 );
		list.add(it100);
		
	}
	
}