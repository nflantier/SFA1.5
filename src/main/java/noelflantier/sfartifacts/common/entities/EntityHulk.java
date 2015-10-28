package noelflantier.sfartifacts.common.entities;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class EntityHulk extends EntityMob implements IBossDisplayData, IRangedAttackMob{

    private int attackTimerSmash;
    
    private static final IEntitySelector attackEntitySelector = new IEntitySelector(){
        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_ instanceof EntityLivingBase;
        }
    };
    
    public EntityHulk(World world)
    {
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIJumpAndCollide(this, EntityPlayer.class, 2.0D, true));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 2.0D, true));
        
        //this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityLiving.class, 2.0D, true));
        //this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        //this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 100.0F));
        //this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
        
        this.setSize(2.5F, 4.0F);
        this.isImmuneToFire = true;
        this.experienceValue = 150;
        /*this.setHealth(this.getMaxHealth());
        this.getNavigator().setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 40.0F));
        this.tasks.addTask(5, new EntityAIAttackOnCollide(this, EntityLiving.class, 3.0D, true));
        this.tasks.addTask(6, new EntityAILookIdle(this)); 
        
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, false, attackEntitySelector));
        
        this.experienceValue = 150;*/
    }

    protected void fall(float p_70069_1_){
    	
    }
    
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }

    protected void entityInit(){
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }

    protected void collideWithEntity(Entity p_82167_1_){
        this.setAttackTarget((EntityLivingBase)p_82167_1_);
        super.collideWithEntity(p_82167_1_);
    }

    public void addPotionEffect(PotionEffect p_70690_1_) {
    	
    }
    
    protected boolean isAIEnabled(){
        return true;
    }   
    
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_){
        this.dropItem(ModItems.itemAsgardiumPearl, 1);
    }
    
    protected void attackEntity(Entity p_70785_1_, float p_70785_2_){
    	super.attackEntity(p_70785_1_, p_70785_2_);
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY){
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
        else if (p_70785_2_ < 30.0F){
        }
    }
    
    /*@SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 4)
        {
            this.attackTimerFling = 10;
            //this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }*/
    
    public boolean attackEntityAsMob(Entity p_70652_1_){
    	
        //this.attackTimerFling = 10;
        //this.worldObj.setEntityState(this, (byte)4);  
        this.getDataWatcher().updateObject(16, 10);
    	boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(0.1));

        if (flag)
        {
            p_70652_1_.motionY += 0.6000000059604645D;
        }
        return flag;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
       
        if (this.getDataWatcher().getWatchableObjectInt(16) > 0)
        {
        	this.getDataWatcher().updateObject(16, this.getDataWatcher().getWatchableObjectInt(16)-1);
            
        }
        if (this.getDataWatcher().getWatchableObjectInt(17) > 0)
        {
        	hulkSmash();
        	this.getDataWatcher().updateObject(17, this.getDataWatcher().getWatchableObjectInt(17)-1);
            
        }
        /*if (this.attackTimerFling > 0)
        {
            --this.attackTimerFling;
            
        }else{
            this.getDataWatcher().updateObject(16, 0);
        }*/
    }
    
    public void hulkSmash(){
    	int smashtime = this.getDataWatcher().getWatchableObjectInt(17);
    }
    
    /*@SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimerFling;
    }*/
        
    /*
    protected void entityInit(){
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }

    	
    public int getWatchedTargetId(int p_82203_1_){
        return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
    }
    
    public void onLivingUpdate(){
        //this.motionY *= 0.6000000238418579D;
        double d1;
        double d3;
        double d5;

        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0)
        {
            Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));

            if (entity != null)
            {
                if (this.posY < entity.posY || this.posY < entity.posY + 5.0D)
                {
                    if (this.motionY < 0.0D)
                    {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                double d0 = entity.posX - this.posX;
                d1 = entity.posZ - this.posZ;
                d3 = d0 * d0 + d1 * d1;

                if (d3 > 9.0D)
                {
                    d5 = (double)MathHelper.sqrt_double(d3);
                    this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
        {
            this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
        }

        super.onLivingUpdate();
        int i;

        for (i = 0; i < 2; ++i)
        {
            this.field_82218_g[i] = this.field_82221_e[i];
            this.field_82217_f[i] = this.field_82220_d[i];
        }

        int j;

        for (i = 0; i < 2; ++i)
        {
            j = this.getWatchedTargetId(i + 1);
            Entity entity1 = null;

            if (j > 0)
            {
                entity1 = this.worldObj.getEntityByID(j);
            }

            if (entity1 != null)
            {
                d1 = this.func_82214_u(i + 1);
                d3 = this.func_82208_v(i + 1);
                d5 = this.func_82213_w(i + 1);
                double d6 = entity1.posX - d1;
                double d7 = entity1.posY + (double)entity1.getEyeHeight() - d3;
                double d8 = entity1.posZ - d5;
                double d9 = (double)MathHelper.sqrt_double(d6 * d6 + d8 * d8);
                float f = (float)(Math.atan2(d8, d6) * 180.0D / Math.PI) - 90.0F;
                float f1 = (float)(-(Math.atan2(d7, d9) * 180.0D / Math.PI));
                this.field_82220_d[i] = this.func_82204_b(this.field_82220_d[i], f1, 40.0F);
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], f, 10.0F);
            }
            else
            {
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], this.renderYawOffset, 10.0F);
            }
        }

        boolean flag = false;

        for (j = 0; j < 3; ++j)
        {
            double d10 = this.func_82214_u(j);
            double d2 = this.func_82208_v(j);
            double d4 = this.func_82213_w(j);
            this.worldObj.spawnParticle("smoke", d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

            if (flag && this.worldObj.rand.nextInt(4) == 0)
            {
                this.worldObj.spawnParticle("mobSpell", d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (this.func_82212_n() > 0)
        {
            for (j = 0; j < 3; ++j)
            {
                this.worldObj.spawnParticle("mobSpell", this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (double)(this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }
    
    protected void updateAITasks(){
        int i;

        if (this.func_82212_n() > 0)
        {
            i = this.func_82212_n() - 1;

            if (i <= 0)
            {
                this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
                this.worldObj.playBroadcastSound(1013, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }

            this.func_82215_s(i);

            if (this.ticksExisted % 10 == 0)
            {
                this.heal(10.0F);
            }
        }
        else
        {
            super.updateAITasks();
            int i1;

            for (i = 1; i < 3; ++i)
            {
                if (this.ticksExisted >= this.field_82223_h[i - 1])
                {
                    this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);

                    if (this.worldObj.difficultySetting == EnumDifficulty.NORMAL || this.worldObj.difficultySetting == EnumDifficulty.HARD)
                    {
                        int k2 = i - 1;
                        int l2 = this.field_82224_i[i - 1];
                        this.field_82224_i[k2] = this.field_82224_i[i - 1] + 1;

                        if (l2 > 15)
                        {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)f, this.posX + (double)f);
                            double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)f1, this.posY + (double)f1);
                            double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)f, this.posZ + (double)f);
                            this.func_82209_a(i + 1, d0, d1, d2, true);
                            this.field_82224_i[i - 1] = 0;
                        }
                    }

                    i1 = this.getWatchedTargetId(i);

                    if (i1 > 0)
                    {
                        Entity entity = this.worldObj.getEntityByID(i1);

                        if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0D && this.canEntityBeSeen(entity))
                        {
                            this.func_82216_a(i + 1, (EntityLivingBase)entity);
                            this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                            this.field_82224_i[i - 1] = 0;
                        }
                        else
                        {
                            this.func_82211_c(i, 0);
                        }
                    }
                    else
                    {
                        List list = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(20.0D, 8.0D, 20.0D), attackEntitySelector);

                        for (int k1 = 0; k1 < 10 && !list.isEmpty(); ++k1)
                        {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));

                            if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase))
                            {
                                if (entitylivingbase instanceof EntityPlayer)
                                {
                                    if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                                    {
                                        this.func_82211_c(i, entitylivingbase.getEntityId());
                                    }
                                }
                                else
                                {
                                    this.func_82211_c(i, entitylivingbase.getEntityId());
                                }

                                break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (this.getAttackTarget() != null)
            {
                this.func_82211_c(0, this.getAttackTarget().getEntityId());
            }
            else
            {
                this.func_82211_c(0, 0);
            }

            if (this.field_82222_j > 0)
            {
                --this.field_82222_j;

                if (this.field_82222_j == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    i = MathHelper.floor_double(this.posY);
                    i1 = MathHelper.floor_double(this.posX);
                    int j1 = MathHelper.floor_double(this.posZ);
                    boolean flag = false;

                    for (int l1 = -1; l1 <= 1; ++l1)
                    {
                        for (int i2 = -1; i2 <= 1; ++i2)
                        {
                            for (int j = 0; j <= 3; ++j)
                            {
                                int j2 = i1 + l1;
                                int k = i + j;
                                int l = j1 + i2;
                                Block block = this.worldObj.getBlock(j2, k, l);

                                if (!block.isAir(worldObj, j2, k, l) && block.canEntityDestroy(worldObj, j2, k, l, this))
                                {
                                    flag = this.worldObj.func_147480_a(j2, k, l, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag)
                    {
                        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
                    }
                }
            }

            if (this.ticksExisted % 20 == 0)
            {
                this.heal(1.0F);
            }
        }
    }

    public void func_82211_c(int p_82211_1_, int p_82211_2_)
    {
        this.dataWatcher.updateObject(17 + p_82211_1_, Integer.valueOf(p_82211_2_));
    }
    
    @SideOnly(Side.CLIENT)
    public float func_82207_a(int p_82207_1_)
    {
        return this.field_82221_e[p_82207_1_];
    }

    @SideOnly(Side.CLIENT)
    public float func_82210_r(int p_82210_1_)
    {
        return this.field_82220_d[p_82210_1_];
    }

    public int func_82212_n()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public void func_82215_s(int p_82215_1_)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
    }

    
    private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_)
    {
        float f3 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);

        if (f3 > p_82204_3_)
        {
            f3 = p_82204_3_;
        }

        if (f3 < -p_82204_3_)
        {
            f3 = -p_82204_3_;
        }

        return p_82204_1_ + f3;
    }

    private void func_82216_a(int p_82216_1_, EntityLivingBase p_82216_2_)
    {
        this.func_82209_a(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F);
    }

    private void func_82209_a(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_)
    {
        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
        double d3 = this.func_82214_u(p_82209_1_);
        double d4 = this.func_82208_v(p_82209_1_);
        double d5 = this.func_82213_w(p_82209_1_);
        double d6 = p_82209_2_ - d3;
        double d7 = p_82209_4_ - d4;
        double d8 = p_82209_6_ - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d6, d7, d8);

        if (p_82209_8_)
        {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.worldObj.spawnEntityInWorld(entitywitherskull);
    }
    
    private double func_82214_u(int p_82214_1_)
    {
        if (p_82214_1_ <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double)f1 * 1.3D;
        }
    }

    private double func_82208_v(int p_82208_1_)
    {
        return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double func_82213_w(int p_82213_1_)
    {
        if (p_82213_1_ <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double)f1 * 1.3D;
        }
    }*/

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		
	}
}
