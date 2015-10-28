package noelflantier.sfartifacts.common.network.messages;

import net.minecraft.client.Minecraft;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketInvokStarting implements IMessage, IMessageHandler<PacketInvokStarting, IMessage> {
		
		public int x;
		public int y;
		public int z;
		
		public PacketInvokStarting(){
		
		}

		public PacketInvokStarting(TileHammerStand t){
			this.x = t.xCoord;
			this.y = t.yCoord;
			this.z = t.zCoord;
		}
		
		@Override
		public IMessage onMessage(PacketInvokStarting message, MessageContext ctx) {
			ctx.getServerHandler().playerEntity.closeScreen();
			HammerHelper.startInvoking(ctx.getServerHandler().playerEntity.worldObj,ctx.getServerHandler().playerEntity, message.x, message.y, message.z);
            return null;
		}
		
		@Override
		public void fromBytes(ByteBuf buf) {
		    x = buf.readInt();
		    y = buf.readInt();
		    z = buf.readInt();
			
		}
		@Override
		public void toBytes(ByteBuf buf) {
		    buf.writeInt(x);
		    buf.writeInt(y);
		    buf.writeInt(z);
			
		}

	}
