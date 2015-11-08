package noelflantier.sfartifacts.common.items;

import java.util.ArrayList;
import java.util.List;

import cofh.api.energy.IEnergyConnection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.ITileCanBeMaster;
import noelflantier.sfartifacts.common.blocks.tiles.ITileCanHavePillar;
import noelflantier.sfartifacts.common.blocks.tiles.ITileMustHaveMaster;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.helpers.Coord4;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.PillarStructures;
import noelflantier.sfartifacts.common.items.baseclasses.IItemHasModes;
import noelflantier.sfartifacts.common.items.baseclasses.ItemMode;

public class ItemBasicHammer extends ItemSFA implements IItemHasModes{

	public List<ItemMode> modes = new ArrayList<>();
	public ItemBasicHammer() {
		super("Basic Hammer");
		this.setUnlocalizedName("itemBasicHammer");
		this.setTextureName(References.MODID+":basic_hammer");
		this.setMaxStackSize(1);
		this.addMode("Basic", "Basic", "To forge and associate structures");
		this.addMode("Construction", "Construction", "To see pillars models");
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_){

		TileEntity t = world.getTileEntity(x, y, z);
		int mode = ItemNBTHelper.getInteger(stack, "Mode", 0);
				
		if(world.isRemote)
			return false;
		
		if(mode!=0)
			return false;
		if(t!=null && t instanceof ITileMustHaveMaster){
			ITileMustHaveMaster tcbm = (ITileMustHaveMaster)t;
			if(tcbm.hasMaster()){
				ItemNBTHelper.setInteger(stack, "mx", tcbm.getMasterX());
				ItemNBTHelper.setInteger(stack, "my", tcbm.getMasterY());
				ItemNBTHelper.setInteger(stack, "mz", tcbm.getMasterZ());

				ItemNBTHelper.setBoolean(stack, "hasmaster", true);
				player.addChatComponentMessage(new ChatComponentText("Master at "+ItemNBTHelper.getInteger(stack, "mx", -1)+" "+
												ItemNBTHelper.getInteger(stack, "my", -1)+" "+ItemNBTHelper.getInteger(stack, "mz", -1)));
			}
		}else if(t!=null && ( t instanceof ITileCanHavePillar || t instanceof IEnergyConnection )){
			ItemNBTHelper.setInteger(stack, "cx", t.xCoord);
			ItemNBTHelper.setInteger(stack, "cy", t.yCoord);
			ItemNBTHelper.setInteger(stack, "cz", t.zCoord);

			ItemNBTHelper.setBoolean(stack, "haschild", true);
			player.addChatComponentMessage(new ChatComponentText("Machine at "+ItemNBTHelper.getInteger(stack, "cx", -1)+" "+
											ItemNBTHelper.getInteger(stack, "cy", -1)+" "+ItemNBTHelper.getInteger(stack, "cz", -1)));
		}
		
		if(ItemNBTHelper.getBoolean(stack, "haschild", false) && ItemNBTHelper.getBoolean(stack, "hasmaster", false)){

			TileEntity tm = world.getTileEntity(ItemNBTHelper.getInteger(stack, "mx", -1), 
					ItemNBTHelper.getInteger(stack, "my", -1), ItemNBTHelper.getInteger(stack, "mz", -1));
			TileEntity tc = world.getTileEntity(ItemNBTHelper.getInteger(stack, "cx", -1), 
					ItemNBTHelper.getInteger(stack, "cy", -1), ItemNBTHelper.getInteger(stack, "cz", -1));
			ITileCanBeMaster tcbm = (ITileCanBeMaster)tm;
			if(tm!=null && tcbm!=null && tcbm.getChildsList()!=null){
				if(tcbm.getChildsList().contains(tc)){
					tcbm.getChildsList().remove(tc);
					player.addChatComponentMessage(new ChatComponentText("Machine allready connected to that pillar. It has been removed."));
				}else{
					tcbm.getChildsList().add(tc);
					if(tc instanceof TileSFA){
						Coord4 m = new Coord4(ItemNBTHelper.getInteger(stack, "mx", -1),ItemNBTHelper.getInteger(stack, "my", -1),ItemNBTHelper.getInteger(stack, "mz", -1));
						((TileSFA)tc).setVariables(new Object[]{m});
						world.markBlockForUpdate(tc.xCoord, tc.yCoord, tc.zCoord);
					}
					player.addChatComponentMessage(new ChatComponentText("Machine connected!"));
				}
			}else if(tm==null){
				ItemNBTHelper.setBoolean(stack, "hasmaster", false);
			}
			//ItemNBTHelper.setBoolean(stack, "hasmaster", false);
			if(ItemNBTHelper.getBoolean(stack, "haschild", false) && ItemNBTHelper.getBoolean(stack, "hasmaster", false)){
				ItemNBTHelper.setBoolean(stack, "haschild", false);
			}
			return true;
		}
		
		return false;
	}

    
	@Override
	public double getDistanceRay() {
		return 5.0D;
	}

	@Override
	public boolean shouldSneak() {
		return true;
	}

	@Override
	public String getStringChat() {
		return "Basic Hammer mode : ";
	}
	
	@Override
	public List<ItemMode> getModes() {
		return this.modes;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player){
		if(w.isRemote || (this.shouldSneak() && !player.isSneaking()) )return stack;
		
		MovingObjectPosition mo = HammerHelper.rayTraceLightning(player,this.getDistanceRay());
        if (mo == null){
        	changeMode(stack, player);
        }

	   return stack;
	}	

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it = new ItemStack(item, 1, 0);
		it = ItemNBTHelper.setInteger(it, "Mode", 0);
		list.add(it);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		int m = ItemNBTHelper.getInteger(stack, "Mode", 0);
		list.add("Mode : "+getModes().get(m).name);
		list.add(getModes().get(m).description);
	}
}
