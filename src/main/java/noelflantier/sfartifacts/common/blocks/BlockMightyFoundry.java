package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockMightyFoundry extends ABlockNBT{

	public BlockMightyFoundry(Material material) {
		super(material, "MIghty Foundry");
		this.setBlockName("blockMightyFoundry");
		this.setBlockTextureName(References.MODID+":machine");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(3.0F);
		this.setResistance(10000.0F);
	}

	@Override
	public boolean dropWithNBT(int meta) {
		return true;
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
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileMightyFoundry();
	}
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(player.isSneaking()) {
    		return false;
    	}    	
       	if(player.getCurrentEquippedItem()!=null && FluidContainerRegistry.isContainer(player.getCurrentEquippedItem())){
    		if(!world.isRemote){
	    		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
				if(fluid!=null && fluid.getFluid()==FluidRegistry.LAVA && fluid.amount>0){
					TileEntity t = world.getTileEntity(x, y, z);
					if(t instanceof TileMightyFoundry){
						TileMightyFoundry tmf = (TileMightyFoundry)t;
						if(tmf.tank.getFluidAmount()+fluid.amount<=tmf.tank.getCapacity()){
							ItemStack emptyItem = player.getCurrentEquippedItem().copy();
							int avail = tmf.fill(ForgeDirection.UNKNOWN,fluid, true);
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
       	
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDMightyFoundry, world, x, y, z);
    	return true;
    }
    
	@Override
    public int getRenderType()
	{
		return -1;
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
