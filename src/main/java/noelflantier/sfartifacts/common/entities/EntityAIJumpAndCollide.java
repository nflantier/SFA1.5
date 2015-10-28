package noelflantier.sfartifacts.common.entities;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIJumpAndCollide extends EntityAIBase{
    World worldObj;
    EntityCreature attacker;
    int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    PathEntity entityPathEntity;
    Class classTarget;
    private int field_75445_i;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;
    public Random rand = new Random();
    private boolean isAttackerJumping;

    private int failedPathFindingPenalty;
    
    public EntityAIJumpAndCollide(EntityCreature p_i1635_1_, Class p_i1635_2_, double p_i1635_3_, boolean p_i1635_5_)
    {
        this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
        this.classTarget = p_i1635_2_;
    }

    public EntityAIJumpAndCollide(EntityCreature p_i1636_1_, double p_i1636_2_, boolean p_i1636_4_)
    {
        this.attacker = p_i1636_1_;
        this.worldObj = p_i1636_1_.worldObj;
        this.speedTowardsTarget = p_i1636_2_;
        this.longMemory = p_i1636_4_;
        this.setMutexBits(3);
    }

	@Override
	public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass()))
        {
            return false;
        }
        else
        {
            if (-- this.field_75445_i <= 0)
            {
               this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
               this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
               return this.entityPathEntity != null;
            }
            else
            {
                return true;
            }
        }
    }
	
	public boolean continueExecuting(){
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistance(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ))));
    }
	
    public void startExecuting(){
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.field_75445_i = 0;
    }
    
    public void resetTask(){
        this.attacker.getNavigator().clearPathEntity();
    } 
    
    public void updateTask(){
    	if(this.isAttackerJumping)
    		this.isAttackerJumping = !this.attacker.onGround;
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
        double d1 = (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + entitylivingbase.width);
        --this.field_75445_i;

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && 
        		this.field_75445_i <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D 
        		|| entitylivingbase.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D 
        		|| this.attacker.getRNG().nextFloat() < 0.05F))
        {
            this.field_151497_i = entitylivingbase.posX;
            this.field_151495_j = entitylivingbase.boundingBox.minY;
            this.field_151496_k = entitylivingbase.posZ;
            this.field_75445_i = this.attacker.getRNG().nextInt(7);

            if (this.attacker.getNavigator().getPath() != null)
            {
                PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
                {
                    failedPathFindingPenalty = 0;
                }
                else
                {
                    failedPathFindingPenalty += 10;
                    attackerJumpToEntity(entitylivingbase);
                }
            }
            else
            {
                failedPathFindingPenalty += 10;
                attackerJumpToEntity(entitylivingbase);
            }

            if (d0 > 1024.0D)
            {
                this.field_75445_i += 10;
                attackerJumpToEntity(entitylivingbase);
            }
            else if (d0 > 256.0D)
            {
                this.field_75445_i += 5;
                attackerJumpToEntity(entitylivingbase);
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
            {
                this.field_75445_i += 15;
                attackerJumpToEntity(entitylivingbase);
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);

        if (d0 <= d1 && this.attackTick <= 20)
        {
            this.attackTick = 20;

            if (this.attacker.getHeldItem() != null)
            {
                this.attacker.swingItem();
            }

            this.attacker.attackEntityAsMob(entitylivingbase);
        }
    }
    
    public void attackerJumpToEntity(EntityLivingBase entity){
    	if(this.isAttackerJumping)
    		return;
    	this.isAttackerJumping = true;
    	setAttackerPointing(entity.posX-this.attacker.posX,entity.posY-this.attacker.posY,entity.posZ-this.attacker.posZ,1.5F,1.0F);
    	this.attacker.motionY += 0.6000000059604645D;
    }    
    
    public void setAttackerPointing(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_){
        float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= (double)f2;
        p_70186_3_ /= (double)f2;
        p_70186_5_ /= (double)f2;
        p_70186_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_1_ *= (double)p_70186_7_;
        p_70186_3_ *= (double)p_70186_7_;
        p_70186_5_ *= (double)p_70186_7_;
        this.attacker.motionX = p_70186_1_;
        this.attacker.motionY = p_70186_3_;
        this.attacker.motionZ = p_70186_5_;
        float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        this.attacker.prevRotationYaw = this.attacker.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
        this.attacker.prevRotationPitch = this.attacker.rotationPitch = (float)(Math.atan2(p_70186_3_, (double)f3) * 180.0D / Math.PI);
    }
}
