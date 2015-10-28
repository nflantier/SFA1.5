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
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public class BlockInjector extends ABlockNBT implements IBlockUsingMaterials{

	public static int renderId;
	
	public BlockInjector(Material material) {
		super(material, "Injector");
		this.setBlockName("blockInjector");
		this.setHarvestLevel("pickaxe",1);
		this.setBlockTextureName(References.MODID+":machine");
		this.setHardness(3.0F);
		this.setResistance(2000.0F);
    	
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileInjector();
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
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
	  return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	  if(side == ForgeDirection.UP) { //stop drips
	    return false;
	  }
	  return true;
	}

	@Override
	public boolean canPlaceTorchOnTop(World arg0, int arg1, int arg2, int arg3) {
	  return true;
	}

	@Override
	public boolean dropWithNBT(int meta){
		return true;
	}
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){

    	if(player.isSneaking()) {
    		return false;
    	}
    	
    	if(player.getCurrentEquippedItem()!=null && FluidContainerRegistry.isContainer(player.getCurrentEquippedItem())){
    		if(!world.isRemote){
	    		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
				if(fluid!=null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite && fluid.amount>0){
					TileEntity t = world.getTileEntity(x, y, z);
					if(t instanceof TileInjector){
						TileInjector tel = (TileInjector)t;
						if(tel.tank.getFluidAmount()+fluid.amount<=tel.tank.getCapacity()){
							ItemStack emptyItem = player.getCurrentEquippedItem().copy();
							int avail = tel.fill(ForgeDirection.UNKNOWN,fluid, true);
							if(avail<=0)
								return false;
							fluid.amount = fluid.amount-avail;
						    if(player.getCurrentEquippedItem().getItem().hasContainerItem(player.getCurrentEquippedItem())) {
						    	emptyItem = player.getCurrentEquippedItem().getItem().getContainerItem(player.getCurrentEquippedItem());
						    }
							FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
							if(!player.capabilities.isCreativeMode)
								player.inventory.mainInventory[player.inventory.currentItem] = emptyItem;
						}
					}
				}
    		}
    		return true;
    	}
    	
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDInjector, world, x, y, z);
		return true;
    	
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
	public boolean renderAsNormalBlock()
	{
		return false;
	}  
}
