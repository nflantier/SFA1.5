package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileMachine;

public class PacketMachine implements IMessage, IMessageHandler<PacketMachine, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isManualyEnable;
	
	public PacketMachine(){
	
	}

	public PacketMachine(TileMachine te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isManualyEnable = te.isManualyEnable;
	}
	
	@Override
	public IMessage onMessage(PacketMachine message, MessageContext ctx) {		
		TileEntity t = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( message.x, message.y, message.z);	
		if(t!=null && t instanceof TileMachine) {
			((TileMachine)t).isManualyEnable = message.isManualyEnable;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isManualyEnable = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isManualyEnable);
		
	}
}
