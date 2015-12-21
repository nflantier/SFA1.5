package noelflantier.sfartifacts.client.render;

import java.util.Map.Entry;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.RenderBlocksHelper;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;

public class RenderBlockMaterials  extends TileEntitySpecialRenderer  implements ISimpleBlockRenderingHandler {
		
	public RenderBlockMaterials(){
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	    renderer.setOverrideBlockTexture(block.getIcon(0, metadata));
	    renderer.renderBlockAsItem(Blocks.stone, 0, 1);
	    renderer.clearOverrideBlockTexture();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
	    int meta = blockAccess.getBlockMetadata(x, y, z);
	    TileBlockPillar tp = null;
	    TileEntity te = blockAccess.getTileEntity(x, y, z);
	    if(te!=null && te instanceof TileBlockPillar)
	    	tp = (TileBlockPillar) te;
	    for(ForgeDirection fd : ForgeDirection.values()){
    		if(tp!=null && tp.hasMaster()){
    			if(tp instanceof TileInterfacePillar && ( ((TileInterfacePillar)tp).extractSides.contains(fd) 
    					|| ((TileInterfacePillar)tp).recieveSides.contains(fd) ) )
    	    		RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, fd.ordinal(), ((BlockMaterialsTE)block).interfaceIcon[0]);
    			else
    				RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, fd.ordinal(), ((BlockMaterialsTE)block).fullIcon);
    		}else
    			RenderBlocksHelper.renderFace(blockAccess, renderer, block, x, y, z, fd.ordinal(), ((BlockMaterialsTE)block).blockCIcon);
	    }
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return BlockMaterialsTE.renderId;
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x,double y, double z, float partialTickTime) {
		if(tile==null)
			return;
		TileRenderPillarModel tp = null;
	    if(tile instanceof TileRenderPillarModel){
	    	tp = (TileRenderPillarModel) tile;
	    }else
	    	return;

	    if(tp.isRenderingPillarModel>-1){
	    	int idpillar = tp.isRenderingPillarModel;
	    	String name = PillarsConfig.getInstance().nameOrderedBySize.get(idpillar);
			if(name!=null && PillarsConfig.getInstance().nameToPillar.containsKey(name)){
				Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
				for(Entry<String, Coord4> entry : p.mapStructure.entrySet()){
					RenderBlocksHelper.renderSimpleFace(x+entry.getValue().x, y+entry.getValue().y, z+entry.getValue().z, "x", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
					RenderBlocksHelper.renderSimpleFace(x+entry.getValue().x, y+entry.getValue().y, z+entry.getValue().z, "y", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
					RenderBlocksHelper.renderSimpleFace(x+entry.getValue().x, y+entry.getValue().y, z+entry.getValue().z, "z", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
				}
			}
	    }
	}
}
