package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketInjector implements IMessage, IMessageHandler<PacketInjector, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRunning[];
	public int currentTickToInject[];
	public int tickToInject;
	
	public PacketInjector(){
	
	}

	public PacketInjector(TileInjector te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isRunning = te.isRunning.clone();
		this.currentTickToInject = te.currentTickToInject.clone();
		this.tickToInject = te.tickToInject;
	}
	
	@Override
	public IMessage onMessage(PacketInjector message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileInjector) {
			((TileInjector)te).isRunning = message.isRunning.clone();
			((TileInjector)te).currentTickToInject = message.currentTickToInject.clone();
			((TileInjector)te).tickToInject = message.tickToInject;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    int rl = buf.readInt();
	    isRunning = new boolean[rl];
	    for(int i =0;i<rl;i++)
	    	isRunning[i] = buf.readBoolean();
	    int ctl = buf.readInt();
	    currentTickToInject = new int[ctl];
	    for(int i =0;i<ctl;i++)
	    	currentTickToInject[i] = buf.readInt();
	    tickToInject = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(isRunning.length);
	    for(int i =0;i<isRunning.length;i++)
	    	buf.writeBoolean(isRunning[i]);
	    buf.writeInt(currentTickToInject.length);
	    for(int i =0;i<currentTickToInject.length;i++)
	    	buf.writeInt(currentTickToInject[i]);
	    buf.writeInt(tickToInject);
		
	}
}
