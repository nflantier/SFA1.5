package noelflantier.sfartifacts.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;

public class ItemLightningRod extends ItemSFA{

	protected IIcon[] metaIcons;
	public static String[] typeLightningRod = new String[] {"basic", "advanced", "reinforced", "ultimate"};
	
	public ItemLightningRod() {
		super("Lightning Rod");
		this.setHasSubtypes(true);
		this.setUnlocalizedName("lightningRod");
	}
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
    	int i = itemstack.getItemDamage();
    	if (i < 0 || i >= this.typeLightningRod.length)
        {
        	i = 0;
        }

        return super.getUnlocalizedName()+"."+this.typeLightningRod[i];
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creaT, List list)
	{
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
    	list.add(new ItemStack(item, 1, 3));    
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		return this.metaIcons[metadata];
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {

        this.metaIcons = new IIcon[this.typeLightningRod.length];
		for (int i = 0; i < metaIcons.length; i++) {
			metaIcons[i] = iconRegister.registerIcon(References.MODID + ":lightningrod_" + this.typeLightningRod[i]);
		}
	}
	
	@Override
    public int getMetadata(int meta){
		return meta;
    }

}