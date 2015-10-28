package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemSFA;

public interface IItemHasModes{
	
	List<ItemMode> getModes();
	
	default void addMode(String nameNbt, String name, String desc){
		getModes().add(new ItemMode(nameNbt, name, desc));
	}	
	
	default void changeMode(ItemStack stack, EntityPlayer player){
		int m = ItemNBTHelper.getInteger(stack, "Mode", 0);
		int nme = m;
		if(m+1>=getModes().size())
			nme = 0;
		else
			nme = m+1;
		
		player.addChatComponentMessage(new ChatComponentText(this.getStringChat()+""+getModes().get(nme).name));
		ItemNBTHelper.setInteger(stack, "Mode", nme);
	}

	ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player);
	double getDistanceRay();
	boolean shouldSneak();
	String getStringChat();
	void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4);
}
