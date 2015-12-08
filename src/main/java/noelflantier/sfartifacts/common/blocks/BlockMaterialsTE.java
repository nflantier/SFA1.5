package noelflantier.sfartifacts.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.PillarHelper;
import noelflantier.sfartifacts.common.items.ItemBasicHammer;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;

public class BlockMaterialsTE extends BlockSFAContainer{

	public static int renderId;
    @SideOnly(Side.CLIENT)
	public IIcon interfaceIcon[];
    public String [] intName = new String[]{"_out"};
    @SideOnly(Side.CLIENT)
	public IIcon fullIcon;
    @SideOnly(Side.CLIENT)
	public IIcon blockCIcon;
    
	public BlockMaterialsTE(Material material, String name) {
		super(material, name);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		if(meta==1)
			return new TileBlockPillar("BP");
		else if(meta==2)
			return new TileInterfacePillar("IP");
		else if(meta==3)
			return new TileMasterPillar("MP");
		else if(meta==4)
			return new TileRenderPillarModel();
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}	
	
	@SideOnly(Side.CLIENT)
	public IIcon getFullIcon(int meta) {
		return this.fullIcon;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.interfaceIcon =  new IIcon[this.intName.length];
		for(int i = 0 ; i < this.interfaceIcon.length ; i++ ){
			interfaceIcon[i] = iconRegister.registerIcon(this.getTextureName()+"_full_interface"+this.intName[i]);
		}
		fullIcon = iconRegister.registerIcon(this.getTextureName()+"_full");
		blockCIcon = iconRegister.registerIcon(this.getTextureName());
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
	  return false;
	}

	@Override
	public int getRenderType() {
	  return renderId;
	}
	  
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
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
	  return true;
	}

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);

		if(world.isRemote)
            return ;
		
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileBlockPillar){
			TileBlockPillar tbp = (TileBlockPillar)tile;
			if(tbp.hasMaster()){
				if(tbp.isMasterHere()){
					if(!PillarHelper.recheckStructure(world, tbp.getMasterX(),tbp.getMasterY(), tbp.getMasterZ(), tbp.getMasterTile().namePillar)){
						PillarHelper.unsetupStructureNoMaster(world, x, y, z);
					}
					/*if(!PillarHelper.recheckStructure(world, tbp.getMasterX(),tbp.getMasterY(), tbp.getMasterZ(), tbp.getMasterTile().structureId)){
						PillarHelper.unsetupStructureNoMaster(world, x, y, z);
					}*/
				}else{
					PillarHelper.unsetupStructureNoMaster(world, x, y, z);
				}
			}else{
				PillarHelper.unsetupStructureNoMaster(world, x, y, z);
			}
		}
    }
    
    public void showData(World world, int x, int y, int z, EntityPlayer player){
		TileEntity t = world.getTileEntity(x, y, z);
		if(t!=null &&  t instanceof TileBlockPillar && ((TileBlockPillar)t).hasMaster()){
			player.addChatComponentMessage(new ChatComponentText("Energy Stored : "+((TileBlockPillar)t).getMasterTile().getEnergyStored(ForgeDirection.UNKNOWN)));
			player.addChatComponentMessage(new ChatComponentText("Fluid Stored : "+((TileBlockPillar)t).getMasterTile().getFluidTanks().get(0).getFluidAmount()));
		}
    }
    
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
		
		ItemStack cei = player.getCurrentEquippedItem();
		int mode = 1;
		if(cei!=null)
			mode = ItemNBTHelper.getInteger(player.getCurrentEquippedItem(), "Mode", 1);
		
		/*if(player!=null && cei!=null && cei.getItem() instanceof ItemBasicHammer){
			
		}*/

		if(player!=null && cei!=null && cei.getItem() instanceof ItemBasicHammer && !world.isRemote){
			if(mode==0){
				PillarHelper.checkStructure(world, player, x, y, z, false);
			}
			if(mode==1){
				//CONSTRUCTION MODE  
	    		TileEntity t = world.getTileEntity(x, y, z);
	    		if(t==null){
	        		world.setBlockMetadataWithNotify(x, y, z, 4, 4);
	    		} 
	    		t = world.getTileEntity(x, y, z);
				if(t!=null && t instanceof TileRenderPillarModel){
					TileRenderPillarModel teap = (TileRenderPillarModel)t;
					int id = teap.isRenderingPillarModel==-1?0:teap.isRenderingPillarModel+1<PillarsConfig.getInstance().nameOrderedBySize.size()?teap.isRenderingPillarModel+1:-1;
					teap.isRenderingPillarModel = id;
					world.markBlockRangeForRenderUpdate(teap.xCoord, teap.yCoord, teap.zCoord, teap.xCoord, teap.yCoord, teap.zCoord);
					world.markBlockForUpdate(x, y, z);
					return true;
				}
			}
		}
    	if(cei!=null && FluidContainerRegistry.isContainer(cei) && !world.isRemote){
    		TileEntity t = world.getTileEntity(x, y, z);
    		if(t!=null &&  t instanceof TileBlockPillar && ((TileBlockPillar)t).hasMaster()){
    			TileMasterPillar tmp = ((TileBlockPillar)t).getMasterTile();
	    		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(cei);
	    		if(FluidContainerRegistry.isEmptyContainer(cei)){
	    			if(tmp.tank.getFluidAmount()>=1000){
	    				FluidStack fsavail = tmp.tank.drain(1000, false);
	    				if(fsavail!=null && fsavail.amount>0){
	    					player.inventory.mainInventory[player.inventory.currentItem] = FluidContainerRegistry.fillFluidContainer(fsavail, cei);
		    				tmp.tank.drain(1000, true);
	    				}
	    			}
	    		}else if(fluid!=null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite && fluid.amount>0){
					if(tmp.tank.getFluidAmount()<tmp.tank.getCapacity()){
						ItemStack emptyItem = cei.copy();
						int avail = tmp.tank.fill(fluid, true);
						fluid.amount = fluid.amount-avail;
					    if(cei.getItem().hasContainerItem(cei)) {
					    	emptyItem = cei.getItem().getContainerItem(cei);
					    }
						FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
						if(!player.capabilities.isCreativeMode)
							player.inventory.mainInventory[player.inventory.currentItem] = emptyItem;
					}
	    		}
    		}
    		return true;
    	}
		
		if(cei==null && !world.isRemote){
			if(player.capabilities.isCreativeMode){
				TileEntity t = world.getTileEntity(x, y, z);
				if(t!=null &&  t instanceof TileBlockPillar  && ((TileBlockPillar)t).hasMaster())
					((TileBlockPillar)t).getMasterTile().receiveEnergy(ForgeDirection.UNKNOWN, 500000, false);
			}
    		showData(world,x,y,z,player);
    		return true;
		}
		return false;
    }
}
