package noelflantier.sfartifacts.compatibilities;

import java.text.NumberFormat;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import noelflantier.sfartifacts.common.blocks.BlockOreVibranium;
import noelflantier.sfartifacts.common.blocks.tiles.IHasWailaContent;
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
		register.registerBodyProvider(INSTANCE, BlockOreVibranium.class);
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
	    TileEntity te = world.getTileEntity(x, y, z);
	    Block b = world.getBlock(x, y, z);
	    
		((ITaggedList<String, String>) currenttip).removeEntries("RFEnergyStorage");
		if( te instanceof IHasWailaContent){
			((IHasWailaContent)te).addToWaila(currenttip);
		}   
		if(b instanceof BlockOreVibranium){
			int meta = world.getBlockMetadata(x, y, z);
			currenttip.add("Cooked process : "+NumberFormat.getNumberInstance().format((float)meta/(float)15*(float)100)+" %");
		}
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
