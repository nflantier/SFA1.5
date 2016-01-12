package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import noelflantier.sfartifacts.SFArtifacts;

public class BlockSFA  extends Block{
	
	public String name;
	
	public BlockSFA(Material material, String name) {
		super(material);
		
		this.name = name;
		this.setCreativeTab(SFArtifacts.sfTabs);
		
	}
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.blockIcon = icon.registerIcon(this.getTextureName());
		super.registerBlockIcons(icon);
    }
}
