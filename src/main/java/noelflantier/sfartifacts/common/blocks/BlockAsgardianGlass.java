package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;

public class BlockAsgardianGlass extends BlockSFA{

	public BlockAsgardianGlass(Material material) {
		super(material, "Asgardian Glass");
		
		this.setBlockName("blockAsgardianGlass");
		this.setBlockTextureName(References.MODID+":asgardian_glass");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(1.0F);
		this.setResistance(2000.0F);
		this.setStepSound(soundTypeGlass);
	}

	@Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity){
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
	  return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	  return true;
	}
	
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass(){
        return 0;
    }
    
    @Override
	public boolean renderAsNormalBlock(){
		return false;
	}  

}
