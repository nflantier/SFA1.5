package noelflantier.sfartifacts.common.helpers;

import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianBronze;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianSteel;
import noelflantier.sfartifacts.common.blocks.BlockAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.handlers.ModBlocks;

public enum PillarMaterials {
	BRONZE(BlockAsgardianBronze.class,18,256,1.5F,ModBlocks.blockAsgardianBronze, "asgardian_bronze"),
	STEEL(BlockAsgardianSteel.class,10,256,1.5F,ModBlocks.blockAsgardianSteel, "asgardian_steel"),
	ASGARDITE(BlockAsgardite.class,2,256,3.9F,ModBlocks.blockAsgardite, "asgardite");
	
	public final int ID;
	public final Class blockclass;
	public final float energyRatio;
	public final float heightRatio;
	public final float rainRatio;
	public static int globalID = 0;
	public Item item;
	public Block block;
	public String texture;
	
	private PillarMaterials(Class <? extends BlockMaterialsTE> blockclass, int energyRatio, int maxHeightEfficiency, float rainEfficiency, Block bl, String texture){
		this.ID = nextGlobalID();
		this.blockclass = blockclass;
		this.energyRatio = energyRatio;
		this.rainRatio = rainEfficiency;
		this.heightRatio = maxHeightEfficiency;
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
