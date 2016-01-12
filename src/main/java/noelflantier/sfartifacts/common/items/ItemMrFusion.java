package noelflantier.sfartifacts.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemMrFusion extends ItemBlockSFA{
	
	public ItemMrFusion(Block block) {
		super(block, "Mr Fusion", true);
	}

	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add("1.21 GigaWatts");
	}
	
}
