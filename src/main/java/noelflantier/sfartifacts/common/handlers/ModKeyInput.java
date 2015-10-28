package noelflantier.sfartifacts.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.baseclasses.ToolHammerBase;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketHammerConfig;

public class ModKeyInput {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(ModKeyBindings.hammerConfig.isPressed()) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ToolHammerBase && ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "CanBeConfigByHand", false)){
				PacketHandler.INSTANCE.sendToServer(new PacketHammerConfig(ModGUIs.guiIDHammerConfig));
				player.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerConfig, Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}
	}
}
