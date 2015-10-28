package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
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
	
	/*@Override
    public int getRenderType()
	{
		return -1;
	}*/

   @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }
    
    @Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}  

}
