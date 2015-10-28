package noelflantier.sfartifacts.common.network.messages;

import java.util.ArrayList;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;
import noelflantier.sfartifacts.common.helpers.SoundEmitterHelper;

public class PacketSoundEmitterGui implements IMessage, IMessageHandler<PacketSoundEmitterGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int frequency;
	public int mode;
	
	public PacketSoundEmitterGui(){
	
	}

	public PacketSoundEmitterGui(int x, int y, int z, int frequency, int mode){
		this.x = x;
		this.y = y;
		this.z = z;
		this.frequency = frequency;
		this.mode = mode;
	}
	
	
	@Override
	public IMessage onMessage(PacketSoundEmitterGui message, MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x,message.y, message.z);
		if(te instanceof TileSoundEmitter){
			if(message.mode==0){
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).frequencyEmited = message.frequency;
				((TileSoundEmitter)te).frequencyEmited = message.frequency;
			}
			if(message.mode==1){
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).frequencySelected = message.frequency;
				((TileSoundEmitter)te).frequencySelected = message.frequency;
			}
			if(message.mode==2){
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).isEmitting = false;
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).frequencyEmited = 0;
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).entitiesNameForSpawning = null;
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).mpForSpawning = null;
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).entityNameForSpawning = "";
				((TileSoundEmitter)te).isEmitting = false;
				((TileSoundEmitter)te).frequencyEmited = 0;
				((TileSoundEmitter)te).entitiesNameForSpawning = null;
				((TileSoundEmitter)te).mpForSpawning = null;
				((TileSoundEmitter)te).entityNameForSpawning = "";
			}
			if(message.mode==3){
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).isEmitting = true;
				((TileSoundEmitter)te).isEmitting = true;
			}
			if(message.mode==4){
				ArrayList<Integer> f = SoundEmitterHelper.getIdsForFrequency(message.frequency);
				String[] s = new String[f.size()];
				for(int i = 0;i<f.size();i++)
					s[i] = SoundEmitterHelper.spawningRulesIDForRules.get(f.get(i)).nameEntity;
				
				if(((TileSoundEmitter)te).listScannedFrequency.containsKey(message.frequency))
						((TileSoundEmitter)te).listScannedFrequency.remove(message.frequency);
				((TileSoundEmitter)te).listScannedFrequency.put(message.frequency, s);
				
				
				if(((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).listScannedFrequency.containsKey(message.frequency))
					((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).listScannedFrequency.remove(message.frequency);
				((TileSoundEmitter)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).listScannedFrequency.put(message.frequency, s);
			}
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    frequency = buf.readInt();
	    mode = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(frequency);
	    buf.writeInt(mode);
		
	}

}
