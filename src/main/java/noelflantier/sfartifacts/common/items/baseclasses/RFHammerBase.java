package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RFHammerBase extends ItemHammerBase implements IEnergyContainerItem{
	
	private int capacity = 0;
	private int maxReceive = 0;
	private int maxExtract = 0;  
	
	@SideOnly(Side.CLIENT)
    private IIcon[] iconHammer;

	public RFHammerBase(){
        this.setHasSubtypes(true);
	}

	/*@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityItemThorHammer(world, location, itemstack);
	}*/
	
	public void setCapacity(int capacity){ this.capacity = capacity;}

	public void setMaxReceive(int maxReceive){ this.maxReceive = maxReceive;}

	public void setMaxExtract(int maxExtract){ this.maxExtract = maxExtract;}

	public int getCapacity(ItemStack stack){ 
		return capacity+ItemNBTHelper.getInteger(stack, "AddedCapacityLevel", 0)*ModConfig.rfAddedPerCapacityUpgrade; 
	}
	
	public int getClassCapacity(){ return this.capacity; }

	public int getMaxExtract(ItemStack stack){ return maxExtract; }

	public int getMaxReceive(ItemStack stack){ return maxReceive; }
	
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
		int energyReceived = Math.min(getCapacity(container) - energy, Math.min(getMaxReceive(container), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			ItemNBTHelper.setInteger(container, "Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
		int energyExtracted = Math.min(energy, Math.min(getMaxExtract(container), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			ItemNBTHelper.setInteger(container, "Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		return ItemNBTHelper.getInteger(container, "Energy", 0);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return getCapacity(container);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !(getEnergyStored(stack) > getMaxEnergyStored(stack));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ((double)getEnergyStored(stack) / (double)getMaxEnergyStored(stack));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {

		ItemStack it = new ItemStack(item, 1, 0);
		it = ItemNBTHelper.setInteger(it, "Energy", 0);
		it = ItemNBTHelper.setInteger(it, "AddedCapacityLevel", 0);
		list.add(it);
		
		if (capacity > 0){
			ItemStack it2 = new ItemStack(item, 1, 1);
			it2 = ItemNBTHelper.setInteger(it2, "Energy", capacity);
			it2 = ItemNBTHelper.setInteger(it2, "AddedCapacityLevel", 0);
			list.add(it2);
		}
	}

    @SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister icon)
    {
        this.iconHammer = new IIcon[2];
        this.iconHammer[0] = icon.registerIcon(References.MODID + ":hammer_empty");
        this.iconHammer[1] = icon.registerIcon(References.MODID + ":hammer_full");
    }

    @SideOnly(Side.CLIENT)
	@Override
    public IIcon getIconFromDamage(int meta)
    {
        return this.iconHammer[meta];
    }
    
	@Override
	public boolean getHasSubtypes() {
		return capacity > 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(String.format("%s/%s RF", ItemNBTHelper.getInteger(stack, "Energy", 0), this.getCapacity(stack)));
	}
}
