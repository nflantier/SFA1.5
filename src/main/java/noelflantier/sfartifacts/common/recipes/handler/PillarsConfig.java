package noelflantier.sfartifacts.common.recipes.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.Utils;

public class PillarsConfig {
	static final PillarsConfig instance = new PillarsConfig();
	private static final String CORE_FILE_NAME = "pillarsConfig.json";

	private static final String KEY_PILLARS = "pillars";
	private static final String KEY_ENERGY_CAPACITY = "energycapacity";
	private static final String KEY_FLUID_CAPACITY = "fluidcapacity";
	private static final String KEY_NATURAL_RATIO = "naturalratio";
	private static final String KEY_STRUCTURE = "structure";
	
	
	private static final String P_NORMAL_BLOCK = "block";
	private static final String P_INTERFACE = "interface";
	private static final String P_BLOCK_ACTIVATE = "blockactivate";
	private static final String P_MASTER = "master";
	
	public static PillarsConfig getInstance() {
		return instance;
	}
	
	public Map<String, Pillar> nameToPillar = new HashMap<String, Pillar>();
	public List<String> nameOrderedBySize;

	private PillarsConfig(){
		String configText;
		JsonElement root;
		JsonObject rootObj;
		JsonObject pillarsObj;

		try {
			configText = Utils.getFileFromConfig(CORE_FILE_NAME, false);
			root = new JsonParser().parse(configText);
			rootObj = root.getAsJsonObject();
			pillarsObj = rootObj.getAsJsonObject(KEY_PILLARS);

			for (Entry<String, JsonElement> entry : pillarsObj.entrySet()) {
				String name = entry.getKey();
				if(name==null || name.equals("")){
					System.out.println("Pillar name cant be null");
					continue;
				}
				if(nameToPillar.containsKey(name)){
					System.out.println("Pillar "+name+" allready registered");
					continue;
				}
				processPillar(name, entry.getValue().getAsJsonObject());
			}
			nameOrderedBySize = new ArrayList<String>(nameToPillar.size());
			for(Entry<String, Pillar> entry : nameToPillar.entrySet()){
				orderMap(entry.getValue());
			}
			for(String s : nameOrderedBySize){
				System.out.println("........................ "+s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void orderMap( Pillar el){
		int idx = 0;
		if(nameOrderedBySize.size()<=0){
			nameOrderedBySize.add(el.name);
			return;
		}
		for(String s : nameOrderedBySize){
			if(el.mapStructure.size()<nameToPillar.get(s).mapStructure.size())
				idx+=1;
		}
		nameOrderedBySize.add(idx, el.name);
	}
	
	private void processPillar(String name, JsonObject pi) {
		boolean flag = false;
		Pillar p = new Pillar(name);
		if(pi.has(KEY_ENERGY_CAPACITY))
			p.energyCapacity = pi.get(KEY_ENERGY_CAPACITY).getAsInt();
		else
			flag = true;
		if(pi.has(KEY_FLUID_CAPACITY))
			p.fluidCapacity = pi.get(KEY_FLUID_CAPACITY).getAsInt();
		else
			flag = true;
		if(pi.has(KEY_NATURAL_RATIO))
			p.naturalRatio = pi.get(KEY_NATURAL_RATIO).getAsFloat();
		else
			flag = true;
		if(!flag){
			boolean havemaster = false;
			boolean haveblocktoactivate = false;
			List<Coord4> ms = new ArrayList<Coord4>();
			Map<String,String> inte = new HashMap<String,String>();
			for (Entry<String, JsonElement> entry : pi.get(KEY_STRUCTURE).getAsJsonObject().entrySet()) {
				int[] c = getXYZ(entry.getKey(), "_");
				Coord4 co = new Coord4(c);
				ms.add(co);
				if(P_BLOCK_ACTIVATE.equals(entry.getValue().getAsString())){
					p.blockToActivate = co;
					haveblocktoactivate = true;
				}else if(P_MASTER.equals(entry.getValue().getAsString())){
					p.blockMaster = co;
					havemaster = true;
				}else if(P_NORMAL_BLOCK.equals(entry.getValue().getAsString())){
					
				}else if(entry.getValue().getAsString().startsWith(P_INTERFACE)){
					String[] separated = entry.getValue().getAsString().split("[:]");
					if(separated!=null && separated.length>1)
						inte.put(entry.getKey(), separated[1]);
				}
			}
			
			if(!havemaster){
				System.out.println("Pillar "+name+" dont have any master");
				flag = true;
			}else{
				if(!haveblocktoactivate){
					haveblocktoactivate = true;
					p.blockToActivate = new Coord4(p.blockMaster.x, p.blockMaster.y, p.blockMaster.z);
				}
			}
			if(!flag && haveblocktoactivate){
				for(Coord4 co : ms){
					int nx = co.x + p.blockMaster.x*-1;
					int ny = co.y + p.blockMaster.y*-1;
					int nz = co.z + p.blockMaster.z*-1;
					p.mapStructure.put(nx+"_"+ny+"_"+nz, new Coord4(nx, ny, nz));
				}
				for(Entry<String, String> entry : inte.entrySet()){
					int[] c = getXYZ(entry.getKey(), "_");
					int ix = c[0] + p.blockMaster.x*-1;
					int iy = c[1] + p.blockMaster.y*-1;
					int iz = c[2] + p.blockMaster.z*-1;
					String[] t = entry.getValue().split("[,]");
					if(t!=null && t.length>=1){
						int[] ta = new int[t.length];
						for(int i = 0;i<t.length;i++)
							ta[i] = Integer.parseInt(t[i]);
						p.interfaces.put(ix+"_"+iy+"_"+iz,ta );
					}
				}
				p.blockToActivate.x += p.blockMaster.x*-1;
				p.blockToActivate.y += p.blockMaster.y*-1;
				p.blockToActivate.z += p.blockMaster.z*-1;
				p.blockMaster.x = 0;
				p.blockMaster.y = 0;
				p.blockMaster.z = 0;
			}
			//dd
			if(!flag)
				nameToPillar.put(name, p);
		}
	}
	
	public int[] getXYZ(String str, String sep){
		String[] separated = str.split("["+sep+"]");
		return new int[]{Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2])};
	}
	
	public Pillar getPillarFromName(String name){
		return nameToPillar.containsKey(name)?nameToPillar.get(name):null;
	}
	
	public class Pillar{
		public final String name;//UID
		public int ID;
		public int energyCapacity = 0;
		public int fluidCapacity = 0;
		public float naturalRatio = 1;
		public Map<String,Coord4> mapStructure = new HashMap<String, Coord4>();
		public Map<String,int[]> interfaces = new HashMap<String, int[]>();
		public Coord4 blockToActivate;
		public Coord4 blockMaster;
		
		public Pillar(String name){
			this.name = name;
		}

		public boolean checkStructure(World w, int x, int y, int z, Block originalb) {
			
			int size = mapStructure.size();
			for(Entry<String, Coord4> entry : mapStructure.entrySet()){
		    	int xT = entry.getValue().x + x;
		    	int yT = entry.getValue().y + y;
		    	int zT = entry.getValue().z + z;
				Block b = w.getBlock(xT, yT, zT);
				TileEntity t = w.getTileEntity(xT, yT, zT);
				if(b==null || b.getClass()!=originalb.getClass()){
					return false;
				}
				if(t!=null){
					if( ( t instanceof TileMasterPillar && ((TileMasterPillar)t).hasMaster() ) ){
						return false;
					}
					if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false){
						return false;
					}
				}
			}
			return true;
		}	
		
		public boolean reCheckStructure(World w, int x, int y, int z, Block originalb) {
			int size = mapStructure.size();
			for(Entry<String, Coord4> entry : mapStructure.entrySet()){
		    	int xT = entry.getValue().x + x;
		    	int yT = entry.getValue().y + y;
		    	int zT = entry.getValue().z + z;
				Block b = w.getBlock(xT, yT, zT);
				TileEntity t = w.getTileEntity(xT, yT, zT);
				if(b==null || b.getClass()!=originalb.getClass()){
					return false;
				}
				if(t!=null){
					/*if( ( t instanceof TileMasterPillar && ((TileMasterPillar)t).hasMaster() ) ){
						return false;
					}*/
					if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false && t instanceof TileBlockPillar==false){
						return false;
					}
				}
			}
			return true;
		}
		
		public void setupStructure(World w, EntityPlayer player, int x, int y, int z) {

			for(Entry<String, Coord4> entry : mapStructure.entrySet()){
		    	int xT = entry.getValue().x + x;
		    	int yT = entry.getValue().y + y;
		    	int zT = entry.getValue().z + z;
				TileEntity tb = w.getTileEntity(xT, yT, zT);
				
				if(tb!=null && tb instanceof TileMasterPillar && ((TileMasterPillar)tb).hasMaster())
					continue;
				
		    	if(tb!=null && tb instanceof TileRenderPillarModel){
					w.removeTileEntity(xT, yT, zT);
					w.setBlockMetadataWithNotify(xT, yT, zT, 0, 4);
					w.markBlockForUpdate(xT, yT, zT);
		    	}
		    	
		    	if(interfaces.containsKey(entry.getKey())){
		    		w.setBlockMetadataWithNotify(xT, yT, zT, 2, 4);
		    		TileEntity te = (TileEntity)w.getTileEntity(xT, yT, zT);
		    		TileInterfacePillar tip;
		        	if(te!=null && te instanceof TileInterfacePillar){
		        		tip = (TileInterfacePillar)te;
		    	        tip.master = new Coord4(x,y,z);
		    	        for(int k = 0 ; k<interfaces.get(entry.getKey()).length; k++){
			            	tip.extractSides.add(ForgeDirection.getOrientation(interfaces.get(entry.getKey())[k]));
		 	        		tip.recieveSides.add(ForgeDirection.getOrientation(interfaces.get(entry.getKey())[k]));
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
	            w.markBlockForUpdate(xT, yT, zT);
			}
		}
	}
}
