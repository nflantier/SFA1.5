package noelflantier.sfartifacts.common.blocks;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockInductor extends BlockSFAContainer {

	public static String[] typeInductor = new String[]  {"basic", "advanced", "basicenergized", "advancedenergized"};
	
	public BlockInductor(Material material) {
		super(material, "Inductor");
		this.setBlockName("blockInductor");
		this.setBlockTextureName(References.MODID+":inductor");
		this.setHardness(1.0F);
		this.setResistance(100.0F);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabz, List list){
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
    	list.add(new ItemStack(item, 1, 3));
    }
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(player.isSneaking()) {
    		return false;
    	}
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDInductor, world, x, y, z);
    	return true;
    }
    
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
	}
	
    public int damageDropped(int damage){
        return damage;
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
    public void setBlockBoundsBasedOnState(IBlockAccess ib, int x, int y, int z){        
    	TileEntity tile = ib.getTileEntity(x, y, z);
	    if(tile!=null && tile instanceof TileSFA){
	    	TileSFA t = (TileSFA)tile;
	    	switch(t.side){
				case 0:
		            this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F); 
					break;
				case 1:
		            this.setBlockBounds(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F); 
					break;
				case 2:
		            this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F); 
					break;
				case 3:
		            this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1F); 
					break;
				case 4:
		            this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F); 
					break;
				case 5:
		            this.setBlockBounds(0.5F, 0.25F, 0.25F, 1F, 0.75F, 0.75F); 
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileInductor(meta);
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
