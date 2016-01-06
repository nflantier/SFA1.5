package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class PacketPillarGui  implements IMessage, IMessageHandler<PacketPillarGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int amountToExtract;
	
	public PacketPillarGui(){
	
	}

	public PacketPillarGui(int x, int y, int z, int amountToExtract){
		this.x = x;
		this.y = y;
		this.z = z;
		this.amountToExtract = amountToExtract;
	}
	
	@Override
	public IMessage onMessage(PacketPillarGui message, MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( message.x, message.y, message.z);
		if(te!=null && te instanceof TileMasterPillar) {
			((TileMasterPillar)te).amountToExtract = ((TileMasterPillar)te).amountToExtract+message.amountToExtract>ModConfig.maxAmountPillarCanExtract? 
					ModConfig.maxAmountPillarCanExtract:((TileMasterPillar)te).amountToExtract+message.amountToExtract<0?0:((TileMasterPillar)te).amountToExtract+message.amountToExtract;
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
