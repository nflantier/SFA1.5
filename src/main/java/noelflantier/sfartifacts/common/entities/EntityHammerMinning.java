package noelflantier.sfartifacts.common.entities;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;
import noelflantier.sfartifacts.common.items.baseclasses.MiningHammerBase;
import noelflantier.sfartifacts.common.items.baseclasses.ToolHammerBase;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class EntityHammerMinning extends EntityThrowable implements IEntityAdditionalSpawnData{

	private int theSlot;
	private double orX,orY,orZ,curhyp;
    public boolean isCommingBack;
    public int isCB = 0;
    public float angleToThrower;
    
	public EntityHammerMinning(World w) {
		super(w);
		this.isImmuneToFire = true;
		this.noClip = true;
	}
	public EntityHammerMinning(World w, EntityLivingBase p)
	{
		super(w,p);
		this.orX = p.posX;
		this.orY = p.posY;
		this.orZ = p.posZ;
		this.isImmuneToFire = true;
		this.noClip = true;
	}

	public EntityHammerMinning(World w, EntityLivingBase p, int slot)
	{
		super(w,p);
		this.orX = p.posX;
		this.orY = p.posY;
		this.orZ = p.posZ;
		this.isImmuneToFire = true;
		this.noClip = true;
		this.theSlot = slot;
	}
        
	@Override
    public boolean isEntityInvulnerable()
    {
        return true;
    }
    
	@Override
	protected void entityInit() {
		//getDataWatcher().addObject(11, this.isCB);
		//getDataWatcher().addObject(12, this.angleToThrower);
	}
	
	protected float getGravityVelocity()
    {
        return 0.0F;
    }
	 
	@Override
    public void onUpdate()
    {
		/*if(!this.worldObj.isRemote)getDataWatcher().updateObject(11, this.isCB);
		if(!this.worldObj.isRemote)getDataWatcher().updateObject(12, this.angleToThrower);
		this.isCB = getDataWatcher().getWatchableObjectInt(11);
		this.angleToThrower = getDataWatcher().getWatchableObjectFloat(12);*/

		super.onUpdate();
		if(!this.worldObj.isRemote){
					
			float decy = 0.8F;
			float decx = -0.5F;
			
			if(!this.isCommingBack){
				this.curhyp = Math.sqrt( (this.posX-this.orX)*(this.posX-this.orX) + (this.posY-this.orY)*(this.posY-this.orY) + (this.posZ-this.orZ)*(this.posZ-this.orZ) );
				if(this.curhyp>20){
					this.isCommingBack = true;
					if(this.getThrower()!=null){
					}
				}
			}
			
			if(this.isCommingBack){
				if(this.getThrower()!=null){	
					double xth = this.getThrower().posX;
					double yth = this.getThrower().posY+0.5;
					double zth = this.getThrower().posZ;
					this.curhyp = Math.sqrt( (this.posX-xth)*(this.posX-xth) + (this.posY-yth)*(this.posY-yth) + (this.posZ-zth)*(this.posZ-zth) );
			        this.setThrowableHeading(xth-this.posX,yth-this.posY,zth-this.posZ, 1.5F, 1.0F);

					if(this.curhyp<1.5){
						if(this.isEntityAlive())
							this.setHammerBack();
					}
				}
			}
	    }
    }
	
	public void setHammerBack(){
		ItemStack st = ((EntityPlayer)this.getThrower()).inventory.getStackInSlot(this.theSlot);
		if(st != null && st.getItem() != null && st.getItem() instanceof MiningHammerBase)
			ItemNBTHelper.setBoolean(st, "IsThrown", false);
		this.isCommingBack = false;
		this.setDead();
	}
	
	protected void onImpact(MovingObjectPosition mop) {
		if(this.worldObj.isRemote)return;
		if(this.getThrower() == null)return;
		

		ItemStack st = ((EntityPlayer)this.getThrower()).inventory.getStackInSlot(this.theSlot);
		if(mop.entityHit!=null){
			if(mop.entityHit==this.getThrower()){
				if(this.isEntityAlive())
					this.setHammerBack();
			}else if(mop.entityHit instanceof EntityLivingBase){
				if(st != null && st.getItem() != null && st.getItem() instanceof ToolHammerBase)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ToolHammerBase)st.getItem()).getDamageVsEntity());
					if(mop.entityHit instanceof EntityLivingBase)	
						st.hitEntity((EntityLivingBase)mop.entityHit, (EntityPlayer) this.getThrower());
			}else if(mop.entityHit != null){
				if(st != null && st.getItem() != null && st.getItem() instanceof ToolHammerBase)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ToolHammerBase)st.getItem()).getDamageVsEntity());
			}
			return;
		}
		
		if(st != null && st.getItem() != null && st.getItem() instanceof MiningHammerBase)
			if(!HammerHelper.breakOnImpact(st,  mop.blockX, mop.blockY, mop.blockZ, 1, 1,(EntityPlayer)this.getThrower(), mop, this))this.isCommingBack = true;
		
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
