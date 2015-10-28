package noelflantier.sfartifacts.common.helpers;

import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianBronze;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianSteel;
import noelflantier.sfartifacts.common.blocks.BlockAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockMaterials;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.handlers.ModBlocks;

public enum PillarMaterials {
	BRONZE(BlockAsgardianBronze.class,18,256,3.9F,ModBlocks.blockAsgardianBronze, "asgardian_bronze"),
	STEEL(BlockAsgardianSteel.class,10,256,2.1F,ModBlocks.blockAsgardianSteel, "asgardian_steel"),
	ASGARDITE(BlockAsgardite.class,1,256,1F,ModBlocks.blockAsgardite, "asgardite");
	
	public final int ID;
	public final Class blockclass;
	public final int naturalEnergy;
	public final int maxHeightEfficiency;
	public final float rainEfficiency;
	public static int globalID = 0;
	public Item item;
	public Block block;
	public String texture;
	
	private PillarMaterials(Class <? extends BlockMaterialsTE> blockclass, int naturalEnergy, int maxHeightEfficiency, float rainEfficiency, Block bl, String texture){
		this.ID = nextGlobalID();
		this.blockclass = blockclass;
		this.naturalEnergy = naturalEnergy;
		this.rainEfficiency = rainEfficiency;
		this.maxHeightEfficiency = maxHeightEfficiency;
		this.block = bl;
		this.item = Item.getItemFromBlock(bl);
		this.texture = texture;
	}
	
	private int nextGlobalID(){
		globalID++;
		return globalID;
	}
	
	
	public static PillarMaterials getMaterialFromId(int id){
		for(PillarMaterials pm : PillarMaterials.values()){
			if(pm.ID==id)return pm;
		}
		return null;
	} 
	public static PillarMaterials getMaterialFromClass(Class <? extends Block> blockclass){
		for(PillarMaterials pm : PillarMaterials.values()){
			if(pm.blockclass==blockclass)return pm;
		}
		return null;
	} 
}
