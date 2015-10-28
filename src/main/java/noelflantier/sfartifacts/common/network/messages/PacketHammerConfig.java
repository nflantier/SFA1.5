package noelflantier.sfartifacts.common.network.messages;

import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketHammerConfig  implements IMessage, IMessageHandler<PacketHammerConfig, IMessage>{

	public int idGui;
	
	public PacketHammerConfig(){
		
	}

	public PacketHammerConfig(int id){
		this.idGui = id;
	}
	
	@Override
	public IMessage onMessage(PacketHammerConfig message, MessageContext ctx) {
		ctx.getServerHandler().playerEntity.openGui(SFArtifacts.instance, message.idGui, ctx.getServerHandler().playerEntity.worldObj, (int)ctx.getServerHandler().playerEntity.posX, (int)ctx.getServerHandler().playerEntity.posY, (int)ctx.getServerHandler().playerEntity.posZ);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	    this.idGui = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(this.idGui);	
	}

}
