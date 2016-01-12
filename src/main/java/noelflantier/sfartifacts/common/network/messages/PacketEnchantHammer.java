package noelflantier.sfartifacts.common.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class PacketEnchantHammer  implements IMessage, IMessageHandler<PacketEnchantHammer, IMessage> {
	public int x;
	public int y;
	public int z;
	public boolean fromStand;
	
	public int enchantId;
	public boolean enchantValue;
	
	public PacketEnchantHammer(){
		
	}
	
	public PacketEnchantHammer(TileHammerStand t, int enchantid, boolean enchantvalue, boolean fromstand){
		this.fromStand = fromstand;
		if(fromStand){
			this.x = t.xCoord;
			this.y = t.yCoord;
			this.z = t.zCoord;
		}
		
		this.enchantId = enchantid;
		this.enchantValue = enchantvalue;
	}

	@Override
	public IMessage onMessage(PacketEnchantHammer message, MessageContext ctx) {

		ItemStack hammer = new ItemStack(ModItems.itemThorHammer);
		
		if(message.fromStand){
			TileEntity t = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( message.x, message.y, message.z);
			if(t!=null && t instanceof TileHammerStand)
				hammer = ((TileHammerStand)t).items[0];
		}else if(!message.fromStand){
			hammer = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
		}

		if(hammer!=null){
			NBTTagList nbttaglist = hammer.stackTagCompound.getTagList("EnchStored", 10);
			hammer.getTagCompound().removeTag("ench");
	        if (nbttaglist != null){
	    		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		        {
		            if(nbttaglist.getCompoundTagAt(i).getShort("id")==message.enchantId){
		            	nbttaglist.getCompoundTagAt(i).setBoolean("enable",message.enchantValue);
		            }
		            if(nbttaglist.getCompoundTagAt(i).getBoolean("enable")){
		            	hammer.addEnchantment(Enchantment.enchantmentsList[nbttaglist.getCompoundTagAt(i).getShort("id")], nbttaglist.getCompoundTagAt(i).getInteger("lvl"));
		            }
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
	    fromStand = buf.readBoolean();
		this.enchantId = buf.readInt();
		this.enchantValue = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(fromStand);
	    buf.writeInt(enchantId);
	    buf.writeBoolean(enchantValue);
	}
}
