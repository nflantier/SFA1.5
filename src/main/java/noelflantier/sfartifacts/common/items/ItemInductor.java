package noelflantier.sfartifacts.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;

public class ItemInductor extends ItemBlockSFA{

	protected IIcon[] metaIcons;
	public static String[] typeInductor = new String[]  {"basic", "advanced", "basicenergized", "advancedenergized"};
	
	public ItemInductor(Block b) {
		super(b, "Inductor", true);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemstack){
    	int i = itemstack.getItemDamage();
    	if (i < 0 || i >= this.typeInductor.length){
        	i = 0;
        }
        return super.getUnlocalizedName()+"."+this.typeInductor[i];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		return this.metaIcons[metadata];
	}

	/*@Override
	public void registerIcons(IIconRegister iconRegister) {
        this.metaIcons = new IIcon[this.typeInductor.length];
		for (int i = 0; i < metaIcons.length; i++) {
			metaIcons[i] = iconRegister.registerIcon(References.MODID + ":inductor_" + this.typeInductor[i]);
		}
	}*/
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creaT, List list){
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
    	list.add(new ItemStack(item, 1, 3));
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add("Capacity : "+TileInductor.energyCapacityConfig.get(stack.getItemDamage())+"RF/T");
	}
	
	@Override
    public int getMetadata(int meta){
		return meta;
    }
}
