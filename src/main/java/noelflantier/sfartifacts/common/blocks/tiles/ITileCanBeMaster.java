package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.common.helpers.Coord4;

public interface ITileCanBeMaster extends ITileMustHaveMaster{
    List<Coord4> getChildsList();
}
