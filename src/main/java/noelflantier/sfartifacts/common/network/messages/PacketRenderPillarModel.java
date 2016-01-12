package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;

public class PacketRenderPillarModel implements IMessage, IMessageHandler<PacketRenderPillarModel, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int isRenderingPillarModel;
	
	public PacketRenderPillarModel(){
		
	}
	
	public PacketRenderPillarModel(TileRenderPillarModel t){
		this.x = t.xCoord;
		this.y = t.yCoord;
		this.z = t.zCoord;
		this.isRenderingPillarModel = t.isRenderingPillarModel;
	}
	
	@Override
	public IMessage onMessage(PacketRenderPillarModel message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileRenderPillarModel) {
			TileRenderPillarModel me = (TileRenderPillarModel)te;
			me.isRenderingPillarModel = message.isRenderingPillarModel;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {	
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isRenderingPillarModel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(isRenderingPillarModel);
	}
}
