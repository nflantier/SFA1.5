package noelflantier.sfartifacts.common.items;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.helpers.BaublesHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class ItemMightyHulkRing  extends ItemSFA implements IBauble{
	

	private static final String TAG_HASHCODE = "playerHashcode";
	Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	
	public ItemMightyHulkRing() {
		super("Mighty Hulk Ring");
		this.setUnlocalizedName("itemMightyHulkRing");
		this.setTextureName(References.MODID+":mighty_hulk_ring");
		this.setMaxStackSize(1);
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), 
				new AttributeModifier("Bauble modifier", 20, 0));
		attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), 
				new AttributeModifier("Bauble modifier", 0.13, 0));
		MinecraftForge.EVENT_BUS.register(this);
		
	}

	@SubscribeEvent
	public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
    	EntityPlayer player = event.entityPlayer;
		if(player == null || event.block==Blocks.bedrock || event.entityPlayer.getCurrentEquippedItem()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			player.getFoodStats().addExhaustion(0.05F);
			event.success = true;
		}
	}

	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
    	EntityPlayer player = event.entityPlayer;
		if(player == null || event.block==Blocks.bedrock || event.entityPlayer.getCurrentEquippedItem()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			float h = event.block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z);
			event.newSpeed = h*8.5F<15?15:h*8.5F;
		}
	}

	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player == null)
				return;
			
			IInventory ip = BaublesApi.getBaubles(player);
			if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
				event.distance = 0;
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
	    	EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player == null)
				return;
			IInventory ip = BaublesApi.getBaubles(player);
			if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
				player.motionY += 0.5;
				player.motionX *= 4.3;
				player.motionZ *= 4.3;
			}
		}
	}
	
	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
    	EntityPlayer player = event.entityPlayer;
		if(player == null || event.target instanceof EntityLiving==false || event.entityPlayer.getCurrentEquippedItem()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			event.target.hitByEntity(event.entity);
            float damages = (float)event.entityLiving.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            damages = damages>1?damages*5:10;
            int knockback = 4;
            event.target.attackEntityFrom(DamageSource.causePlayerDamage(event.entityPlayer), damages);
            event.target.addVelocity((double)(-MathHelper.sin(event.entity.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.3D, (double)(MathHelper.cos(event.entity.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
            event.entityPlayer.addExhaustion(0.3F);
			event.setCanceled(true);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		//list.add(String.format("Make your finger angry"));
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			setLastPlayerHashcode(stack, player.hashCode());
		}
		if (player.getHealth() < player.getMaxHealth())//REGEN
			player.heal(1.0F);
         	 
		if(player.stepHeight <1)//STEP
			player.stepHeight = 1F;
		
	}

	public static int getLastPlayerHashcode(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, TAG_HASHCODE, 0);
	}

	public static void setLastPlayerHashcode(ItemStack stack, int hash) {
		ItemNBTHelper.setInteger(stack, TAG_HASHCODE, hash);
	}
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		player.getAttributeMap().applyAttributeModifiers(attributes);
		setLastPlayerHashcode(stack, player.hashCode());
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.getAttributeMap().removeAttributeModifiers(attributes);
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
}