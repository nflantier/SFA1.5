package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModPlayerStats;

public class ItemHulkFlesh  extends ItemFood{

	public static int tickHulkEffect = 1500;
	public static int jumpBoostDuration = tickHulkEffect;
	    
	public ItemHulkFlesh(){
        super(100,100,false);
		this.setUnlocalizedName("itemHulkFlesh");
		this.setTextureName(References.MODID+":hulk_flesh");
		this.setAlwaysEdible();
		this.setCreativeTab(SFArtifacts.sfTabs);
		MinecraftForge.EVENT_BUS.register(this);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack){
        return true;
    }

    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.epic;
    }   
    
	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player == null || event.entity.worldObj.isRemote)
				return;
			
			ModPlayerStats stats = ModPlayerStats.get(player);
			if(stats!=null && stats.tickHasHulkFleshEffect>0){
				event.distance = 0;
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
	    	EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player == null || !event.entity.worldObj.isRemote)
				return;
			
			ModPlayerStats stats = ModPlayerStats.get(player);
			if(stats==null)
				return;
			if(stats.tickHasHulkFleshEffect>0){
				player.motionY += 1.45;
				player.motionX *= 5.2;
				player.motionZ *= 5.2;
			}
		}
	}
	
    @Override
	public int getMaxItemUseDuration(ItemStack stack){
        return 64;
    }
    
    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player){
		ModPlayerStats stats = ModPlayerStats.get(player);
		if(stats.tickJustEatHulkFlesh<=0){
			stats.tickHasHulkFleshEffect = tickHulkEffect;
			stats.tickJustEatHulkFlesh = 10;
	    	if (!world.isRemote){
	    		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, tickHulkEffect, 20));
	            player.addPotionEffect(new PotionEffect(Potion.resistance.id, tickHulkEffect, 1));
	            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, tickHulkEffect, 1));
	    		//player.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, tickHulkEffect, 4));
	    		player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, tickHulkEffect, 10));
	    		player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, tickHulkEffect, 6));
	    		player.addPotionEffect(new PotionEffect(Potion.field_76434_w.id, tickHulkEffect, 9));
	        }
		}
        super.onFoodEaten(stack, world, player);
    }
}
