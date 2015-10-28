package noelflantier.sfartifacts.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface ISFAEntityMultiPart {
    World func_82194_d();

    boolean attackEntityFromPart(Entity p_70965_1_, DamageSource p_70965_2_, float p_70965_3_);

}
