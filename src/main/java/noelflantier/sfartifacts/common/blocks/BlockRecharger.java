package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockRecharger extends ABlockNBT {
	public BlockRecharger(Material material) {
		super(material, "Recharger");
		this.setBlockName("blockRecharger");
		this.setBlockTextureName(References.MODID+":recharger");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(3.0F);
		this.setResistance(10000.0F);
	}    
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(player.isSneaking()) {
    		return false;
    	}
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDRecharger, world, x, y, z);
    	return true;
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRecharger();
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

	@Override
	public boolean dropWithNBT(int meta) {
		return true;
	}
}
