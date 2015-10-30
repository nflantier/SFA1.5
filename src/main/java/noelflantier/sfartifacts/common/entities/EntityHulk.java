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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class EntityHulk extends EntityMob implements IBossDisplayData, IRangedAttackMob{

    private boolean isRunningAway = false;
    public int flingAttackTimer = 0;
    
    private static final IEntitySelector attackEntitySelector = new IEntitySelector(){
        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_ instanceof EntityLivingBase;
        }
    };
    
    public EntityHulk(World world){
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIJumpAndCollide(this, EntityPlayer.class, 2.0D, true));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 2.0D, true));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 100.0F));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        
        this.setSize(2.5F, 4.0F);
        this.getNavigator().setCanSwim(true);
        this.isImmuneToFire = true;
        this.experienceValue = 50;
    }
    
    public void writeEntityToNBT(NBTTagCompound p_70014_1_){
        super.writeEntityToNBT(p_70014_1_);
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_){
        super.readEntityFromNBT(p_70037_1_);
    }
    
    protected void fall(float p_70069_1_){
    	
    }
    
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(20.0D);
    }

    protected void entityInit(){
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    protected void collideWithEntity(Entity p_82167_1_){
        this.setAttackTarget((EntityLivingBase)p_82167_1_);
        super.collideWithEntity(p_82167_1_);
    }
    
    protected boolean isAIEnabled(){
        return true;
    }   
    
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_){
        this.dropItem(ModItems.itemHulkFlesh, 1);
    }
    
    protected void attackEntity(Entity p_70785_1_, float p_70785_2_){
    	super.attackEntity(p_70785_1_, p_70785_2_);
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY){
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }
   
    public boolean attackEntityAsMob(Entity p_70652_1_){

    	this.flingAttackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
    	
    	boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(0.1));

        if (flag)
        {
            p_70652_1_.motionY += 0.6000000059604645D;
        }
        return flag;
    }
    
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_){
        if (p_70103_1_ == 4)
        {
            this.flingAttackTimer = 10;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public int getAttackFlingTimer(){
        return this.flingAttackTimer;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
       
        if(this.getHealth()<this.getMaxHealth()/2){
        	this.isRunningAway = true;
        }
        
        if(this.flingAttackTimer>0)
        	this.flingAttackTimer-=1;
        /*if (this.getDataWatcher().getWatchableObjectInt(17) > 0){
        	this.getDataWatcher().updateObject(17, this.getDataWatcher().getWatchableObjectInt(17)-1);
            
        }*/
    }

    protected boolean canDespawn(){
        return false;
    }
    
	@Override
    protected void updateAITasks(){
        super.updateAITasks();
    }
    
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		
	}
}
