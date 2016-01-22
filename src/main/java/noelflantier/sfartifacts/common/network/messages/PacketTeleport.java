package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
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
							SoundHelper.playPositionedSound(SoundHelper.Sounds.PORTALTRAVEL, Minecraft.getMinecraft(), ctx.getServerHandler().playerEntity.posX, ctx.getServerHandler().playerEntity.posY, ctx.getServerHandler().playerEntity.posZ, 0.1F);
		                	ctx.getServerHandler().playerEntity.worldObj.addWeatherEffect(new EntityLightningBolt(ctx.getServerHandler().playerEntity.worldObj, ctx.getServerHandler().playerEntity.posX+3, ctx.getServerHandler().playerEntity.posY, ctx.getServerHandler().playerEntity.posZ+3));
							//ctx.getServerHandler().playerEntity.worldObj.playSoundEffect(ctx.getServerHandler().playerEntity.posX, ctx.getServerHandler().playerEntity.posY, ctx.getServerHandler().playerEntity.posZ, SoundHelper.Sounds.PORTALTRAVEL.sound, 0.1F, ctx.getServerHandler().playerEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
							HammerHelper.startTeleporting(ctx.getServerHandler().playerEntity, st);
							SoundHelper.playPositionedSound(SoundHelper.Sounds.PORTALTRAVEL, Minecraft.getMinecraft(), (double)Integer.parseInt(st[1]), (double)Integer.parseInt(st[2]), (double)Integer.parseInt(st[3]), 0.1F);

		            		HammerHelper.extractEnergyInHammer(hammer,((ItemThorHammer)hammer.getItem()).energyTeleporting);
							//ctx.getServerHandler().playerEntity.worldObj.playSoundEffect((double)Integer.parseInt(st[1]), (double)Integer.parseInt(st[2]), (double)Integer.parseInt(st[3]), SoundHelper.Sounds.PORTALTRAVEL.sound, 0.1F, ctx.getServerHandler().playerEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);

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
