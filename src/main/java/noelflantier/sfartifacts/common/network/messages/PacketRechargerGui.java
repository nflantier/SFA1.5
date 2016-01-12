package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;

public class PacketRechargerGui implements IMessage, IMessageHandler<PacketRechargerGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean wirelessRechargingEnable;
	
	public PacketRechargerGui(){
	
	}

	public PacketRechargerGui(TileRecharger te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.wirelessRechargingEnable = te.wirelessRechargingEnable;
	}
	
	@Override
	public IMessage onMessage(PacketRechargerGui message, MessageContext ctx) {			
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileRecharger) {
			((TileRecharger)te).wirelessRechargingEnable = message.wirelessRechargingEnable;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    wirelessRechargingEnable = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(wirelessRechargingEnable);
		
	}

}
