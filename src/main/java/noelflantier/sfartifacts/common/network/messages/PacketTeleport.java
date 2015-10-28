package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;

public class PacketTeleport implements IMessage, IMessageHandler<PacketTeleport, IMessage> {

	public String name = "";
	public String coord = "";
	public int idTask;
	public int idLink = -1;
	
	public PacketTeleport(){
	
	}

	public PacketTeleport(int idTask, String name, String coord){
		this.idTask = idTask;
		this.name = name;
		this.coord = coord;
	}

	public PacketTeleport(int idTask, int idLink){
		this.idTask = idTask;
		this.idLink = idLink;
	}

	public PacketTeleport(int idTask, String coord){
		this.idTask = idTask;
		this.coord = coord;
	}
	
	@Override
	public IMessage onMessage(PacketTeleport message, MessageContext ctx) {
		ItemStack hammer = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
		if(hammer==null || hammer.getItem() instanceof ItemThorHammer == false)
			return null;
		
		if(hammer!=null){
			switch(message.idTask){
				case 0 :
					if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
						hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());
					NBTTagList nbttaglist = hammer.stackTagCompound.getTagList("TeleportCoord", 10);
			        NBTTagCompound nbttagcompound = new NBTTagCompound();
			        nbttagcompound.setString("name", message.name);
			        nbttagcompound.setString("coord", message.coord);
			        nbttaglist.appendTag(nbttagcompound);
			        break;
				case 1 :
					NBTTagList nbttaglist1 = hammer.stackTagCompound.getTagList("TeleportCoord", 10);
					if(nbttaglist1.getCompoundTagAt(message.idLink)!=null)
						nbttaglist1.removeTag(message.idLink);
					break;
				case 2 :
					String[] st = message.coord.split(",");
					if(st.length==4){
						if (st[0].matches("[+-]?[0-9]+") && st[1].matches("[+-]?[0-9]+") && st[2].matches("[+-]?[0-9]+") && st[3].matches("[+-]?[0-9]+")){
							/*int dimid = Integer.parseInt(st[0]);
							double cx = ctx.getServerHandler().playerEntity.posX;
							double cy = ctx.getServerHandler().playerEntity.posY;
							double cz = ctx.getServerHandler().playerEntity.posZ;
							double tx = (double)Integer.parseInt(st[1]);
							double ty = (double)Integer.parseInt(st[2]);
							double tz = (double)Integer.parseInt(st[3]);
							EntityPlayerMP player = ctx.getServerHandler().playerEntity;
							
							if(!DimensionManager.isDimensionRegistered(dimid))
								return null;
							if(player.dimension!=dimid)
								player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimid);
							*/	
							ctx.getServerHandler().playerEntity.worldObj.playSoundEffect(ctx.getServerHandler().playerEntity.posX, ctx.getServerHandler().playerEntity.posY, ctx.getServerHandler().playerEntity.posZ, SoundHelper.PORTALTRAVEL.sound, 0.1F, ctx.getServerHandler().playerEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
							HammerHelper.startTeleporting(ctx.getServerHandler().playerEntity, st);
							ctx.getServerHandler().playerEntity.worldObj.playSoundEffect((double)Integer.parseInt(st[1]), (double)Integer.parseInt(st[2]), (double)Integer.parseInt(st[3]), SoundHelper.PORTALTRAVEL.sound, 0.1F, ctx.getServerHandler().playerEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
							
							/*ctx.getServerHandler().setPlayerLocation(tx, ty,tz, 0.0F, 0.0F);
							player.closeScreen();*/
							//ctx.getServerHandler().playerEntity.travelToDimension(Integer.parseInt(st[0]));
						}
					}
					break;
				default:
					break;
			}
		}
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		idTask = buf.readInt();
		idLink = buf.readInt();
	    int l = buf.readInt();
	    name = "";
	    for(int i = 0;i<l;i++)
	    	name = name+buf.readChar();
	    int l2 = buf.readInt();
	    coord = "";
	    for(int i = 0;i<l2;i++)
	    	coord = coord+buf.readChar();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(idTask);
	    buf.writeInt(idLink);
	    buf.writeInt(name.length());
	    for(int i=0;i<name.length();i++)
	    	buf.writeChar(name.charAt(i));
	    buf.writeInt(coord.length());
	    for(int i=0;i<coord.length();i++)
	    	buf.writeChar(coord.charAt(i));
	}

}
