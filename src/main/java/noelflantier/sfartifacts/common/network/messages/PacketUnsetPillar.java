package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;

public class PacketUnsetPillar  implements IMessage, IMessageHandler<PacketUnsetPillar, IMessage> {
	
	public int x;
	public int y;
	public int z;
	
	public PacketUnsetPillar(){
		
	}
	
	public PacketUnsetPillar(TileMasterPillar t){
		this.x = t.xCoord;
		this.y = t.yCoord;
		this.z = t.zCoord;
	}
	
	@Override
	public IMessage onMessage(PacketUnsetPillar message, MessageContext ctx) {	
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileMasterPillar) {
			((TileMasterPillar)te).master = null;
		}
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
