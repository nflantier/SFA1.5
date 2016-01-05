package noelflantier.sfartifacts.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
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
import noelflantier.sfartifacts.common.helpers.DamageSourceHelper;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;
import noelflantier.sfartifacts.common.items.baseclasses.MiningHammerBase;
import noelflantier.sfartifacts.common.items.baseclasses.ToolHammerBase;

public class EntityShieldThrow extends EntityThrowable implements IEntityAdditionalSpawnData{

	private int theSlot;
	private double orX,orY,orZ,curhyp;
	public int tickTravel = 20;
	public int currentTickTravel = 0;
	public boolean isCommingBack = false;
	public float angleYaw = 0;
	public float anglePitch = 0;
	public boolean blockMop = false;
	
	public EntityShieldThrow(World w) {
		super(w);
		this.isImmuneToFire = true;
		this.noClip = false;
	}
	
	public EntityShieldThrow(World w, EntityLivingBase p){
		super(w,p);
		this.orX = p.posX;
		this.orY = p.posY;
		this.orZ = p.posZ;
		this.isImmuneToFire = true;
		this.noClip = false;
		if(w.isRemote)
			this.setVelocity(this.motionX*1.5, this.motionY*1.5,this.motionZ*1.5);
	}	
	
	public EntityShieldThrow(World w, EntityLivingBase p, int slot){
		this(w,p);
		this.theSlot = slot;
	}
	
	public void setV(double vx, double vy, double vz){
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(vy, (double)f) * 180.0D / Math.PI);
        }
	}
	
    public boolean canBeCollidedWith(){
        return true;
    }
    
	@Override
    public boolean isEntityInvulnerable(){
        return true;
    }
	
	protected float getGravityVelocity(){
        return 0.0F;
    }

	@Override
    public void onUpdate(){
		super.onUpdate();
		//this.setDead();
		
		this.currentTickTravel+=1;
		if(this.currentTickTravel>this.tickTravel)
			this.isCommingBack=true;
		if(this.isCommingBack){
			if(this.getThrower()!=null){	
				double xth = this.getThrower().posX;
				double yth = this.getThrower().posY+0.5;
				double zth = this.getThrower().posZ;
				this.curhyp = Math.sqrt( (this.posX-xth)*(this.posX-xth) + (this.posY-yth)*(this.posY-yth) + (this.posZ-zth)*(this.posZ-zth) );
		        this.setThrowableHeading(xth-this.posX,yth-this.posY,zth-this.posZ, 1.5F, 1.0F);
				if(this.curhyp<1.5){
					if(this.isEntityAlive())
						this.setShieldBack();
				}
			}
		}/*else
			System.out.println(this.rotationYaw+" ");*/
    }
	
	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if(this.worldObj.isRemote)return;
		if(this.getThrower() == null)return;

		ItemStack st = ((EntityPlayer)this.getThrower()).inventory.getStackInSlot(this.theSlot);
		
		if(mop.typeOfHit==MovingObjectType.ENTITY && mop.entityHit!=null){
			if(mop.entityHit==this.getThrower()){
				if(this.isEntityAlive())
					this.setShieldBack();
			}else if(mop.entityHit instanceof EntityLivingBase){
				
				if(st != null && st.getItem() != null && st.getItem() instanceof ItemVibraniumShield)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemVibraniumShield)st.getItem()).SHIELD_1.getDamageVsEntity());
				//DamageSourceaHelper.causeShieldDamages(this, this.getThrower())
				if(mop.entityHit instanceof EntityLivingBase)
					st.hitEntity((EntityLivingBase)mop.entityHit, (EntityPlayer) this.getThrower());
				
			}else if(mop.entityHit != null){				
			
				if(st != null && st.getItem() != null && st.getItem() instanceof ItemVibraniumShield)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemVibraniumShield)st.getItem()).SHIELD_1.getDamageVsEntity());
				//DamageSourceaHelper.causeShieldDamages(this, this.getThrower())
			}
			return;
		}else if(mop.typeOfHit==MovingObjectType.BLOCK){
			
			//this.setBounce(mop.sideHit, mop);
			this.isCommingBack = true;
			this.blockMop = true;
		}
	}
	
	public void setBounce(int side, MovingObjectPosition mop){
		this.angleYaw = this.rotationYaw;
		this.anglePitch= this.rotationPitch;
		if(side==0 || side==1)
			this.anglePitch = this.rotationPitch*-1;

		switch(side){
			 case 2:this.angleYaw=this.rotationYaw<0?(-180-this.rotationYaw):180-this.rotationYaw;
				 break;
			 case 3:this.angleYaw=this.rotationYaw<0?(-180-this.rotationYaw):180-this.rotationYaw;
			 	break;
			 case 4:this.angleYaw=this.rotationYaw*-1;
			 	break;
			 case 5:this.angleYaw=this.rotationYaw*-1;
			 	break;
			 default:
				 break;
		}
		this.prevRotationPitch = this.rotationPitch;
		this.rotationPitch = this.anglePitch;
		this.prevRotationYaw = this.rotationYaw;
		this.rotationYaw = this.angleYaw;
		
        float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}
	
	public void setShieldBack(){
		ItemStack st = ((EntityPlayer)this.getThrower()).inventory.getStackInSlot(this.theSlot);
		if(st != null && st.getItem() != null && st.getItem() instanceof ItemVibraniumShield)
			ItemNBTHelper.setBoolean(st, "IsThrown", false);
		this.isCommingBack = false;
		this.setDead();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(theSlot);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		theSlot = data.readInt();
	}
}
