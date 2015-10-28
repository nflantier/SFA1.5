package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class ItemManual extends ItemSFA{

	public ItemManual() {
		super("Manual");
		this.setMaxStackSize(1);
		this.setUnlocalizedName("itemManual");
		this.setTextureName(References.MODID+":manual");
	}
	
	@Override
    public ItemStack onItemRightClick (ItemStack stack, World w, EntityPlayer player){
			player.openGui(SFArtifacts.instance, ModGUIs.guiIDManual, w, (int)player.posX, (int)player.posY, (int)player.posZ);
        return stack;
    }
}
