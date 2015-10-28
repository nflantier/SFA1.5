package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class BlockLiquefiedAsgardite extends BlockFluidClassic{

    public IIcon stillIcon;
    public IIcon flowIcon;
    
	public BlockLiquefiedAsgardite(Fluid fluid, Material material) {
		super(fluid, material);
		this.setBlockName("blockLiquefiedAsgardite");
		this.setBlockTextureName(References.MODID+":liquefied_asgardite");
		this.setCreativeTab(SFArtifacts.sfTabs);
	}


    @Override
    public void registerBlockIcons (IIconRegister iconRegister)
    {
        stillIcon = iconRegister.registerIcon(this.getTextureName());
        flowIcon = iconRegister.registerIcon(this.getTextureName() + "_flow");
        this.getFluid().setIcons(stillIcon, flowIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta)
    {
        if (side == 0 || side == 1)
            return stillIcon;
        return flowIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double r =random.nextDouble();
        if(r<0.8) {
        	return;
        }
        float nx = (float)x+0.5F;
        float ny = (float)y+0.5F;
        float nz = (float)z+0.5F; 
    	float rdx =	0;
        float rdy = random.nextFloat()*2-1F;
    	float rdz = 0;
        if(rdy>0.5F || rdy<-0.5F){
        	rdx = random.nextFloat()*2-1F;
        	rdz = random.nextFloat()*2-1F;
        }else{
        	float tx = random.nextFloat();
        	if(tx>=0.5F){
        		rdx = random.nextFloat()*-1;
        	}else{
        		rdx = random.nextFloat();
        	}
        	
        	float tz = random.nextFloat();
        	if(tz>=0.5F){
        		rdz = random.nextFloat()*-1;
        	}else{
        		rdz = random.nextFloat();
        	}
        }
        ParticleHelper.spawnCustomParticle(ParticleHelper.Type.LIGHTNING, nx+rdx, ny+rdy, nz+rdz);
		ParticleHelper.spawnCustomParticle(ParticleHelper.Type.BOLT, nx+rdx, ny+rdy, nz+rdz);
    }
}
