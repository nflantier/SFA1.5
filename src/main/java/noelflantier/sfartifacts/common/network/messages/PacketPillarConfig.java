package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;

public class PacketPillarConfig  implements IMessage, IMessageHandler<PacketPillarConfig, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int amountToExtract;
	
	public PacketPillarConfig(){
	
	}

	public PacketPillarConfig(int x, int y, int z, int amountToExtract){
		this.x = x;
		this.y = y;
		this.z = z;
		this.amountToExtract = amountToExtract;
	}
	
	@Override
	public IMessage onMessage(PacketPillarConfig message, MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( message.x, message.y, message.z);
		if(te!=null && te instanceof TileMasterPillar) {
			((TileMasterPillar)te).amountToExtract = ((TileMasterPillar)te).amountToExtract+message.amountToExtract>1000? 1000:((TileMasterPillar)te).amountToExtract+message.amountToExtract<0?0:((TileMasterPillar)te).amountToExtract+message.amountToExtract;
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    amountToExtract = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(amountToExtract);
		
	}
}
