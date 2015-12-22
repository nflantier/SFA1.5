package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;

public class PacketInductorGui  implements IMessage, IMessageHandler<PacketInductorGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int type;
	public boolean typevalue;
	
	public PacketInductorGui(){
	
	}

	public PacketInductorGui(TileInductor te, int type, boolean tv){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.type = type;
		this.typevalue = tv;
	}
	
	@Override
	public IMessage onMessage(PacketInductorGui message, MessageContext ctx) {			
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileInductor) {
			switch(message.type){
				case 0 :
					((TileInductor)te).canWirelesslySend = message.typevalue;
					break;
				case 1 :
					((TileInductor)te).canWirelesslyRecieve = message.typevalue;
					break;
				case 2 :
					((TileInductor)te).canSend = message.typevalue;
					break;
				case 3 :
					((TileInductor)te).canSend = message.typevalue;
					break;
				default:
					break;
			}
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    type = buf.readInt();
	    typevalue = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(type);
	    buf.writeBoolean(typevalue);
		
	}
}