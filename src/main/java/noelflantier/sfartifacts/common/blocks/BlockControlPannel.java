package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockControlPannel extends BlockSFAContainer{

	public static int renderId;
	
	public BlockControlPannel(Material material) {
		super(material, "Control Panel");
		
		this.setBlockName("blockControlPanel");
		this.setBlockTextureName(References.MODID+":machine");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(1.0F);
		this.setResistance(2000.0F);
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.blockIcon = icon.registerIcon(this.getTextureName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(world.isRemote)
    		return true;
    	if(player.isSneaking())
    		return false;
    	
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileControlPannel && ((TileControlPannel)tile).hasMaster()){
        	player.openGui(SFArtifacts.instance, ModGUIs.guiIDControlPanel, world, x, y, z);
    		return true;
		}
		
		return false;    	
    }
    
    public void onNeighborBlockChange(World w, int x, int y, int z, Block b) {
    	super.onNeighborBlockChange(w, x, y, z, b);
    	TileEntity tile = w.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileControlPannel){
			((TileControlPannel)tile).checkMaster();
		}
    }
    
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileControlPannel();
	}
    
	@Override
    public int getRenderType()
	{
		return renderId;
	}

    @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @Override
	public boolean renderAsNormalBlock(){
		return false;
	}
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess ib, int x, int y, int z){        
    	TileEntity tile = ib.getTileEntity(x, y, z);
	    if(tile!=null && tile instanceof TileControlPannel){
	    	TileControlPannel t = (TileControlPannel)tile;
	    	switch(t.side){
				case 2:
		            this.setBlockBounds(0.0F, 0.0F, 0.7F, 1F, 1F, 1F); 
					break;
				case 3:
		            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 1F, 0.3F); 
					break;
				case 4:
		            this.setBlockBounds(0.7F, 0.0F, 0.0F, 1F, 1F, 1F); 
					break;
				case 5:
		            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.3F, 1F, 1F); 
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
}
