package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;

public class PacketLightningRodStand implements IMessage, IMessageHandler<PacketLightningRodStand, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int lightningRodEnergy;
	
	public PacketLightningRodStand(){
		
	}
	
	public PacketLightningRodStand(TileLightningRodStand t){
		this.x = t.xCoord;
		this.y = t.yCoord;
		this.z = t.zCoord;
		this.lightningRodEnergy = t.lightningRodEnergy;
	}
	
	@Override
	public IMessage onMessage(PacketLightningRodStand message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileLightningRodStand) {
			TileLightningRodStand me = (TileLightningRodStand)te;
			me.lightningRodEnergy = message.lightningRodEnergy;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {	
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    lightningRodEnergy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(lightningRodEnergy);
	}
}
