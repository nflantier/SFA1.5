package noelflantier.sfartifacts.common.entities;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.entities.ai.EntityAIJumpAndCollide;
import noelflantier.sfartifacts.common.entities.ai.EntityAITargetBlock;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class EntityHulk extends EntityMob implements IBossDisplayData, IRangedAttackMob{

    private boolean isRunningAway = false;
    private int runningAwayTimer = -1;
    private int flingAttackTimer = 0;
    private int destroyAroundTimer = 0;
    private int randomDropTimer = 0;
    private int jumpAndDestroyAroundTimer = 0;
    private Random rdm = new Random();
    private EntityAINearestAttackableTarget aiNearest;
    private EntityAIHurtByTarget aiHurt;
    private EntityAITargetBlock aiTargetAway;
    public int countFleshDropped = 0;
    private int animationSmash = 0;
    private int lastLoot = 0;
    
    public EntityHulk(World world){
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIJumpAndCollide(this, EntityPlayer.class, 0.8D, true));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.8D, true));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 100.0F));
        aiNearest = new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true);
        aiHurt = new EntityAIHurtByTarget(this, false);
        aiTargetAway = new EntityAITargetBlock(this, 0,true,false,(int)this.posX+1000,(int)this.posY+100,(int)this.posZ+1000);
        this.targetTasks.addTask(1, aiNearest);
        this.targetTasks.addTask(2, aiHurt);
        
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
		nbt.setInteger("randomDropTimer", randomDropTimer);
		nbt.setInteger("jumpAndDestroyAroundTimer", jumpAndDestroyAroundTimer);		
		nbt.setInteger("lastLoot", lastLoot);
		nbt.setInteger("countFleshDropped", countFleshDropped);
    }

    public void readEntityFromNBT(NBTTagCompound nbt){
        super.readEntityFromNBT(nbt);
		isRunningAway = nbt.getBoolean("isRunningAway");
		flingAttackTimer = nbt.getInteger("flingAttackTimer");
		destroyAroundTimer = nbt.getInteger("destroyAroundTimer");
		randomDropTimer = nbt.getInteger("randomDropTimer");
		jumpAndDestroyAroundTimer = nbt.getInteger("jumpAndDestroyAroundTimer");
		lastLoot = nbt.getInteger("lastLoot");
		countFleshDropped = nbt.getInteger("countFleshDropped");
    }

    public boolean canBreatheUnderwater(){
        return true;
    }
    
    protected void fall(float p_70069_1_){
    	
    }
    
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(12.0D);
    }

    protected void entityInit(){
        super.entityInit();
    }

    protected void collideWithEntity(Entity p_82167_1_){
        this.setAttackTarget((EntityLivingBase)p_82167_1_);
        super.collideWithEntity(p_82167_1_);
    }
    
    protected boolean isAIEnabled(){
        return true;
    }
    
    protected void dropFewItems(boolean p_70628_1_, int loot){
    	dropWithCurrentLoot();
    }

    public void dropWithCurrentLoot(){
    	int nb = 1;
    	if (lastLoot > 0){
            nb += this.rand.nextInt(lastLoot);
        }
        this.dropItem(ModItems.itemHulkFlesh, nb);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource ds, float par2){
        if (isEntityInvulnerable()){
            return false;
        }else{
            Entity entity = ds.getEntity();
            int loot = 0;
        	if (entity instanceof EntityPlayer){
        		loot = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
	        }
        	lastLoot = loot;
        	if(!this.worldObj.isRemote && this.countFleshDropped<3 && this.randomDropTimer<=0 && this.rdm.nextFloat()<=0.1F && par2>10){
        		this.randomDropTimer = 500;
        		this.countFleshDropped +=1;
                this.dropItem(ModItems.itemHulkFlesh, 1);
        	}
            return super.attackEntityFrom(ds, par2);
        }
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
        }else if (p_70103_1_ == 18)
        {
            this.animationSmash = 10;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getAnimationSmash(){
        return this.animationSmash;
    }
    
    @SideOnly(Side.CLIENT)
    public int getAttackFlingTimer(){
        return this.flingAttackTimer;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
       
        if(this.randomDropTimer>0)
        	this.randomDropTimer-=1;
        
        if(this.flingAttackTimer>0)
        	this.flingAttackTimer-=1;
        
        if(this.animationSmash>0)
        	this.animationSmash-=1;
        
        if(this.worldObj.isRemote)
        	return;
        
        if(this.getHealth()-1<this.getMaxHealth()/2 && !this.isRunningAway){
        	this.isRunningAway = true;
        	this.targetTasks.removeTask(aiNearest);
        	this.targetTasks.removeTask(aiHurt);
        	this.targetTasks.addTask(0,aiTargetAway);
        	runningAwayTimer = 100;
        }
        if(this.runningAwayTimer>0)
        	this.runningAwayTimer-=1;

        if(this.runningAwayTimer==50){
        	this.noClip = true;
        	dropWithCurrentLoot();
        	this.motionX *= 15;
        	this.motionZ *= 15;
        	this.motionY+=8.0D;
    	}
        
        if(this.runningAwayTimer==0)
        	this.setDead();
        
        if(this.destroyAroundTimer>0)
        	this.destroyAroundTimer-=1;

        if(this.jumpAndDestroyAroundTimer>0)
        	this.jumpAndDestroyAroundTimer-=1;
        

        if(this.jumpAndDestroyAroundTimer==1)
        	this.smashAround();
        
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
        this.worldObj.setEntityState(this, (byte)18);
        animationSmash = 10;
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
    	if(this.runningAwayTimer>0)
    		return true;
        return super.isEntityInvulnerable();
    }
    
    protected boolean canDespawn(){
        return this.runningAwayTimer<=0?true:false;
    }
    
	@Override
    protected void updateAITasks(){
        super.updateAITasks();
    }
    
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		
	}
}
