package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.EntityLightningBolt;

public class PacketLightning implements IMessage, IMessageHandler<PacketLightning, IMessage> {
	
	public int x;
	public int y;
	public int z;
	
	public PacketLightning(){
	
	}

	public PacketLightning(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(PacketLightning message, MessageContext ctx) {
		ctx.getServerHandler().playerEntity.worldObj.addWeatherEffect(new EntityLightningBolt(ctx.getServerHandler().playerEntity.worldObj, message.x, message.y+6, message.z));
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
