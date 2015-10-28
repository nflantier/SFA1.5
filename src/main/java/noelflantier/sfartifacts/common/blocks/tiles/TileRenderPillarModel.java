package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRenderPillarModel;

public class TileRenderPillarModel extends TileEntity{

	public int isRenderingPillarModel = -1;
	
	public TileRenderPillarModel(){
		
	}
	
    @Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
        	return;       
        
        PacketHandler.sendToAllAround(new PacketRenderPillarModel(this), this);
    }
	
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isRenderingPillarModel = nbt.getInteger("isRenderingPillarModel");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("isRenderingPillarModel", this.isRenderingPillarModel);
    }
}
