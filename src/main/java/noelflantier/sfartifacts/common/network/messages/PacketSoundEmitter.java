package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;

public class PacketSoundEmitter implements IMessage, IMessageHandler<PacketSoundEmitter, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isEmitting;
	
	public PacketSoundEmitter(){
	
	}

	public PacketSoundEmitter(TileSoundEmitter te){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.isEmitting = te.isEmitting;
	}
	
	@Override
	public IMessage onMessage(PacketSoundEmitter message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileSoundEmitter) {
			((TileSoundEmitter)te).isEmitting = message.isEmitting;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isEmitting = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isEmitting);	
	}
}

