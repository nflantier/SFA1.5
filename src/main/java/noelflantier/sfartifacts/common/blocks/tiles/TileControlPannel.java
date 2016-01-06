package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.helpers.Coord4;

public class TileControlPannel extends TileSFA implements ITileCanBeSidedToPillar {

	public Coord4 master;
    public int tickCheck = 20;
	
	public TileControlPannel(){
		super("Control Panel");
	}
	
	@Override
    public void preinit(){
		super.preinit();
		checkMaster();
    }
	
	public void checkMaster(){
		ForgeDirection fd = ForgeDirection.getOrientation(this.side).getOpposite();
		TileMasterPillar t = this.isSidedToPillar(this.xCoord+fd.offsetX,this.yCoord+fd.offsetY, this.zCoord+fd.offsetZ);
		if(t!=null)
			this.master = new Coord4(t.xCoord,t.yCoord,t.zCoord);
		else
			this.master = null;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public Coord4 getMasterCoord() {
		return this.master;
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
		if(this.hasMaster())
			list.add("Pillar at : "+this.master.x+", "+this.master.y+", "+this.master.z);
		else
			list.add("Not connected to a pillar");	
	}
}
