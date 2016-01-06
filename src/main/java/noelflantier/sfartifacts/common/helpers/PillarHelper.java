package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.ITileMaster;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketPillar;
import noelflantier.sfartifacts.common.network.messages.PacketUnsetPillar;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;

public class PillarHelper {
	
	public static boolean checkStructure(World w, EntityPlayer player, int x, int y, int z, boolean repass){
		Block originalb = w.getBlock(x, y, z);		
		for(String name : PillarsConfig.getInstance().nameOrderedBySize){
			if(PillarsConfig.getInstance().nameToPillar.containsKey(name)){
				Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
				if(p!=null){
					int xe = x-p.blockToActivate.x;
					int ye = y-p.blockToActivate.y;
					int ze = z-p.blockToActivate.z;
					if(p.checkStructure(w,xe,ye,ze,originalb)){
						return setupStructure(w,player,xe,ye,ze,PillarMaterials.getMaterialFromClass(originalb.getClass()).ID  ,name);
					}
				}
			}
		}

		return false;
	}

	public static boolean setupStructure(World w, EntityPlayer player, int x, int y, int z, int materialID, String namePillar){
		TileEntity t = w.getTileEntity(x, y, z);
		TileMasterPillar tmp;
		if(t!=null && t instanceof TileMasterPillar){
			tmp = (TileMasterPillar)t;
		}else if(t!=null && t instanceof TileRenderPillarModel){
			w.removeTileEntity(x, y, z);
			w.setBlockMetadataWithNotify(x, y, z, 3, 4);
			w.markBlockForUpdate(x, y, z);
			tmp = (TileMasterPillar)w.getTileEntity(x, y, z);
		}else{
			w.setBlockMetadataWithNotify(x, y, z, 3, 4);
			tmp = (TileMasterPillar)w.getTileEntity(x, y, z);
		}
    	tmp.isRenderingPillarModel = -1;
		tmp.namePillar = namePillar;
		tmp.materialId = materialID;
    	tmp.energyCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).energyCapacity;
		tmp.storage.setCapacity(tmp.energyCapacity);
		tmp.storage.setMaxTransfer(tmp.energyCapacity/tmp.ratioTransfer);
		tmp.tankCapacity = PillarsConfig.getInstance().getPillarFromName(namePillar).fluidCapacity;
		tmp.tank.setCapacity(tmp.tankCapacity);
    	PillarMaterials pm = PillarMaterials.getMaterialFromId(tmp.materialId);
    	tmp.setMaterialRatios(pm.energyRatio, pm.heightRatio, pm.rainRatio);
    	tmp.master = new Coord4(x,y,z);
    	if(t!=null && t instanceof TileMasterPillar){
    		tmp.storage.setEnergyStored(tmp.getEnergyStored(ForgeDirection.UNKNOWN));
    	}
    	tmp.init();
    	tmp.updateRenderTick = 2;
        w.markBlockForUpdate(x, y, z);
    	w.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    	
    	PillarsConfig.getInstance().getPillarFromName(namePillar).setupStructure(w,player,x,y,z);
    	if(player!=null)
    		player.addChatComponentMessage(new ChatComponentText(namePillar+" pillar structure created"));
		
		return true;
	}

	public static boolean recheckStructure(World w, int x, int y, int z, String name){
		Block originalb = w.getBlock(x, y, z);
		if(PillarsConfig.getInstance().nameToPillar.containsKey(name)){
			Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
			if(p!=null){
				return p.reCheckStructure(w,x,y,z,originalb);
			}
		}
		return false;
	}
	
	public static boolean unsetupStructureNoMaster(World w, int x, int y, int z){
		TileEntity t = w.getTileEntity(x, y, z);
		if(t instanceof ITileMaster && ((ITileMaster)t).getMasterCoord() == null)
			return false;
		
		if(t instanceof TileMasterPillar){
			((TileMasterPillar)t).master = null;
			int rf = ((TileMasterPillar)t).getEnergyStored(ForgeDirection.UNKNOWN);
			w.notifyBlockChange(x, y, z, w.getBlock(x, y, z));
			((TileMasterPillar)t).getEnergyStorage().setEnergyStored(rf);
			PacketHandler.sendToAllAround(new PacketUnsetPillar((TileMasterPillar)t),t);
		}else{
			Block b = w.getBlock(x, y, z);
			w.removeTileEntity(x, y, z);
			w.setBlockMetadataWithNotify(x, y, z, 0, 3);
    		w.setBlock(x, y, z, b);
			w.markBlockForUpdate(x, y, z);
		}
		return false;
	}
}
