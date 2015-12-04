package noelflantier.sfartifacts.common.items;

import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.ShieldHelper;

public class ItemVibraniumShield  extends ItemSFA{

	public static ToolMaterial SHIELD_1 = EnumHelper.addToolMaterial("SHIELD_1", 0, -1, 280.0F, 50.0F, -1);
    public static final AttributeModifier knockbackModifier = (new AttributeModifier( "KnockBack modifier", 100D, 0)).setSaved(false);
	
	public ItemVibraniumShield() {
		super("Vibranium Shield");
		this.setUnlocalizedName("itemVibraniumShield");
		this.setTextureName(References.MODID+":vibranium_shield");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player){

		if (!ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
			if(player.isSneaking()){
				return stack;
			}			
			if(!w.isRemote){
				ItemNBTHelper.setBoolean(stack, "IsThrown", true);
				ShieldHelper.startThrowing(w, player);
			}
		}
		return stack;
	}

	@Override
    public void onUpdate(ItemStack stack, World w, Entity entity, int p_77663_4_, boolean p_77663_5_) {
		if (ItemNBTHelper.getBoolean(stack, "IsThrown", false))
			return;
		if(!ModConfig.isShieldBlockOnlyWhenShift && !ItemNBTHelper.getBoolean(stack, "CanBlock", false))
			ItemNBTHelper.setBoolean(stack, "CanBlock", true);
		
        if (entity instanceof EntityPlayer && ModConfig.isShieldBlockOnlyWhenShift){
            EntityPlayer player = (EntityPlayer)entity;
			if(player.isSneaking()){
				ItemNBTHelper.setBoolean(stack, "CanBlock", true);
			}else
				if(ItemNBTHelper.getBoolean(stack, "CanBlock", false))
					ItemNBTHelper.setBoolean(stack, "CanBlock", false);
        }
		
		//SHIELD
    }
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it = new ItemStack(item, 1, 0);
		if(!ModConfig.isShieldBlockOnlyWhenShift)
			it = ItemNBTHelper.setBoolean(it, "CanBlock", true);
		else
			it = ItemNBTHelper.setBoolean(it, "CanBlock", false);
		it = ItemNBTHelper.setBoolean(it, "IsThrown", false);
		list.add(it);
	}
	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase el, EntityLivingBase el2){
        return true;
    }
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(entity instanceof EntityItem==false)
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), SHIELD_1.getDamageVsEntity());
		return true;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
        return !ItemNBTHelper.getBoolean(stack, "IsThrown", false);
    }

	@Override
    public Multimap getAttributeModifiers(ItemStack stack){
		if(ItemNBTHelper.getBoolean(stack, "IsThrown", false) || !ItemNBTHelper.getBoolean(stack, "CanBlock", false))
			return super.getAttributeModifiers(stack);
		
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), knockbackModifier);
        return multimap;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		
		if(ModConfig.isShieldBlockOnlyWhenShift)
			list.add(String.format("You have to press shift to block damages."));
	}
}
