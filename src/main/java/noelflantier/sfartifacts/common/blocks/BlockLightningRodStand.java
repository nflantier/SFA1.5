package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public class BlockLightningRodStand extends ABlockNBT  implements IBlockUsingMaterials{
	
	public static int renderId;
	
	public BlockLightningRodStand(Material material) {
		super(material, "LightningRod Stand");
		this.setBlockName("blockLightningRodStand");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(3.0F);
		this.setResistance(2000.0F);
		this.setBlockTextureName(References.MODID+":machine");
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess block, int x, int y, int z, int side)
    {
        return side!=1;
    }
    
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if(side==ForgeDirection.UP)return false;
		return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabz, List list)
    {
    	for(PillarMaterials pm :PillarMaterials.values()){
        	list.add(new ItemStack(item, 1, pm.ordinal()));
    	}
    }

	@Override
	public int damageDropped(int meta){
    	return meta;
    }
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz)
    {
    	if(player.isSneaking()) {
    		return false;
    	}

    	player.openGui(SFArtifacts.instance, ModGUIs.guiIDLightningRodStand, world, x, y, z);
    	return true;

    }
    /*
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess ib, int x, int y, int z){        
    	TileEntity tile = ib.getTileEntity(x, y, z);
	    if(tile!=null && tile instanceof TileEntityLightningRodStand){
	    	TileEntityLightningRodStand t = (TileEntityLightningRodStand)tile;
	    	if(t.items[0]!=null){
	    		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 6F, 1F);
	    	}else{
	    		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 1F, 1F);
	    	}
	    }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_, Vec3 p_149731_5_, Vec3 p_149731_6_){
        TileEntity tile = p_149731_1_.getTileEntity(p_149731_2_, p_149731_3_, p_149731_4_);
	    if(tile!=null && tile instanceof TileEntityLightningRodStand){
	    	TileEntityLightningRodStand t = (TileEntityLightningRodStand)tile;
	    	if(t.items[0]!=null){
	    		//this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 6F, 1F);
	    	}else{
	    		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 1F, 1F);
	    	}
	    }
        return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }

    @Override
    public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 1F, 1F);
    }
    
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB abb, List list, Entity entity){
        this.setBlockBoundsBasedOnState(w, x, y, z);
        super.addCollisionBoxesToList(w, x, y, z, abb, list, entity);
    }
    */
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileLightningRodStand();
	}

	/*@Override
	public void breakBlock(World w, int x, int y, int z, Block b, int meta)
    {
		TileEntity t = w.getTileEntity(x, y, z);
		if(t!=null && t instanceof TileEntityLightningRodStand){
			Random rand = new Random();
			TileEntityLightningRodStand te = (TileEntityLightningRodStand)t;
			for(int i = 0 ; i < te.items.length ; i++){
				if(te.items[i]!=null){
					float f1 = rand.nextFloat() * 0.8F+0.1F;
					float f2 = rand.nextFloat() * 0.8F+0.1F;
					float f3 = rand.nextFloat() * 0.8F+0.1F;
					EntityItem it = new EntityItem(w, x+f1, y+f2, z+f3, te.items[i]);
					w.spawnEntityInWorld(it);
					w.func_147453_f(x, y, z, b);
				}
			}
		}
		super.breakBlock(w, x, y, z, b, meta);
    }*/

    @Override
	public boolean isInventoryDroppedOnBreaking(){
		return true;
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
		return false;
	}
    
}
