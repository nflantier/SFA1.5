package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.helpers.Coord4;

public interface ITileMaster {
	default int getMasterX(){
		return getMasterCoord().x;
	};
	default int getMasterY(){
		return getMasterCoord().y;
	};
	default int getMasterZ(){
		return getMasterCoord().z;
	};
    Coord4 getMasterCoord();
    World getWorld();
    
    default boolean hasMaster(){
    	return getMasterCoord()!=null;
    };
    default boolean isMasterHere(){
    	return getMasterTile()!=null;
    };
    default boolean isMasterStillMaster(){
    	return isMasterHere() && getMasterTile().hasMaster();
    };
    
    default public TileMasterPillar getMasterTile(){
    	TileEntity t = getWorld().getTileEntity(this.getMasterX(), this.getMasterY(), this.getMasterZ());
    	if(t!=null && t instanceof TileMasterPillar){
    		return (TileMasterPillar)t;
    	}
    	return null;
    }
}
