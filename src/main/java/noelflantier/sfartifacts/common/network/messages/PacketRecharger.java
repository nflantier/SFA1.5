package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;

public class PacketRecharger implements IMessage, IMessageHandler<PacketRecharger, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRecharging;
	
	public PacketRecharger(){
	
	}

	public PacketRecharger(TileRecharger te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isRecharging = te.isRecharging;
	}
	
	@Override
	public IMessage onMessage(PacketRecharger message, MessageContext ctx) {
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileRecharger) {
			((TileRecharger)te).isRecharging = message.isRecharging;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isRecharging = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isRecharging);
		
	}
}