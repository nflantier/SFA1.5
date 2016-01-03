package noelflantier.sfartifacts.common.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;

public class EntityShieldThrowCaptain extends EntityShieldThrow{
	
	public EntityShieldThrowCaptain(World w) {
		super(w);
	}
	public EntityShieldThrowCaptain(World w, EntityLivingBase p){
		super(w,p);
	}	
	public EntityShieldThrowCaptain(World w, EntityLivingBase p, int slot){
		super(w,p,slot);
	}
	
}
