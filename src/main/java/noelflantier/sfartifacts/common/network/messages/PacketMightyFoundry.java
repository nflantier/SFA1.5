package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketMightyFoundry implements IMessage, IMessageHandler<PacketMightyFoundry, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isLocked;
	public double progression;
	
	public PacketMightyFoundry(){
	
	}

	public PacketMightyFoundry(TileMightyFoundry te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isLocked = te.isLocked;
		this.progression = te.progression;
	}
	
	@Override
	public IMessage onMessage(PacketMightyFoundry message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileMightyFoundry) {
			((TileMightyFoundry)te).progression = message.progression;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isLocked = buf.readBoolean();
	    progression = buf.readDouble();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isLocked);
	    buf.writeDouble(progression);
	}
}
