package noelflantier.sfartifacts.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID);
	private static int ID = 0;
	
	public static int nextId(){
		return ID++;
	}
	public static void sendToAllAround(IMessage message, TileEntity entity){
    	INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(entity.getWorldObj().provider.dimensionId,entity.xCoord,entity.yCoord,entity.zCoord,64));
	}

	public static void sendToPlayerMP(IMessage message, EntityPlayerMP player){
    	INSTANCE.sendTo(message, player);
	}
	
	public static void sendToAllAround(IMessage message, World world,int x, int y, int z){
    	INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId,x,y,z,64));
	}
}
