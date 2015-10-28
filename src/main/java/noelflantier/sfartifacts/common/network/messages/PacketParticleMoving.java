package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class PacketParticleMoving  implements IMessage, IMessageHandler<PacketParticleMoving, IMessage> {
	
	public double xM;
	public double yM;
	public double zM;
	public double xP;
	public double yP;
	public double zP;
	
	public PacketParticleMoving(){
	
	}

	public PacketParticleMoving(double xm, double ym, double zm, double xp, double yp, double zp){
		this.xM = xm;
		this.yM = ym;
		this.zM = zm;
		this.xP = xp;
		this.yP = yp;
		this.zP = zp;
	}
	
	@Override
	public IMessage onMessage(PacketParticleMoving message, MessageContext ctx) {			
		ParticleHelper.spawnCustomParticle(ParticleHelper.Type.ENERGYFLOW, (double)message.xM+0.5, (double)message.yM+0.5, (double)message.zM+0.5, new Object[]{(double)message.xP+0.5, (double)message.yP+0.5, (double)message.zP+0.5,1});
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    xM = buf.readDouble();
	    yM = buf.readDouble();
	    zM = buf.readDouble();
	    xP = buf.readDouble();
	    yP = buf.readDouble();
	    zP = buf.readDouble();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeDouble(xM);
	    buf.writeDouble(yM);
	    buf.writeDouble(zM);
	    buf.writeDouble(xP);
	    buf.writeDouble(yP);
	    buf.writeDouble(zP);
		
	}
}
