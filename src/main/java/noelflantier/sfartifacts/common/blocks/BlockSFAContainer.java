package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.ITileGlobalNBT;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;

public class BlockSFAContainer extends BlockContainer {

	public String name;
    private final Random randomN = new Random();
	
	public BlockSFAContainer(Material material, String name) {
		super(material);
		
		this.name = name;
		this.setCreativeTab(SFArtifacts.sfTabs);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
	    super.onBlockPlacedBy(world, x, y, z, player, stack);
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileSFA){
			int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			int s = -1;
			if(l == 0)s=2;
			if(l == 1)s=5;
			if(l == 2)s=3;
			if(l == 3)s=4;
		    if(te instanceof ITileGlobalNBT) {
		    	((ITileGlobalNBT)te).readFromItem(stack);
				te.xCoord = x;
				te.yCoord = y;
				te.zCoord = z;
				((TileSFA) te).side=s;
			}
			if(s!=-1)((TileSFA)te).side = s;
			world.markBlockForUpdate(x, y,z);
		}
	}
	
	public boolean isInventoryDroppedOnBreaking(){
		return false;
	}
	
	@Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity){
        return false;
    }
	
	public void breakBlock(World w, int x, int y, int z, Block b, int p_149749_6_){
		if(!isInventoryDroppedOnBreaking())
			super.breakBlock(w, x, y, z, b, p_149749_6_);
		
		TileEntity te = w.getTileEntity(x, y, z);

        if (te != null && te instanceof IInventory){
        	IInventory tei = (IInventory)te;
            for (int i1 = 0; i1 < tei.getSizeInventory(); ++i1){
                ItemStack itemstack = tei.getStackInSlot(i1);

                if (itemstack != null){
                    float f = this.randomN.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.randomN.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.randomN.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; w.spawnEntityInWorld(entityitem)){
                        int j1 = this.randomN.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize){
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(w, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.randomN.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.randomN.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.randomN.nextGaussian() * f3);

                        if (itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            w.func_147453_f(x, y, z, b);
        }
        super.breakBlock(w, x, y, z, b, p_149749_6_);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
	}

}
