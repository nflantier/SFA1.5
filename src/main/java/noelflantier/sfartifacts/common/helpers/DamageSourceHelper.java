package noelflantier.sfartifacts.common.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;
import noelflantier.sfartifacts.common.entities.EntityShieldThrowCaptain;

public class DamageSourceHelper {
		
	public DamageSourceHelper(){
		
	}
	
	public static DamageSource causeShieldDamages(EntityShieldThrow p_76356_0_, Entity p_76356_1_){
        return (new EntityDamageSourceIndirect("shield", p_76356_0_, p_76356_1_)).setFireDamage().setProjectile();
	}
}
