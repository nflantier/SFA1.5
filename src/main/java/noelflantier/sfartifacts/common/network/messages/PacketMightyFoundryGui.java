package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketMightyFoundryGui implements IMessage, IMessageHandler<PacketMightyFoundryGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int idUpgrade;
	
	public PacketMightyFoundryGui(){
	
	}

	public PacketMightyFoundryGui(TileMightyFoundry te, int idUpgrade){
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.idUpgrade = idUpgrade;
	}
	
	@Override
	public IMessage onMessage(PacketMightyFoundryGui message, MessageContext ctx) {			
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileMightyFoundry) {
			switch(message.idUpgrade){
				case 1 :
					((TileMightyFoundry)te).isLocked = true;
					break;
				case 2 :
					((TileMightyFoundry)te).isLocked = false;
					break;
				default : 
					break;
			}
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    idUpgrade = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);		
	    buf.writeInt(idUpgrade);	
	}
}
