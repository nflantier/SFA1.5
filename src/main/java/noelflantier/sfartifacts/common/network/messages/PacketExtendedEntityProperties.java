package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModPlayerStats;

public class PacketExtendedEntityProperties implements IMessage, IMessageHandler<PacketExtendedEntityProperties, IMessage> {
	
	 private NBTTagCompound data;

	public PacketExtendedEntityProperties(){
	}
	
	public PacketExtendedEntityProperties(EntityPlayer player){ 
		data = new NBTTagCompound();
		ModPlayerStats.get(player).saveNBTData(data);
	}
	
	@Override
	public IMessage onMessage(PacketExtendedEntityProperties message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			ModPlayerStats.get(SFArtifacts.myProxy.getPlayerEntity(ctx)).loadNBTData(message.data);
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    data = ByteBufUtils.readTag(buf);
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    ByteBufUtils.writeTag(buf, data);
		
	}
}
