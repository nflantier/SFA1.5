package noelflantier.sfartifacts.common.blocks;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.hammerstandrecipe.ConfigByHandRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.EnchantmentRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.LightningRecipe;
import noelflantier.sfartifacts.common.hammerstandrecipe.MagnetRecipe;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.HammerStandRecipe;
import noelflantier.sfartifacts.common.helpers.InjectorRecipe;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;
import noelflantier.sfartifacts.common.network.messages.PacketSound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHammerStand extends BlockSFAContainer {

    @SideOnly(Side.CLIENT)
	protected IIcon[] metaIcons;
    @SideOnly(Side.CLIENT)
	private int hsRenderSide;
	public static String[] typeHammerStand = new String[] {"normal", "broken"};
	
	public BlockHammerStand(Material material) {
		super(material, "Hammer Stand");
		
		this.setBlockName("blockHammerStand");
		this.setBlockTextureName(References.MODID+":hammer_stand");
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(3.0F);
		this.setResistance(2000.0F);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabz, List list)
    {
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    }

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(References.MODID + ":hammerstand_normal");
        this.metaIcons = new IIcon[this.typeHammerStand.length];
		for (int i = 0; i < metaIcons.length; i++) {
			metaIcons[i] = iconRegister.registerIcon(References.MODID + ":hammerstand_"+ this.typeHammerStand[i]);
		}
	}

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	 if (this.hsRenderSide == 3 && side == 1)
         {
             int k = (meta >> 2) % this.metaIcons.length;
             return this.metaIcons[k];
         }
         else
         {
             return this.blockIcon;
         }
    }
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = world.getBlockMetadata(x, y, z) >> 2;
        ++l;
        l %= 4;
		if(l == 0)world.setBlockMetadataWithNotify(x, y, z, 2 | i1 << 2, 2);
		if(l == 1)world.setBlockMetadataWithNotify(x, y, z, 3 | i1 << 2, 2);
		if(l == 2)world.setBlockMetadataWithNotify(x, y, z, 0 | i1 << 2, 2);
		if(l == 3)world.setBlockMetadataWithNotify(x, y, z, 1 | i1 << 2, 2);
	}
	
	@Override
    public int damageDropped(int meta){
    	return meta >> 2;
    }
    
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if(side==ForgeDirection.UP)return false;
		return true;
    }
	
    @Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHammerStand();
	}
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
    	if(player.isSneaking()) {
    		return false;
    	}
    	
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileHammerStand){
				    	
	    	if(side==1 && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem()==ModItems.itemBasicHammer && ItemNBTHelper.getInteger(player.getCurrentEquippedItem(), "Mode", 1)==0){
	    		TileHammerStand ths= (TileHammerStand)tile;
	    		if(ths.items[0]!=null){
	    			
		    		if(!world.isRemote && ths.curentRecipe!=null){
		    			
		    			if(ths.curentRecipe.recipe.itemStillHere()){
		    				ths.curentRecipe.recipe.age++;
		    				if(ths.curentRecipe.recipe.age%2==0)
		    					PacketHandler.sendToAllAround(new PacketSound(x, y, z,  SoundHelper.ANVIL.ordinal()),ths);
			    			if(ths.curentRecipe.recipe.isDone()){
			    				ths.curentRecipe.recipe.end(ths.items[0]);				
			    				ths.curentRecipe = null;
			    				world.markBlockForUpdate(x, y, z);		
			    			}
			    			
		    			}else{
		    				ths.curentRecipe = null;
		    			}
			    		return true;
		    		}
		    		
	    			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x-1,y,z-1,x+1,y+2,z+1));

    				if (!world.isRemote){
						for(HammerStandRecipe hsr : HammerStandRecipe.values()){
							if(hsr.recipe.canCraft(items,  ths.items[0])){
								ths.curentRecipe = hsr;
								break;
							}
						}
    				}
	    		}
	    		return true;
	    	}
			
    		if(tile.getBlockMetadata()>3) player.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerStandInvoked, world, x, y, z);
    		else player.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerStandNonInvoked, world, x, y, z);
    		
		}else{
			return false;
		}
		return true;
    }
    
	@Override
	public boolean isInventoryDroppedOnBreaking(){
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
