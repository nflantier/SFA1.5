package noelflantier.sfartifacts.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.BlockMaterials;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.helpers.PillarStructures;
import noelflantier.sfartifacts.common.helpers.RenderBlocksHelper;

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
	    	String str = "0_0_0";
	    	do{
	    		if(str.equals("0_0_0")){
		    		str = (String)PillarStructures.getStructureFromId(idpillar).structure.get(str);
	    			continue;
	    		}
	    		String[] strParts = str.split("_");
	    		double xT = Integer.parseInt(strParts[0]);
	    		double yT = Integer.parseInt(strParts[1]);
	    		double zT = Integer.parseInt(strParts[2]);
				RenderBlocksHelper.renderSimpleFace(x+xT, y+yT, z+zT, "x", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
				RenderBlocksHelper.renderSimpleFace(x+xT, y+yT, z+zT, "y", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
				RenderBlocksHelper.renderSimpleFace(x+xT, y+yT, z+zT, "z", ((BlockMaterialsTE)tile.blockType).fullIcon, 0.55F ,0.1F, 0.1F);
	    		str = (String)PillarStructures.getStructureFromId(idpillar).structure.get(str);
	    	}while(!str.equals("end"));
	    }
	}
}