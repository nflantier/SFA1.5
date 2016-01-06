package noelflantier.sfartifacts.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.handlers.ModFluids;

public class FilledBucket extends ItemBucket{

    public FilledBucket(Block b)
    {
        super(b);
        this.setUnlocalizedName("sfaFluidBucket");
        this.setContainerItem(Items.bucket);
		this.setCreativeTab(SFArtifacts.sfTabs);
        this.setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        float var4 = 1.0F;
        double trueX = player.prevPosX + (player.posX - player.prevPosX) * (double) var4;
        double trueY = player.prevPosY + (player.posY - player.prevPosY) * (double) var4 + 1.62D - (double) player.yOffset;
        double trueZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) var4;
        boolean wannabeFull = false;
        MovingObjectPosition position = this.getMovingObjectPositionFromPlayer(world, player, wannabeFull);

        if (position == null)
        {
            return stack;
        }
        else
        {

            if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int clickX = position.blockX;
                int clickY = position.blockY;
                int clickZ = position.blockZ;

                if (!world.canMineBlock(player, clickX, clickY, clickZ))
                {
                    return stack;
                }

                if (position.sideHit == 0)
                {
                    --clickY;
                }

                if (position.sideHit == 1)
                {
                    ++clickY;
                }

                if (position.sideHit == 2)
                {
                    --clickZ;
                }

                if (position.sideHit == 3)
                {
                    ++clickZ;
                }

                if (position.sideHit == 4)
                {
                    --clickX;
                }

                if (position.sideHit == 5)
                {
                    ++clickX;
                }

                if (!player.canPlayerEdit(clickX, clickY, clickZ, position.sideHit, stack))
                {
                    return stack;
                }
                
                if(world.getBlock(position.blockX, position.blockY, position.blockZ) instanceof BlockMaterialsTE 
                		&& world.getTileEntity(position.blockX, position.blockY, position.blockZ)!=null 
                		&& world.getTileEntity(position.blockX, position.blockY, position.blockZ) instanceof TileBlockPillar){
                	return stack;
                }

                if (this.tryPlaceContainedLiquid(world, clickX, clickY, clickZ, stack.getItemDamage()) && !player.capabilities.isCreativeMode)
                {
                    return new ItemStack(Items.bucket);
                }
            }

            return stack;
        }
    }

    public boolean tryPlaceContainedLiquid (World world, int clickX, int clickY, int clickZ, int meta){
        Material material = world.getBlock(clickX, clickY, clickZ).getMaterial();
        boolean flag = !material.isSolid();

        if (!world.isAirBlock(clickX, clickY, clickZ) && !flag){
            return false;
        }else{
            if (!world.isRemote && flag && !material.isLiquid())
            {
            	world.func_147480_a(clickX, clickY, clickZ, true);
            }
            if (ModFluids.fluidBlocks[meta] == null)
                return false;
            world.setBlock(clickX, clickY, clickZ, ModFluids.fluidBlocks[meta], 0, 3);

            return true;
        }
    }

    @Override
    public void getSubItems (Item b, CreativeTabs tab, List list)
    {
        for (int i = 0; i < icons.length; i++)
            list.add(new ItemStack(b, 1, i));
    }

    public IIcon[] icons;

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage (int meta)
    {
        return icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons (IIconRegister iconRegister)
    {
        this.icons = new IIcon[textureNames.length];

        for (int i = 0; i < this.icons.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(References.MODID+":bucket_" + textureNames[i]);
        }
    }

    @Override
    public String getUnlocalizedName (ItemStack stack)
    {
        int arr = MathHelper.clamp_int(stack.getItemDamage(), 0, materialNames.length);
        return getUnlocalizedName() + "." + materialNames[arr];
    }

    public static final String[] materialNames = new String[] { "liquefiedAsgardite" };

    public static final String[] textureNames = new String[] { "liquefied_asgardite" };
}