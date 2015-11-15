package noelflantier.sfartifacts.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;


public class BlockOreVibranium extends BlockSFA {

	public static int renderId;
    @SideOnly(Side.CLIENT)
	protected IIcon[] metaIcons;
	public static String[] typeVibranium = new String[] {"0", "35", "65", "100"};
	public static int[][] tabC = {{0,0,1},{0,0,-1},{1,0,0},{-1,0,0},{0,1,0},{0,-1,0}};
	public static int tickBase = ModConfig.tickToCookVibraniumOres/16;
	
	public BlockOreVibranium(Material material) {
		super(material, "Ore Vibranium");
		
		this.setBlockName("blockOreVibranium");
		this.setBlockTextureName(References.MODID+":vibranium_ore");
		this.setHarvestLevel("pickaxe",3);
		this.setHardness(3.0F);
		this.setResistance(2000.0F);
	}

	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabz, List list)
    {
    	list.add(new ItemStack(item, 1, 0));
    	
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
    	list.add(new ItemStack(item, 1, 3));
    	list.add(new ItemStack(item, 1, 4));
    	list.add(new ItemStack(item, 1, 5));
    	list.add(new ItemStack(item, 1, 6));
    	list.add(new ItemStack(item, 1, 7));
    	
    	list.add(new ItemStack(item, 1, 8));
    	list.add(new ItemStack(item, 1, 9));
    	list.add(new ItemStack(item, 1, 10));
    	list.add(new ItemStack(item, 1, 11));
    	list.add(new ItemStack(item, 1, 12));
    	list.add(new ItemStack(item, 1, 13));
    	list.add(new ItemStack(item, 1, 14));
    	
    	list.add(new ItemStack(item, 1, 15));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	return this.metaIcons[meta<15?meta<8?meta<1?0:1:2:3];
    }
    
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(References.MODID + ":ore_vibranium_0");
        this.metaIcons = new IIcon[typeVibranium.length];
		for (int i = 0; i < metaIcons.length; i++) {
			metaIcons[i] = iconRegister.registerIcon(References.MODID + ":ore_vibranium_"+ typeVibranium[i]);
		}
    }

	@Override
    public int damageDropped(int meta){
    	return meta;
    }
	
    @Override
    public Item getItemDropped(int meta, Random random, int ch)
    {
        return meta==15?ModItems.itemVibraniumDust:super.getItemDropped(meta, random, ch);
    }

    @Override
    public int quantityDroppedWithBonus(int p_149679_1_, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(p_149679_1_ + 1);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 2+ random.nextInt(2);
    }
    
    @Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Item.getItemFromBlock(this), 1, metadata));
		return (metadata!=15)?ret:super.getDrops(world, x, y, z, metadata, fortune);
	}
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int meta){
		return meta==15?true:false;
    }

    @Override
    public int tickRate(World world){
        return 40;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random){
    	super.updateTick(world, x, y, z, random);
    	if(world.isRemote)
    		return;
    	
    	int tick = tickBase;

        for(int i = 0 ; i < this.tabC.length ; i++){
        	Block b = world.getBlock(x+this.tabC[i][0], y+this.tabC[i][1], z+this.tabC[i][2]);
        	Fluid f = FluidRegistry.lookupFluidForBlock(b);
        	if( f!=null && f.getTemperature()>=1300)
        		tick-=40;
        }
        int meta = world.getBlockMetadata(x, y, z);
    	if(meta!=15){
    		if(tick<tickBase){
	    		world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
	    		world.scheduleBlockUpdate(x, y, z, this, tick);
    		}else
        		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    	}
    	
    }
}
