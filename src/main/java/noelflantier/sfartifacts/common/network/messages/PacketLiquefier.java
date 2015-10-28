package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;

public class PacketLiquefier implements IMessage, IMessageHandler<PacketLiquefier, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRunning;
	public int currentTickToMelt;
	public int tickToMelt;
	
	public PacketLiquefier(){
	
	}

	public PacketLiquefier(TileLiquefier te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isRunning = te.isRunning;
		this.currentTickToMelt = te.currentTickToMelt;
		this.tickToMelt = te.tickToMelt;
	}
	
	@Override
	public IMessage onMessage(PacketLiquefier message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileLiquefier) {
			((TileLiquefier)te).isRunning = message.isRunning;
			((TileLiquefier)te).currentTickToMelt = message.currentTickToMelt;
			((TileLiquefier)te).tickToMelt = message.tickToMelt;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isRunning = buf.readBoolean();
	    currentTickToMelt = buf.readInt();
	    tickToMelt = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isRunning);
	    buf.writeInt(currentTickToMelt);
	    buf.writeInt(tickToMelt);
		
	}
}
