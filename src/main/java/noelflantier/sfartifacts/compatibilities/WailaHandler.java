package noelflantier.sfartifacts.compatibilities;

import java.util.List;
import java.util.Locale;

import cpw.mods.fml.common.Optional;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila")
public class WailaHandler implements IWailaDataProvider {
	  private class WailaWorldWrapper extends World {
	    private World wrapped;

	    private WailaWorldWrapper(World wrapped) {
	      super(wrapped.getSaveHandler(), wrapped.getWorldInfo().getWorldName(), wrapped.provider, new WorldSettings(wrapped.getWorldInfo()), wrapped.theProfiler);
	      this.wrapped = wrapped;
	      this.isRemote = wrapped.isRemote;
	    }

	    @Override
	    public Block getBlock(int x, int y, int z) {
	      Block block = wrapped.getBlock(x, y, z);
	      return block;
	    }

	    @Override
	    public int getBlockMetadata(int x, int y, int z) {
	      Block block = wrapped.getBlock(x, y, z);
	      return wrapped.getBlockMetadata(x, y, z);
	    }

	    @Override
	    public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
	      return wrapped.getTileEntity(p_147438_1_, p_147438_2_, p_147438_3_);
	    }

	    @Override
	    protected IChunkProvider createChunkProvider() {
	      return null;
	    }

	    @Override
	    protected int func_152379_p() {
	      return 0;
	    }

	    @Override
	    public Entity getEntityByID(int p_73045_1_) {
	      return null;
	    }
	  }
	  
	 public static final WailaHandler INSTANCE = new WailaHandler();
	 
	 @Optional.Method(modid = "Waila")
	 public static void load(IWailaRegistrar register) {
		 register.registerBodyProvider(INSTANCE, TileSFA.class);
	 }

	@Override
	@Optional.Method(modid = "Waila")
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return accessor.getStack();
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

		Locale.setDefault(Locale.US);
	    EntityPlayer player = accessor.getPlayer();
	    MovingObjectPosition pos = accessor.getPosition();
	    int x = pos.blockX, y = pos.blockY, z = pos.blockZ;
	    World world = new WailaWorldWrapper(player.worldObj);
	    Block block = world.getBlock(x, y, z);
	    TileEntity te = world.getTileEntity(x, y, z);
	    Item item = Item.getItemFromBlock(block);
	    NBTTagCompound trueNbt = new NBTTagCompound();
	    
		((ITaggedList<String, String>) currenttip).removeEntries("RFEnergyStorage");
		
		/*if(te instanceof TileControlPannel){
	    	te.writeToNBT(trueNbt);
			currenttip.add(String.format("Pillar at : %s%s,%s,%s%s",EnumChatFormatting.WHITE,trueNbt.getInteger("pillarX"),trueNbt.getInteger("pillarY"),
					trueNbt.getInteger("pillarZ"),EnumChatFormatting.RESET));
		}*/
		/*
		if(te instanceof TileEntityMultiBlockPillar){
			
		    if(accessor.getNBTData().hasKey("hasMaster"))
		    	trueNbt = accessor.getNBTData();
		    else
		    	te.writeToNBT(trueNbt);

			TileEntityMultiBlockPillar tep = (TileEntityMultiBlockPillar) te;
			if(trueNbt.getBoolean("hasMaster")){
				
				currenttip.add(String.format("Pillar : "+PillarStructures.getStructureFromId(trueNbt.getInteger("structureId")).name()));
				currenttip.add(String.format("Material : "+PillarMaterials.getMaterialFromId(trueNbt.getInteger("materialId")).name()));
				if(!trueNbt.getBoolean("isMaster"))
					currenttip.add(String.format("Master at : %s%s,%s,%s%s",EnumChatFormatting.WHITE,trueNbt.getInteger("masterX"),trueNbt.getInteger("masterY"),
							trueNbt.getInteger("masterZ"),EnumChatFormatting.RESET));
				else
					currenttip.add(String.format("This is the master"));
				
				currenttip.add(String.format("%s : %s%s%s / %s%s%s RF","Energy", EnumChatFormatting.WHITE, NumberFormat.getNumberInstance().format(trueNbt.getInteger("rf")), 
						EnumChatFormatting.RESET, EnumChatFormatting.WHITE,NumberFormat.getNumberInstance().format(PillarStructures.getStructureFromId(trueNbt.getInteger("structureId")).capacity),EnumChatFormatting.RESET));
				List<FluidTank> ft = ((ISFAFluid)te).getFluidTanks();
				for(FluidTank f: ft){
					if(f.getFluid().getFluid().getName()!=null)
					currenttip.add(String.format("%s : %s%s%s / %s%s%s MB",Character.toUpperCase(f.getFluid().getFluid().getName().charAt(0)) + f.getFluid().getFluid().getName().substring(1), EnumChatFormatting.WHITE, NumberFormat.getNumberInstance().format(f.getFluidAmount()), 
							EnumChatFormatting.RESET, EnumChatFormatting.WHITE,NumberFormat.getNumberInstance().format(f.getCapacity()),EnumChatFormatting.RESET));
				}
			}
		}		
		*/
		/*
		if(te instanceof TileLiquefier || te instanceof TileInjector || te instanceof TileMightyFoundry){
	    	te.writeToNBT(trueNbt);
			currenttip.add(String.format("%s : %s%s%s / %s%s%s RF","Energy", EnumChatFormatting.WHITE, NumberFormat.getNumberInstance().format(trueNbt.getInteger("rf")), 
					EnumChatFormatting.RESET, EnumChatFormatting.WHITE,NumberFormat.getNumberInstance().format(((TileEntitySFAEnergy)te).getMaxEnergyStored(ForgeDirection.UNKNOWN)),EnumChatFormatting.RESET));
			List<FluidTank> ft = ((ISFAFluid)te).getFluidTanks();
			for(FluidTank f: ft){
				if(f.getFluid()!=null && f.getCapacity()!=0 && f.getFluid().getFluid().getName()!=null)
				currenttip.add(String.format("%s : %s%s%s / %s%s%s MB",Character.toUpperCase(f.getFluid().getFluid().getName().charAt(0)) + f.getFluid().getFluid().getName().substring(1), EnumChatFormatting.WHITE, NumberFormat.getNumberInstance().format(f.getFluidAmount()), 
						EnumChatFormatting.RESET, EnumChatFormatting.WHITE,NumberFormat.getNumberInstance().format(f.getCapacity()),EnumChatFormatting.RESET));
			}
		}
		
		if(te instanceof TileLightningRodStand){
			te.writeToNBT(trueNbt);
			currenttip.add(String.format("%s : %s%s%s / %s%s%s RF","Energy", EnumChatFormatting.WHITE, NumberFormat.getNumberInstance().format(trueNbt.getInteger("rf")), 
					EnumChatFormatting.RESET, EnumChatFormatting.WHITE,NumberFormat.getNumberInstance().format(((TileEntitySFAEnergy)te).getMaxEnergyStored(ForgeDirection.UNKNOWN)),EnumChatFormatting.RESET));
			
		}
			*/    
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
	    return tag;
	}
}
