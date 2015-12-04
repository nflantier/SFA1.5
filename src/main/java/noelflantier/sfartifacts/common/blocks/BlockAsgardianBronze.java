package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import noelflantier.sfartifacts.References;

public class BlockAsgardianBronze extends BlockMaterialsTE implements IBlockMaterials{

	public BlockAsgardianBronze(Material material) {
		super(material, "Asgardian Bronze");
		
		this.setBlockName("blockAsgardianBronze");
		this.setBlockTextureName(References.MODID+":asgardian_bronze");
		this.setHarvestLevel("pickaxe",2);
		this.setHardness(3.0F);
		this.setResistance(10000.0F);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.blockIcon = icon.registerIcon(this.getTextureName());
		super.registerBlockIcons(icon);
    }

	@Override
	public String getTexture() {
		return References.MODID+":textures/blocks/"+this.getTextureName().substring(References.MODID.length()+1, this.getTextureName().length())+".png"; 
	}

}
