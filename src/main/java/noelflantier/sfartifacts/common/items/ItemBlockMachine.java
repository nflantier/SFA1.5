package noelflantier.sfartifacts.common.items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
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
    public String getUnlocalizedName(ItemStack itemstack){
    	int i = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+StringUtils.lowerCase(PillarMaterials.values()[i].name());
    }
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		/*Locale.setDefault(Locale.US);
		if(ItemNBTHelper.verifyExistance(stack, "Energy"))
			list.add("Energy : "+NumberFormat.getNumberInstance().format(ItemNBTHelper.getInteger(stack, "Energy", 0))+" RF");*/
	}
	
}
