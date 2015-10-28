package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BlockSoundEmitter  extends ABlockNBT{

	public static int renderId;
	
	public BlockSoundEmitter(Material material) {
		super(material, "Sound Emiter");
		
		this.setBlockName("blockSoundEmiter");
		this.setBlockTextureName(References.MODID+":sound_emiter");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(3.0F);
		this.setResistance(10000.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileSoundEmitter();
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(player.isSneaking()) {
    		return false;
    	}
    	
    	if(player.getCurrentEquippedItem()!=null && FluidContainerRegistry.isContainer(player.getCurrentEquippedItem())){
    		if(!world.isRemote){
	    		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
				if(fluid!=null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite && fluid.amount>0){
					TileEntity t = world.getTileEntity(x, y, z);
					if(t instanceof TileSoundEmitter){
						TileSoundEmitter tel = (TileSoundEmitter)t;
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
    	
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDSoundEmiter, world, x, y, z);
    	return true;
    }
	
	@Override
	public boolean dropWithNBT(int meta) {
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
