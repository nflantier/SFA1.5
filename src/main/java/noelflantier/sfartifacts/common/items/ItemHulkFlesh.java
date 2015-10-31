package noelflantier.sfartifacts.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModPlayerStats;

public class ItemHulkFlesh  extends ItemFood{

	public static int tickHulkEffect = 1000;
	public static int jumpBoostDuration = tickHulkEffect;
	    
	public ItemHulkFlesh(){
        super(100,100,false);
		this.setUnlocalizedName("itemHulkFlesh");
		this.setTextureName(References.MODID+":hulk_flesh");
		this.setAlwaysEdible();
		this.setCreativeTab(SFArtifacts.sfTabs);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack){
        return true;
    }

    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.epic;
    }   

    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player){
		ModPlayerStats stats = ModPlayerStats.get(player);
		stats.tickHasHulkFleshEffect += tickHulkEffect;
    	if (!world.isRemote){
    		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, tickHulkEffect, 20));
            player.addPotionEffect(new PotionEffect(Potion.resistance.id, tickHulkEffect, 20));
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, tickHulkEffect, 1));
    		//player.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, tickHulkEffect, 10));
    		player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, tickHulkEffect, 10));
    		player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, tickHulkEffect, 20));
    		player.addPotionEffect(new PotionEffect(Potion.field_76434_w.id, tickHulkEffect, 20));
        }
    }
}
