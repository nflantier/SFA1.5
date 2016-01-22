package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.ISFAEnergyHandler;

public class PacketEnergy  implements IMessage, IMessageHandler<PacketEnergy, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int energy;
	public int lastenergystored;
	
	public PacketEnergy(){
	
	}

	public PacketEnergy(int x, int y, int z, int energy){
		this.x = x;
		this.y = y;
		this.z = z;
		this.energy = energy;
		this.lastenergystored = -1;
	}

	public PacketEnergy(int x, int y, int z, int energy, int lastenergystored){
		this.x = x;
		this.y = y;
		this.z = z;
		this.energy = energy;
		this.lastenergystored = lastenergystored;
	}
	
	
	@Override
	public IMessage onMessage(PacketEnergy message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof ISFAEnergyHandler) {
			((ISFAEnergyHandler)te).getEnergyStorage().setEnergyStored(message.energy);
			((ISFAEnergyHandler)te).setLastEnergyStored(message.lastenergystored);
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    energy = buf.readInt();
	    lastenergystored = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(energy);
	    buf.writeInt(lastenergystored);
		
	}
}
