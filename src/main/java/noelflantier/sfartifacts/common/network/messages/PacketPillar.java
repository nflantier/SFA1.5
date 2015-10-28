package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;

public class PacketPillar implements IMessage, IMessageHandler<PacketPillar, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int amountToExtract;
	public int passiveEnergy;
	public int fluidEnergy;
	public int isRenderingPillarModel;
	
	public PacketPillar(){
		
	}
	
	public PacketPillar(TileMasterPillar t){
		this.x = t.xCoord;
		this.y = t.yCoord;
		this.z = t.zCoord;
		this.amountToExtract = t.amountToExtract;
		this.passiveEnergy = t.passiveEnergy;
		this.fluidEnergy = t.fluidEnergy;
	}
	
	@Override
	public IMessage onMessage(PacketPillar message, MessageContext ctx) {	
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof TileMasterPillar) {
			TileMasterPillar me = (TileMasterPillar)te;
			me.amountToExtract = message.amountToExtract;
			me.passiveEnergy = message.passiveEnergy;
			me.fluidEnergy = message.fluidEnergy;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {	
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    amountToExtract = buf.readInt();
	    passiveEnergy = buf.readInt();
	    fluidEnergy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(amountToExtract);	
	    buf.writeInt(passiveEnergy);
	    buf.writeInt(fluidEnergy);
	}
}