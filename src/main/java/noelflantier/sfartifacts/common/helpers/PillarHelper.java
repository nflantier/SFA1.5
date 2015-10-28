package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;

public class PillarHelper {
	
	public static boolean checkStructure(World w, EntityPlayer player, int x, int y, int z, boolean repass){
		Block originalb = w.getBlock(x, y, z);
		boolean flag = true;
		int structureid = -1;
		int yd = 0;
		
		for(PillarStructures ps : PillarStructures.values()){
			flag = true;
			structureid = ps.ID;
			yd = y+ps.decY;
			String str ="0_0_0";
			do{
		    	String[] strParts = str.split("_");
		    	int xT = Integer.parseInt(strParts[0]) + x;
		    	int yT = Integer.parseInt(strParts[1]) + yd;
		    	int zT = Integer.parseInt(strParts[2]) + z;
				Block b = w.getBlock(xT, yT, zT);
				TileEntity t = w.getTileEntity(xT, yT, zT);

				if(b==null || b.getClass()!=originalb.getClass()){
					flag = false;
					break;
				}
				if(t!=null){
					if( ( t instanceof TileMasterPillar && ((TileMasterPillar)t).hasMaster() ) ){
						flag = false;
						break;
					}
					if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false){
						flag = false;
						break;
					}
				}
				
				str = (String)PillarStructures.getStructureFromId(ps.ID).structure.get(str);
			}while(!str.equals("end"));

			if(flag)
				break;
		}

		if(flag && !repass)
			return setupStructure(w,player,x,yd,z,PillarMaterials.getMaterialFromClass(originalb.getClass()).ID ,structureid);
		
		return flag;
	}
	
	public static boolean setupStructure(World w, EntityPlayer player, int x, int y, int z, int materialID, int structureID){
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
		tmp.structureId = structureID;
		tmp.materialId = materialID;
    	tmp.energyCapacity = PillarStructures.getStructureFromId(structureID).energyCapacity;
		tmp.storage.setCapacity(tmp.energyCapacity);
		tmp.storage.setMaxTransfer(tmp.energyCapacity/tmp.ratioTransfer);
		tmp.tankCapacity = PillarStructures.getStructureFromId(structureID).tankCapacity;
		tmp.tank.setCapacity(tmp.tankCapacity);
    	PillarMaterials pm = PillarMaterials.getMaterialFromId(tmp.materialId);
    	tmp.setMaterialRatio(pm.naturalEnergy, pm.maxHeightEfficiency, pm.rainEfficiency);
    	tmp.master = new Coord4(x,y,z);
    	if(t!=null && t instanceof TileMasterPillar){
    		tmp.storage.setEnergyStored(tmp.getEnergyStored(ForgeDirection.UNKNOWN));
    	}
    	tmp.init();
    	tmp.updateRenderTick = 2;
        w.markBlockForUpdate(x, y, z);
    	w.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        
		boolean flag = true;
		
		String str =(String)PillarStructures.getStructureFromId(structureID).structure.get("0_0_0");
		do{
	    	String[] strParts = str.split("_");
	    	int xT = Integer.parseInt(strParts[0]) + x;
	    	int yT = Integer.parseInt(strParts[1]) + y;
	    	int zT = Integer.parseInt(strParts[2]) + z;
	    	
	    	TileEntity tb = w.getTileEntity(xT, yT, zT);
	    	if(tb!=null && tb instanceof TileRenderPillarModel){
				w.removeTileEntity(xT, yT, zT);
				w.setBlockMetadataWithNotify(xT, yT, zT, 0, 4);
				w.markBlockForUpdate(xT, yT, zT);
	    	}
	    	
	    	if(PillarStructures.getStructureFromId(structureID).extract.get(str) != null 
	    			|| PillarStructures.getStructureFromId(structureID).recieve.get(str) != null){
	    		
	    		w.setBlockMetadataWithNotify(xT, yT, zT, 2, 4);
	    		TileEntity te = (TileEntity)w.getTileEntity(xT, yT, zT);
	    		TileInterfacePillar tip;
	        	if(te!=null && te instanceof TileInterfacePillar){
	        		tip = (TileInterfacePillar)te;
	        		tip.master = new Coord4(x,y,z);

		    		if(PillarStructures.getStructureFromId(structureID).extract.get(str) != null){
		    			Integer[] tab = PillarStructures.getStructureFromId(structureID).extract.get(str);
			            for(int k = 0 ; k<tab.length ; k++){
			            	tip.extractSides.add(ForgeDirection.getOrientation(tab[k]));
			            }
		    		}
		            if(PillarStructures.getStructureFromId(structureID).recieve.get(str) != null){
		            	Integer[] tab = PillarStructures.getStructureFromId(structureID).recieve.get(str);
		 	        	for(int k = 0 ; k<tab.length ; k++){
		 	        		tip.recieveSides.add(ForgeDirection.getOrientation(tab[k]));
		 	        	}
		            }
		            
	        		tip.updateRenderTick = 2;
		            tip.init();
		            tip.isRenderingPillarModel = -1;
	    		}
	    		
	    	}else{
	    		w.setBlockMetadataWithNotify(xT, yT, zT, 1, 4);
	    		TileEntity te = (TileEntity)w.getTileEntity(xT, yT, zT);
	    	   	TileBlockPillar tbp;
	        	if(te!=null && te instanceof TileBlockPillar){
	        		tbp = (TileBlockPillar)te;
		    		tbp.master = new Coord4(x,y,z);
		    		tbp.updateRenderTick = 2;
		    		tbp.isRenderingPillarModel = -1;
	    		}
	    	}
	    	//w.markBlockRangeForRenderUpdate(xT, yT, zT, xT, yT, zT);
            w.markBlockForUpdate(xT, yT, zT);
			str = (String)PillarStructures.getStructureFromId(structureID).structure.get(str);
		}while(!str.equals("end"));
		
    	if(player!=null)
    		player.addChatComponentMessage(new ChatComponentText("Pillar structure created"));
		
    	return true;
	}
	
	public static boolean recheckStructure(World w, int x, int y, int z, int structureid){
		Block originalb = w.getBlock(x, y, z);
		boolean flag = true;
		String str ="0_0_0";
		do{
	    	String[] strParts = str.split("_");
	    	int xT = Integer.parseInt(strParts[0]) + x;
	    	int yT = Integer.parseInt(strParts[1]) + y;
	    	int zT = Integer.parseInt(strParts[2]) + z;
			Block b = w.getBlock(xT, yT, zT);
			TileEntity t = w.getTileEntity(xT, yT, zT);
			
			if(b==null || b.getClass()!=originalb.getClass()){
				flag = false;
				break;
			}
			if(t!=null){
				if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false && t instanceof TileBlockPillar==false){
					flag = false;
					break;
				}
			}
			
			str = (String)PillarStructures.getStructureFromId(structureid).structure.get(str);
		}while(!str.equals("end"));
		
		return flag;
	}
	
	public static boolean unsetupStructureNoMaster(World w, int x, int y, int z){
		TileEntity t = w.getTileEntity(x, y, z);
		if(t instanceof TileMasterPillar){
			((TileMasterPillar)t).master = null;
			int rf = ((TileMasterPillar)t).getEnergyStored(ForgeDirection.UNKNOWN);
			w.notifyBlockChange(x, y, z, w.getBlock(x, y, z));
			((TileMasterPillar)t).getEnergyStorage().setEnergyStored(rf);
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
