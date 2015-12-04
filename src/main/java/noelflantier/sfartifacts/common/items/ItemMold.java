package noelflantier.sfartifacts.common.items;

import io.netty.channel.embedded.EmbeddedChannel;

import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.internal.FMLMessage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.client.gui.GuiMoldMaking;
import noelflantier.sfartifacts.common.container.ContainerMoldMaking;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Molds;
import noelflantier.sfartifacts.common.items.baseclasses.ItemInventoryMold;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class ItemMold extends ItemSFA{

	protected IIcon[] icons;
	public static String[] typeMolds = new String[] {"empty","filled"};
	
	public ItemMold() {
		super("Mold");
		this.setUnlocalizedName("itemMold");
		//this.setTextureName(References.MODID+":mold_empty");
		this.setMaxStackSize(16);
		this.hasSubtypes = true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		return this.icons[metadata];
	}

	@Override
    public int getMetadata(int meta){
		return meta;
    }
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {

        this.icons = new IIcon[this.typeMolds.length];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = iconRegister.registerIcon(References.MODID + ":mold_" + this.typeMolds[i]);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player){
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDMold, w, (int)player.posX, (int)player.posY, (int)player.posZ);
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		ItemStack it1 = new ItemStack(item, 1, 0);
		it1.setItemDamage(0);
		it1 = ItemNBTHelper.setInteger(it1, "idmold", 0);//0 nothing   -1 invalid    other>0 valid
		it1 = ItemNBTHelper.setIntegerArray(it1, "moldstructure", new int[]{0,0,0,0,0,0,0,0,0});
		list.add(it1);
		for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			int m = RecipeMold.class.cast(entry.getValue()).getMoldMeta();
			ItemStack it0 = new ItemStack(item, 1, m);
			it0.setItemDamage(m);
			it0 = ItemNBTHelper.setInteger(it0, "idmold", m);
			it0 = ItemNBTHelper.setIntegerArray(it0, "moldstructure", RecipeMold.class.cast(entry.getValue()).getTabShape().clone());
			list.add(it0);
		}
	}


	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
		if(player.openContainer.windowId==ModGUIs.guiIDMold){
			return false;
		}
		return true;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		int tid = ItemNBTHelper.getInteger(stack, "idmold", 0);
		if(tid==-1)
			list.add(String.format("Mold : Invalid"));
		else if(tid==0)
			list.add(String.format("Mold : Empty"));
		else if(tid>0)
			list.add(String.format("Mold : "+RecipesRegistry.instance.getNameWithMeta(tid)));
	}

}
