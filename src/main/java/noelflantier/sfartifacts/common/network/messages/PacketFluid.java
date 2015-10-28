package noelflantier.sfartifacts.common.network.messages;

import java.util.List;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.blocks.tiles.ISFAFluid;

public class PacketFluid  implements IMessage, IMessageHandler<PacketFluid, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int quantity[];
	public int capacity[];
	public int fluidId[];
	
	public PacketFluid(){
	
	}

	public PacketFluid(int x, int y, int z, int[] quantity, int[] capacity, int[] fluidId){
		this.x = x;
		this.y = y;
		this.z = z;
		this.quantity = quantity.clone();
		this.capacity = capacity.clone();
		this.fluidId = fluidId.clone();
	}
	
	@Override
	public IMessage onMessage(PacketFluid message, MessageContext ctx) {			
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te!=null && te instanceof ISFAFluid) {
			List<FluidTank> ft = ((ISFAFluid)te).getFluidTanks();
			if(ft!=null){
				int i = 0;
				for(FluidTank f: ft){
					f.setFluid(new FluidStack(FluidRegistry.getFluid(message.fluidId[i]), message.quantity[i]));
					f.setCapacity(message.capacity[i]);
					i+=1;
				}
			}
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    int qt = buf.readInt();
	    this.quantity = new int[qt];
	    for(int i =0;i<qt;i++)
	    	this.quantity[i] = buf.readInt();
	    int cp = buf.readInt();
	    this.capacity = new int[cp];
	    for(int i =0;i<cp;i++)
	    	this.capacity[i] = buf.readInt();
	    int fi = buf.readInt();
	    this.fluidId = new int[fi];
	    for(int i =0;i<fi;i++)
	    	this.fluidId[i] = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(this.quantity.length);
	    for(int i =0;i<this.quantity.length;i++)
	    	buf.writeInt(this.quantity[i]);
	    buf.writeInt(this.capacity.length);
	    for(int i =0;i<this.capacity.length;i++)
	    	buf.writeInt(this.capacity[i]);
	    buf.writeInt(this.fluidId.length);
	    for(int i =0;i<this.fluidId.length;i++)
	    	buf.writeInt(this.fluidId[i]);
		
	}
}
