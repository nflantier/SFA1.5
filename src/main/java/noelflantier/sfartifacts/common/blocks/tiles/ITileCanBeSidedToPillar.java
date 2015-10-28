package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;

public interface ITileCanBeSidedToPillar extends ITileMaster{

    default public TileMasterPillar isSidedToPillar(int x, int y, int z){
    	TileEntity t = getWorld().getTileEntity(x, y, z);
    	if(t!=null && t instanceof ITileMustHaveMaster){
    		ITileMustHaveMaster itm = (ITileMustHaveMaster)t;
    		return itm.getMasterTile();
    	}
    	return null;
    }
}
