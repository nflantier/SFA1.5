package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileSFA  extends TileEntity implements IHasWailaContent{

    public String name;
    public int side = -1;
    public boolean init = false;
	public boolean isRedStoneEnable = false;
    
    public TileSFA(){
    }
    
    public TileSFA(String name){
    	this.name = name;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
        	return;
        if(!this.init)
        	this.init();
        this.isRedStoneEnable = this.worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0 ;
    }
    
    public void init(){
    	this.init = true;
    }

    public void setVariables(Object... params){
    	
    }
    
	@Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
    
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
	}
	
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("side", this.side);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.side = nbt.getInteger("side");
    }
}
