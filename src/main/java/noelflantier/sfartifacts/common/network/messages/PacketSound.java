package noelflantier.sfartifacts.common.network.messages;

import net.minecraft.client.Minecraft;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSound implements IMessage, IMessageHandler<PacketSound, IMessage> {

	public int x;
	public int y;
	public int z;
	public int sound;

	public PacketSound(){
	
	}

	public PacketSound(int x, int y, int z, int sound){
		this.x = x;
		this.y = y;
		this.z = z;
		this.sound = sound;
	}
	
	@Override
	public IMessage onMessage(PacketSound message, MessageContext ctx) {
		Minecraft.getMinecraft().theWorld.playSound((double)message.x, (double)message.y, (double)message.z,  SoundHelper.values()[message.sound].sound,  5.0F, 0.8F + 0.1F * 0.3F, false);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    sound = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(sound);
	}

}
