package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketParticleGlobal;

public class BlockOreAsgardite extends BlockSFA{
	
	public BlockOreAsgardite(Material material) {
		super(material, "Ore Asgardite");
		this.setBlockName("blockOreAsgardite");
		this.setBlockTextureName(References.MODID+":asgardite_ore");
		this.setHarvestLevel("pickaxe",2);
		this.setHardness(3.0F);
		this.setLightLevel(0.5F);
		this.setResistance(1.0F);
	}

	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.blockIcon = icon.registerIcon(this.getTextureName());
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return ModItems.itemAsgardite;
    }

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
	
    @Override
    public int quantityDroppedWithBonus(int p_149679_1_, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(p_149679_1_ + 1);
    }

    @Override
    public int tickRate(World world){
        return 20;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if(ModConfig.isOresEmitParticles)
    		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random){
    	super.updateTick(world, x, y, z, random);
    	if(ModConfig.isOresEmitParticles)
    		return;
    	if(world.isRemote)
    		return;
    	PacketHandler.sendToAllAround(new PacketParticleGlobal(ParticleHelper.Type.ASGARDIANORES,x,y,z),world,x,y,z);
    	world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }
    
    @Override
    public int quantityDropped(Random random)
    {
        return 4 + random.nextInt(2);
    }
}
