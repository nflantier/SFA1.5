package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;

public class PacketInductor implements IMessage, IMessageHandler<PacketInductor, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean canSend;
	public boolean canRecieve;
	public boolean canWSend;
	public boolean canWRecieve;
	
	public PacketInductor(){
	
	}

	public PacketInductor(TileInductor te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.canWRecieve = te.canWirelesslyRecieve;
		this.canWSend = te.canWirelesslySend;
		this.canRecieve = te.canRecieve;
		this.canSend = te.canSend;
	}
	
	@Override
	public IMessage onMessage(PacketInductor message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileInductor) {
			((TileInductor)te).canWirelesslyRecieve = message.canWRecieve;
			((TileInductor)te).canWirelesslySend = message.canWSend;
			((TileInductor)te).canRecieve = message.canRecieve;
			((TileInductor)te).canSend = message.canSend;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    canWRecieve = buf.readBoolean();
	    canWSend = buf.readBoolean();
	    canRecieve = buf.readBoolean();
	    canSend = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(canWRecieve);
	    buf.writeBoolean(canWSend);
	    buf.writeBoolean(canRecieve);
	    buf.writeBoolean(canSend);
		
	}
}