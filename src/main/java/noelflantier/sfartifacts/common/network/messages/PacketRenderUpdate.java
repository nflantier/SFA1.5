package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class PacketRenderUpdate implements IMessage, IMessageHandler<PacketRenderUpdate, IMessage> {
	
	public int x;
	public int y;
	public int z;
	
	public PacketRenderUpdate(){
	
	}

	public PacketRenderUpdate(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(PacketRenderUpdate message, MessageContext ctx) {
		Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(message.x, message.y,message.z,message.x,message.y,message.z);
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
		
	}
}