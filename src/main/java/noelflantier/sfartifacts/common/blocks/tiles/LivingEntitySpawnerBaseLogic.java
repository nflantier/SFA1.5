package noelflantier.sfartifacts.common.blocks.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class LivingEntitySpawnerBaseLogic {

	public int spawnDelay = 20;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 400;
	public int spawnCount = 1;
	public int maxSpawnCount = 5;
	public int minSpawnCount = 1;
	public boolean requiresPlayer = false;
	public boolean followVanillaSpawnRules = false;
	public int spawnRange = 4;
	public int minSpawnRange = 0;
    private int activatingRangeFromPlayer = 25;
	private int maxNearbyEntities = 100;
	public boolean spawnEntityOnce = false;
	public int skeletonType = 0;
	public boolean attractedToSpawner = false;
	public int customX = 0;
	public int customY = 0;
	public int customZ = 0;
	
	public abstract String getEntityNameToSpawn();
	
	public boolean isActivated() {
		if (!requiresPlayer)
			return true;
		else
			return this.getSpawnerWorld().getClosestPlayer((double) this.getSpawnerX() + 0.5D, (double) this.getSpawnerY() + 0.5D, (double) this.getSpawnerZ() + 0.5D, (double) this.activatingRangeFromPlayer) != null;
	}
	public void updateSpawner() {
		if (isActivated()) {
			double d2;
			if (this.getSpawnerWorld().isRemote) {
				//particles
			} else {
				
				if (this.spawnDelay == -1) {
					this.resetTimer();
				}
				
				if(this.spawnDelay==0){
					if(!spawnConditions()){
						this.resetTimer();
						return;
					}
				}
				
				if (this.spawnDelay > 0) {
					--this.spawnDelay;
					return;
				}
                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                    
                    if (entity == null)
                    {
                        return;
                    }
                    entityJustCreated(entity);
                    
                    int j = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getBoundingBox((double)this.getSpawnerX(), (double)this.getSpawnerY(), (double)this.getSpawnerZ(), (double)(this.getSpawnerX() + 1), (double)(this.getSpawnerY() + 1), (double)(this.getSpawnerZ() + 1)).expand((double)(this.spawnRange * 2), 4.0D, (double)(this.spawnRange * 2))).size();

                    if (j >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    d2 = (double)this.getSpawnerX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange;
                    double d3 = (double)(this.getSpawnerY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
                    double d4 = (double)this.getSpawnerZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange;
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(d2+this.customX, d3+this.customY, d4+this.customZ, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);
                    if (( entityliving == null || (entityliving.getCanSpawnHere() || !followVanillaSpawnRules) ) && getSpawnerWorld().getBlock((int)d2, (int)d3, (int)d4) == Blocks.air )
                    {
                        this.spawnEntity(entity);
                        entityJustSpawned(entityliving!=null?entityliving:entity);
                        this.getSpawnerWorld().playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }
                this.customX = 0;
                this.customY = 0;
                this.customZ = 0;
                if (flag){
                    this.resetTimer();
                }
                finishSpawning();
			}
		}
	}


	public Entity spawnEntity(Entity par1Entity) {
		if (par1Entity instanceof EntityLivingBase && par1Entity.worldObj != null) {
			if (par1Entity instanceof EntitySkeleton) {
				((EntitySkeleton)par1Entity).setSkeletonType(skeletonType);
				if (skeletonType == 1) {
					par1Entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
					((EntitySkeleton) par1Entity).setEquipmentDropChance(0, 0f);
				}else{
					par1Entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				}
			} //else ((EntityLiving) par1Entity).onSpawnWithEgg(null);

			((EntityLiving)par1Entity).func_110163_bv();
			this.getSpawnerWorld().spawnEntityInWorld(par1Entity);
		}

		return par1Entity;
	}
	
	private void resetTimer() {
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			int i = this.maxSpawnDelay - this.minSpawnDelay;
			this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
		}
		this.blockEvent(1);
	}

	public abstract void blockEvent(int var1);
    public abstract World getSpawnerWorld();
    public abstract int getSpawnerX();
    public abstract int getSpawnerY();
    public abstract int getSpawnerZ();
    public abstract void entityJustCreated(Entity entity);
    public abstract void entityJustSpawned(Entity entity);
    public abstract void finishSpawning();

    public abstract boolean spawnConditions();
    
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.spawnDelay = par1NBTTagCompound.getShort("Delay");
		requiresPlayer = par1NBTTagCompound.getBoolean("RequiresPlayer");
		skeletonType = par1NBTTagCompound.getInteger("SkeletonType");

			this.minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
			this.maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
			this.spawnCount = par1NBTTagCompound.getShort("SpawnCount");

		if (par1NBTTagCompound.hasKey("MaxNearbyEntities", 99)) {
			this.maxNearbyEntities = par1NBTTagCompound.getShort("MaxNearbyEntities");
			this.activatingRangeFromPlayer = par1NBTTagCompound.getShort("RequiredPlayerRange");
		}

		if (par1NBTTagCompound.hasKey("SpawnRange", 99)) {
			this.spawnRange = par1NBTTagCompound.getShort("SpawnRange");
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("Delay", (short) spawnDelay);
		par1NBTTagCompound.setShort("MinSpawnDelay", (short) minSpawnDelay);
		par1NBTTagCompound.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
		par1NBTTagCompound.setShort("SpawnCount", (short) spawnCount);
		par1NBTTagCompound.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
		par1NBTTagCompound.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
		par1NBTTagCompound.setShort("SpawnRange", (short) spawnRange);
		par1NBTTagCompound.setBoolean("RequiresPlayer", requiresPlayer);
		par1NBTTagCompound.setInteger("SkeletonType", skeletonType);
	}
}
