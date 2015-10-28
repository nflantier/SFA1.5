package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import noelflantier.sfartifacts.References;

public class BlockAsgardite extends BlockMaterialsTE implements IBlockMaterials{

	public BlockAsgardite(Material material) {
		super(material, "Asgardite");
		
		this.setBlockName("blockAsgardite");
		this.setBlockTextureName(References.MODID+":asgardite");
		this.setHarvestLevel("pickaxe",2);
		this.setHardness(3.0F);
		this.setLightLevel(0.5F);
		this.setResistance(20.0F);
		
	}
	
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

