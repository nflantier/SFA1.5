package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileMrFusion;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockMrFusion extends BlockSFAContainer {
	
	public BlockMrFusion(Material material) {
		super(material, "MrFusion");
		this.setBlockName("blockMrFusion");
		this.setHarvestLevel("pickaxe",1);
		this.setBlockTextureName(References.MODID+":mrfusion");
		this.setHardness(2.0F);
		this.setResistance(10000.0F);
    	
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(World arg0, int arg1, int arg2, int arg3) {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileMrFusion();
	}
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){

    	if(player.isSneaking()) {
    		return false;
    	}
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDMrFusion, world, x, y, z);
    	return true;
    }
	
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess ib, int x, int y, int z){        
    	TileEntity tile = ib.getTileEntity(x, y, z);
	    if(tile!=null && tile instanceof TileSFA){
	    	TileSFA t = (TileSFA)tile;
	    	switch(t.side){
				case 0:
		            this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.9F, 0.875F); 
					break;
				case 1:
		            this.setBlockBounds(0.125F, 0.1F, 0.125F, 0.875F, 1F, 0.875F); 
					break;
				case 2:
		            this.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.9F); 
					break;
				case 3:
		            this.setBlockBounds(0.125F, 0.125F, 0.1F, 0.875F, 0.875F, 1F); 
					break;
				case 4:
		            this.setBlockBounds(0.0F, 0.125F, 0.125F, 0.9F, 0.875F, 0.875F); 
					break;
				case 5:
		            this.setBlockBounds(0.1F, 0.125F, 0.125F, 1F, 0.875F, 0.875F); 
					break;
				case -1:
					break;
				default:
					break;
			} 	
	    }
    }
    
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB abb, List list, Entity entity){
        this.setBlockBoundsBasedOnState(w, x, y, z);
        super.addCollisionBoxesToList(w, x, y, z, abb, list, entity);
    }
    
	@Override
	public boolean isInventoryDroppedOnBreaking(){
		return true;
	}
    
	@Override
    public int getRenderType()
	{
		return -1;
	}

    @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}  

}
