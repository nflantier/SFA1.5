package noelflantier.sfartifacts.common.entities;

import java.util.Map;

import org.apache.commons.logging.Log;

import com.google.common.collect.MapMaker;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;

public class EntityHoverBoard  extends Entity implements IEntityAdditionalSpawnData {

	private EntityPlayer player;
	private int slot;

	private static Map<EntityPlayer, EntityHoverBoard> hoverBoardMap = new MapMaker().weakKeys().weakValues().makeMap();
	private long tickSwing = 0;
	
	public static boolean isHoverBoardDeployed(Entity player) {
		EntityHoverBoard hoverb = hoverBoardMap.get(player);
		return hoverb == null || hoverb.isDeployed();
	}
	private static boolean isHoverBoardValid(EntityPlayer player, EntityHoverBoard hoverboard) {
		if (player == null || player.isDead || hoverboard == null || hoverboard.isDead) return false;
		if (player.worldObj.provider.dimensionId != hoverboard.worldObj.provider.dimensionId) return false;
		if (player.inventory.getStackInSlot(hoverboard.getSlot())==null)return false;
		if (player.inventory.getStackInSlot(hoverboard.getSlot()).getItem() instanceof ItemHoverBoard == false)return false;
		if (((ItemHoverBoard)player.inventory.getStackInSlot(hoverboard.getSlot()).getItem()).getEnergyStored(player.inventory.getStackInSlot(hoverboard.getSlot()))<ModConfig.rfPerSecondHoverBoard)
					return false;
		return true;
	}
	
	public EntityHoverBoard(World world) {
		super(world);
	}
	
	public EntityHoverBoard(World world, EntityPlayer player) {
		this(world);
		this.player = player;
	}
	
	public EntityHoverBoard(World world, EntityPlayer player, int slot) {
		this(world, player);
		this.slot = slot;
	}
	
	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(17, (byte)1);
	}

	public boolean isDeployed() {
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	@Override
	public void onUpdate() {
		//setDead();
		if (!isHoverBoardValid(player, this)) {
			setDead();
		}

		if (isDead) {
			hoverBoardMap.remove(player);
			return;
		}

		boolean isDeployed = false;

		if (!worldObj.isRemote) {
			this.dataWatcher.updateObject(17, (byte)(isDeployed? 1 : 0));
			fixPositions(player, false);
		}

		if (!player.isInWater()) {
			final double horizontalSpeed;
			if (player.isSneaking()) {
				horizontalSpeed = 0;
			} else {
				horizontalSpeed = 0.3;
			}
			
			double x = Math.cos(Math.toRadians(player.rotationYawHead + 90)) * horizontalSpeed;
			double z = Math.sin(Math.toRadians(player.rotationYawHead + 90)) * horizontalSpeed;

			if(player.motionX!=0){
				if(player.motionX>-1 && player.motionX<1)
					player.motionX += x;
			}
			if(player.motionZ!=0){
				if(player.motionZ>-1 && player.motionZ<1)
					player.motionZ += z;
			}
			
			if(horizontalSpeed>0 && (player.motionX!=0 && player.motionZ!=0)){
				if(SFArtifacts.instance.myProxy.sfaEvents.getServerTick()%20==0 ){
					tickSwing = SFArtifacts.instance.myProxy.sfaEvents.getServerTick()+8;
					player.limbSwing = 0.5f;
					player.prevLimbSwingAmount = 0.5f;
					player.limbSwingAmount = 0.5f;
				}
			}
			if(tickSwing==0 || (tickSwing>0 && tickSwing==SFArtifacts.instance.myProxy.sfaEvents.getServerTick()) ){
				player.limbSwing = 0f;
				player.prevLimbSwingAmount = 0f;
				player.limbSwingAmount = 0f;
				tickSwing=0;
			}

			if(SFArtifacts.instance.myProxy.sfaEvents.getServerTick()%20==0 && horizontalSpeed>0 && (player.motionX!=0 && player.motionZ!=0)){
				((ItemHoverBoard)player.inventory.getStackInSlot(slot).getItem()).extractEnergy(player.inventory.getStackInSlot(slot), ModConfig.rfPerSecondHoverBoard, false);
			}
			
			if(player.motionY>5)
				player.motionY = 5;
			if(player.fallDistance>5)
				player.fallDistance = 5;
			
			player.stepHeight = 2;
		}
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	public int getSlot() {
		return slot;
	}
	
	@Override
	public void setDead() {
		super.setDead();
		player.stepHeight = 0.5F;
		hoverBoardMap.remove(player);
	}

	private void fixPositions(EntityPlayer thePlayer, boolean localPlayer) {
		this.lastTickPosX = prevPosX = player.prevPosX;
		this.lastTickPosY = prevPosY = player.prevPosY;
		this.lastTickPosZ = prevPosZ = player.prevPosZ;

		this.posX = player.posX;
		this.posY = player.posY;
		this.posZ = player.posZ;

		setPosition(posX, posY, posZ);
		this.prevRotationYaw = player.prevRenderYawOffset;
		this.rotationYaw = player.renderYawOffset;

		this.prevRotationPitch = player.prevRotationPitch;
		this.rotationPitch = player.rotationPitch;

		if (!localPlayer) {
			this.posY += 1.2;
			this.prevPosY += 1.2;
			this.lastTickPosY += 1.2;
		}

		this.motionX = this.posX - this.prevPosX;
		this.motionY = this.posY - this.prevPosY;
		this.motionZ = this.posZ - this.prevPosZ;
	}
	
	@Override
	public void writeSpawnData(ByteBuf data) {
		if (player == null) {
			data.writeInt(-42);
		} else {
			data.writeInt(player.getEntityId());
		}
		data.writeInt(slot);
	}

	@Override
	public void readSpawnData(ByteBuf data) {		
		int playerId = data.readInt();
		Entity e = worldObj.getEntityByID(playerId);
	
		if (e instanceof EntityPlayer) {
			player = (EntityPlayer)e;
			hoverBoardMap.put(player, this);
			slot = data.readInt();
		} else {
			setDead();
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}

}
