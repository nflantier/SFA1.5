package noelflantier.sfartifacts.common.blocks.tiles.pillar;

import java.text.NumberFormat;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.ITileMustHaveMaster;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.helpers.PillarStructures;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRenderUpdate;

public class TileBlockPillar  extends TileSFA implements ITileMustHaveMaster{

	public Coord4 master;
    public int updateRenderTick = -1;
	public int isRenderingPillarModel = -1;
    
	public TileBlockPillar(){
    }
    
    public TileBlockPillar(String name){
    	super(name);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
        	return;
        checkRenderTick();
    }

    public void checkRenderTick(){
        if(this.updateRenderTick>0){
        	this.updateRenderTick-=1;
        }
        if(this.updateRenderTick==0){
            PacketHandler.sendToAllAround(new PacketRenderUpdate(this.xCoord,this.yCoord,this.zCoord), this);
        	this.updateRenderTick=-1;
        }
    }

	@Override
	public Coord4 getMasterCoord() {
		return master;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if(this.hasMaster()){
	        nbt.setInteger("masterx", this.getMasterX());
	        nbt.setInteger("mastery", this.getMasterY());
	        nbt.setInteger("masterz", this.getMasterZ());
        	nbt.setBoolean("hasmaster", true);
        }else
        	nbt.setBoolean("hasmaster", false);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

    	if(nbt.getBoolean("hasmaster"))
    		this.master = new Coord4(nbt.getInteger("masterx"),nbt.getInteger("mastery"),nbt.getInteger("masterz"));
    }

	@Override
	public void addToWaila(List<String> list) {
		if(this.hasMaster()){
			list.add("Master at : "+this.master.x+", "+this.master.y+", "+this.master.z);
			TileMasterPillar m = this.getMasterTile();
			list.add("Pillar : "+PillarStructures.getStructureFromId(m.structureId));
			list.add("Material : "+PillarMaterials.getMaterialFromId(m.materialId));
			list.add("Energy : "+NumberFormat.getNumberInstance().format(m.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(m.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
			if(m.getFluidTanks().get(0)!=null)
				list.add("Liquefied Asgardite : "+NumberFormat.getNumberInstance().format(m.getFluidTanks().get(0).getFluidAmount())+" MB / "+NumberFormat.getNumberInstance().format(m.getFluidTanks().get(0).getCapacity())+" MB");
		}
	}

}
