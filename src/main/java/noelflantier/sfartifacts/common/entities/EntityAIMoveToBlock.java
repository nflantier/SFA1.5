package noelflantier.sfartifacts.common.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class EntityAIMoveToBlock  extends EntityAIBase{
	
	EntityLiving entitySpawn;
    PathEntity entityPathToBlock;
    boolean nearBLock = false;
    int xBlock;
    int yBlock;
    int zBlock;
	
	public EntityAIMoveToBlock(EntityLiving entity, int x, int y, int z){
		this.entitySpawn = entity;
		this.xBlock = x;
		this.yBlock = y;
		this.zBlock = z;
		setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
		if (entitySpawn == null){
			return false;
		}
		else if (!entitySpawn.isEntityAlive()){
			return false;
		}
		else{
			return true;
        }
	}
	
	public boolean continueExecuting(){
        return entitySpawn.isEntityAlive() && !entitySpawn.getNavigator().noPath() && entitySpawn.getDistanceSq(xBlock, yBlock, zBlock)>2.0*2.0;
    }
	
    public void startExecuting(){
    	entitySpawn.getNavigator().tryMoveToXYZ(this.xBlock, this.yBlock, this.zBlock, 2.0);
    }
    
    public void resetTask(){
    	entitySpawn = null;
    }
}
