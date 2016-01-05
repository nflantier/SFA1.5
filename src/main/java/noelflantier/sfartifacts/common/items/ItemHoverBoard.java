package noelflantier.sfartifacts.common.items;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModPlayerStats;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Utils;

public class ItemHoverBoard extends ItemSFA implements IEnergyContainerItem{

	private int capacity = ModConfig.capacityHoverBoard;
	private int maxReceive = ModConfig.capacityHoverBoard;
	private int maxExtract = ModConfig.capacityHoverBoard;  
	private static Map<EntityPlayer, EntityHoverBoard> spawnedHoverBoardsMap = new MapMaker().weakKeys().weakValues().makeMap();

	public ItemHoverBoard() {
		super("Hoverboard");
		this.setUnlocalizedName("itemHoverBoard");
		this.setTextureName(References.MODID+":hoverboard");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && player != null) {
			EntityHoverBoard hoverboard = spawnedHoverBoardsMap.get(player);
			if (hoverboard != null) despawnHoverBoard(player, hoverboard);
			else {
				if(this.getEnergyStored(stack)>0)
					spawnHoverBoard(player);
			}
		}
		return stack;
	}

	private static void despawnHoverBoard(EntityPlayer player, EntityHoverBoard hoverboard) {
		hoverboard.setDead();
		spawnedHoverBoardsMap.remove(player);
	}

	private static void spawnHoverBoard(EntityPlayer player) {
		EntityHoverBoard hoverboard = new EntityHoverBoard(player.worldObj, player, player.inventory.currentItem);
		hoverboard.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYaw);
		player.worldObj.spawnEntityInWorld(hoverboard);
		spawnedHoverBoardsMap.put(player, hoverboard);
	}
	
	public int getCapacity(ItemStack stack){ return capacity; }
	public int getMaxExtract(ItemStack stack){ return maxExtract; }
	public int getMaxReceive(ItemStack stack){ return maxReceive; }
	
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
		if (player != null && !player.worldObj.isRemote) {
			EntityHoverBoard hoverboard = spawnedHoverBoardsMap.get(player);
			if (hoverboard != null) despawnHoverBoard(player, hoverboard);
		}
        return true;
    }
	
	@Override
	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
		int energyReceived = Math.min(getCapacity(stack) - energy, Math.min(getMaxReceive(stack), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			ItemNBTHelper.setInteger(stack, "Energy", energy);
		}
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
		int energyExtracted = Math.min(energy, Math.min(getMaxExtract(stack), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			ItemNBTHelper.setInteger(stack, "Energy", energy);
		}
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, "Energy", 0);
	}
	
	@Override
	public int getMaxEnergyStored(ItemStack stack) {
		return getCapacity(stack);
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
		list.add(it);
		
		if (capacity > 0){
			ItemStack it2 = new ItemStack(item, 1, 1);
			it2 = ItemNBTHelper.setInteger(it2, "Energy", capacity);
			list.add(it2);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(String.format("%s/%s RF", ItemNBTHelper.getInteger(stack, "Energy", 0), this.getCapacity(stack)));
	}
}