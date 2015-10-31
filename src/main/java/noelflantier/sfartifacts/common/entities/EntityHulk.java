package noelflantier.sfartifacts.common.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.entities.ai.EntityAIJumpAndCollide;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class EntityHulk extends EntityMob implements IBossDisplayData, IRangedAttackMob{

    private boolean isRunningAway = false;
    private int flingAttackTimer = 0;
    private int destroyAroundTimer = 0;
    private int jumpAndDestroyAroundTimer = 0;
    private Random rdm = new Random();
    
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
        
        this.setSize(2.5F, 4.5F);
        this.getNavigator().setCanSwim(true);
        this.isImmuneToFire = true;
        this.experienceValue = 50;
    }
    
    public void writeEntityToNBT(NBTTagCompound nbt){
        super.writeEntityToNBT(nbt);
		nbt.setBoolean("isRunningAway", isRunningAway);
		nbt.setInteger("flingAttackTimer", flingAttackTimer);
		nbt.setInteger("destroyAroundTimer", destroyAroundTimer);
		nbt.setInteger("jumpAndDestroyAroundTimer", jumpAndDestroyAroundTimer);		
    }

    public void readEntityFromNBT(NBTTagCompound nbt){
        super.readEntityFromNBT(nbt);
		isRunningAway = nbt.getBoolean("isRunningAway");
		flingAttackTimer = nbt.getInteger("flingAttackTimer");
		destroyAroundTimer = nbt.getInteger("destroyAroundTimer");
		jumpAndDestroyAroundTimer = nbt.getInteger("jumpAndDestroyAroundTimer");
    }
    
    protected void fall(float p_70069_1_){
    	
    }
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
        if (isEntityInvulnerable()){
            return false;
        }{
            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
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
    	
    	boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 
    			(float)(this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()));

        if (flag){
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
       

        if(this.flingAttackTimer>0)
        	this.flingAttackTimer-=1;
        
        if(this.worldObj.isRemote)
        	return;
        
        if(this.getHealth()<this.getMaxHealth()/2){
        	this.isRunningAway = true;
        }
        
        if(this.destroyAroundTimer>0)
        	this.destroyAroundTimer-=1;

        if(this.jumpAndDestroyAroundTimer>0)
        	this.jumpAndDestroyAroundTimer-=1;
        

        if(this.jumpAndDestroyAroundTimer==1)
        	this.smashAround();
        
        //System.out.println(this.destroyAroundTimer+"   "+this.getNavigator().getPath()+"    "+this.getAttackTarget());
        if(this.destroyAroundTimer==0){
	        if(this.getAttackTarget()!=null){
	        	if(this.getNavigator().getPath()==null || this.getNavigator().getPathToEntityLiving(this.getAttackTarget())==null){
	        		hulkSmash(true);
	        	}
	        }else if(this.getAttackTarget()==null){
	        	//if(this.getNavigator().getPath()==null)
	        	hulkSmash(false);
	        }
        }
        /*if (this.getDataWatcher().getWatchableObjectInt(17) > 0){
        	this.getDataWatcher().updateObject(17, this.getDataWatcher().getWatchableObjectInt(17)-1);
            
        }*/
    }

    public void smashAround(){
        int hulkx = MathHelper.floor_double(this.posX);
        int hulky = MathHelper.floor_double(this.posY);
        int hulkz = MathHelper.floor_double(this.posZ);
		//this.worldObj.newExplosion(this, hulkx+ox, hulky+oy, hulkz+oz, 4.0F, true, true);
        boolean flag = false;

        for (int by = 0; by <= 4+rdm.nextInt(3); ++by)
        {
            for (int bz = -4-rdm.nextInt(3); bz <= 4+rdm.nextInt(3); ++bz)
            {
                for (int bx = -4-rdm.nextInt(3); bx <= 4+rdm.nextInt(3); ++bx)
                {
                    int wbx = hulkx + bx;
                    int wby = hulky + by;
                    int wbz = hulkz + bz;
                    Block block = this.worldObj.getBlock(wbx, wby, wbz);

                    if (!block.isAir(worldObj, wbx, wby, wbz) && block.canEntityDestroy(worldObj, wbx, wby, wbz, this))
                    {
                        flag = this.worldObj.func_147480_a(wbx, wby, wbz, true) || flag;
                    }
                }
            }
        }
    }
    
    public void jumpAndSmashAround(){
    	this.motionY+=0.6000000059604645D;
    	jumpAndDestroyAroundTimer = 20;
    }
    
    public int getEntityOrientation(Entity e){
		int direction = MathHelper.floor_double((double) (e.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;			
		int s = -1;
		if(direction == 0)s=2;//NORTH
		if(direction == 1)s=5;//EAST
		if(direction == 2)s=3;//SOUTH
		if(direction == 3)s=4;//WEST
    	return s;
    }
    
    public void hulkSmash(boolean hasentity){
    	int s = getEntityOrientation(this);
		int ox = ForgeDirection.getOrientation(s).offsetX;
		int oy = ForgeDirection.getOrientation(s).offsetY;
		int oz = ForgeDirection.getOrientation(s).offsetZ;

		if(hasentity){
			if(this.getAttackTarget().posY>this.posY){
				jumpAndSmashAround();
			}else if(this.getAttackTarget().posY<this.posY){
				jumpAndSmashAround();
			}else{
				smashAround();
			}
	    	this.destroyAroundTimer = 400;
		}else{
			smashAround();
	    	this.destroyAroundTimer = 110;
		}
    }

    public boolean isEntityInvulnerable(){
        return super.isEntityInvulnerable();
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
