package noelflantier.sfartifacts.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class ItemSilicon extends ItemSFA{

	public IIcon[] metaIcons;
	public static String[] typeSilicon = new String[] {"raw", "clear", "pure"};
	public static int[] colorPrct = new int[]{16777215, 14606046, 12434877, 10921638, 9737364 };
	
    public ItemSilicon(){
		super("Silicon");
		this.setHasSubtypes(true);
		this.setUnlocalizedName("itemSilicon");
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemstack){
    	int i = itemstack.getItemDamage();
    	if (i < 0 || i >= this.typeSilicon.length){
        	i = 0;
        }
        return super.getUnlocalizedName()+"."+this.typeSilicon[i];
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creaT, List list){
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
	}

	@Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isequiped) {
    	if(stack.getItemDamage()==2 && !world.isRemote){
    		long age = ItemNBTHelper.getLong(stack, "agesilicon", -1);
    		ItemNBTHelper.setLong(stack, "worldtime", world.getTotalWorldTime());
    		if(age<=-1){
        		ItemNBTHelper.setLong(stack, "agesilicon", world.getTotalWorldTime()+ModConfig.pureSiliconLifeSpan);
        		return;
    		}
    		if(age-world.getTotalWorldTime()<=0){
    			if(entity instanceof EntityPlayer){
    				((EntityPlayer)entity).inventory.setInventorySlotContents(slot, new ItemStack(ModItems.itemSilicon,stack.stackSize,1));
    			}
    			return;
    		}
    	}
    }
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	if(stack.getItemDamage()==2 && !world.isRemote){
    		ItemNBTHelper.setLong(stack, "agesilicon", world.getTotalWorldTime()+ModConfig.pureSiliconLifeSpan);
    	}
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int p_82790_2_){
		if(stack.getItemDamage()==2){
    		long age = ItemNBTHelper.getLong(stack, "agesilicon", -1);
    		if(age>-1){
    			float prct = (float)Math.round( ((1 - (float)((float)age-(float)ItemNBTHelper.getLong(stack, "worldtime", age)) / (float)ModConfig.pureSiliconLifeSpan) * 10)) / 10;
    			int c = Math.round(colorPrct.length*prct);
    			if(c%2==0)
    				c=0;
    			else
    				c=1;
    	        return c<colorPrct.length?colorPrct[c]:14737632;
    		}
	        return 14737632;
		}
        return 16777215;
    }

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.getItemDamage()==2){
			list.add("Decay : "+(ItemNBTHelper.getLong(stack, "agesilicon", -1)-ItemNBTHelper.getLong(stack, "worldtime",-1)));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		return this.metaIcons[metadata];
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
        this.metaIcons = new IIcon[this.typeSilicon.length];
		for (int i = 0; i < metaIcons.length; i++) {
			metaIcons[i] = iconRegister.registerIcon(References.MODID + ":silicon_" + this.typeSilicon[i]);
		}
	}
	
	@Override
    public int getMetadata(int meta){
		return meta;
    }
    
}
