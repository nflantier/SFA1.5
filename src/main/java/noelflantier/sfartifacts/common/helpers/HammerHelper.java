package noelflantier.sfartifacts.common.helpers;

import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.handlers.ModPlayerStats;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.items.baseclasses.MiningHammerBase;

public class HammerHelper {
	
	public static boolean breakOnImpact(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player, MovingObjectPosition mop, Entity entityHit){
		return ((MiningHammerBase)stack.getItem()).getEnergyStored(stack) >= ((MiningHammerBase)stack.getItem()).energyMining && (breakRadius > 0) ? breakaBlockWithMop(stack, x, y, z, breakRadius, breakDepth, player, mop, entityHit, -1) : false;
	}
	
	public static boolean breakOnMinning(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player){
		return ((MiningHammerBase)stack.getItem()).getEnergyStored(stack) >= ((MiningHammerBase)stack.getItem()).energyMining ? breakaBlockWithMop(stack, x, y, z, breakRadius, breakDepth, player, rayTraceMining(player.worldObj, player, 4.5d), null, 1) : false;
	}
	/*
	public static boolean breakaBlockWithoutMop(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player){
		//return breakaBlockWithMop(stack,x,y,z,breakRadius,breakDepth,player,rayTraceMining(player.worldObj, player, 4.5d));
		Block block = player.worldObj.getBlock(x,y,z);
		int meta = player.worldObj.getBlockMetadata(x,y,z);
		boolean effective = false;
		if(block != null)
		{
			for (String s : stack.getItem().getToolClasses(stack))
			{
				if (block.isToolEffective(s, meta) || stack.getItem().func_150893_a(stack, block) > 1F) effective = true;
			}
		}
		if (!effective)	return true;

		float refStrength = ForgeHooks.blockStrength(block, player, player.worldObj, x, y, z);


		MovingObjectPosition mop = rayTraceMining(player.worldObj, player, 4.5d);
		if(mop == null)
		{
			updateGhostBlocks(player, player.worldObj);
			return true;
		}
		int sideHit = mop.sideHit;
		
		int xRange = breakRadius;
		int yRange = breakRadius;
		int zRange = breakDepth;
		int yOffset = 0;
		switch (sideHit) {
			case 0:
			case 1:
				yRange = breakDepth;
				zRange = breakRadius;
				break;
			case 2:
			case 3:
				xRange = breakRadius;
				zRange = breakDepth;
				yOffset = breakRadius - 1;
				break;
			case 4:
			case 5:
				xRange = breakDepth;
				zRange = breakRadius;
				yOffset = breakRadius - 1;
				break;
		}
		if(breakRadius == 0)yOffset = 0;
		for (int xPos = x - xRange; xPos <= x + xRange; xPos++)
		{
			for (int yPos = y + yOffset - yRange; yPos <= y + yOffset + yRange; yPos++)
			{
				for (int zPos = z - zRange; zPos <= z + zRange; zPos++)
				{
					breakthablock(stack, player.worldObj, xPos, yPos, zPos, sideHit, player, refStrength, Math.abs(x - xPos) <= 1 && Math.abs(y - yPos) <= 1 && Math.abs(z - zPos) <= 1);
				}
			}
		}
		
		if(ItemNBTHelper.getBoolean(stack, "CanMagnet", false) && ItemNBTHelper.getBoolean(stack, "IsMagnetOn", false))
		{
			List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - xRange, y + yOffset - yRange, z - zRange, x + xRange + 1, y + yOffset + yRange + 1, z + zRange + 1));
			for (EntityItem item : items){
				if (!player.worldObj.isRemote)
				{
					item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
					((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(item));
					item.delayBeforeCanPickup = 0;
				}
			}
		}
		return true;
	}
	*/
	public static boolean breakaBlockWithMop(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player, MovingObjectPosition mop, Entity entityHit, int rangeMining){
		
		Block block = player.worldObj.getBlock(x,y,z);
		
		if(block==Blocks.bedrock)return false;
		
		int meta = player.worldObj.getBlockMetadata(x,y,z);
		
		boolean effective = false;
		if(block != null)
		{
			for (String s : stack.getItem().getToolClasses(stack))
			{
				if (block.isToolEffective(s, meta) || stack.getItem().func_150893_a(stack, block) > 1F) effective = true;
			}
		}
		if (!effective)	return false;
		

		float refStrength = ForgeHooks.blockStrength(block, player, player.worldObj, x, y, z);
		if(mop == null)
		{
			updateGhostBlocks(player, player.worldObj);
			return true;
		}

		int sideHit = mop.sideHit;
		int xRange = breakRadius;
		int yRange = breakRadius;
		int zRange = breakDepth;
		int yOffset = 0;
		
		if(breakRadius == 0){
			breakthablock(stack, player.worldObj, x, y, z, sideHit, player, refStrength, true);
		}else{
			switch (sideHit) {
				case 0:
				case 1:
					yRange = breakDepth;
					zRange = breakRadius;
					break;
				case 2:
				case 3:
					xRange = breakRadius;
					zRange = breakDepth;
					yOffset = breakRadius - 1;
					break;
				case 4:
				case 5:
					xRange = breakDepth;
					zRange = breakRadius;
					yOffset = breakRadius - 1;
					break;
			}
			
			boolean soundOnRange = false;
			
			for (int xPos = x - xRange; xPos <= x + xRange; xPos++)
			{
				for (int yPos = y + yOffset - yRange; yPos <= y + yOffset + yRange; yPos++)
				{
					for (int zPos = z - zRange; zPos <= z + zRange; zPos++)
					{
						if(rangeMining<=0)soundOnRange = true;
						else soundOnRange = Math.abs(x - xPos) <= rangeMining && Math.abs(y - yPos) <= rangeMining && Math.abs(z - zPos) <= rangeMining;
						
						Block tblock = player.worldObj.getBlock(xPos, yPos, zPos);
						if(entityHit!=null && tblock.canEntityDestroy(player.worldObj, xPos, yPos, zPos, entityHit))
							breakthablock(stack, player.worldObj, xPos, yPos, zPos, sideHit, player, refStrength, soundOnRange);
						else if (entityHit == null)
							breakthablock(stack, player.worldObj, xPos, yPos, zPos, sideHit, player, refStrength, soundOnRange);
					}
				}
			}
		}
		if(ItemNBTHelper.getBoolean(stack, "CanMagnet", false) && ItemNBTHelper.getBoolean(stack, "IsMagnetOn", false))
		{
			List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - xRange, y + yOffset - yRange, z - zRange, x + xRange + 1, y + yOffset + yRange + 1, z + zRange + 1));
			for (EntityItem item : items){
				if (!player.worldObj.isRemote)
				{
					item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
					((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(item));
					item.delayBeforeCanPickup = 0;
				}
			}
		}
		return true;
	}
	
	public static void breakthablock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isAirBlock(x, y, z))
			return;

		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(block==Blocks.bedrock || !block.canEntityDestroy(world, x, y, z, entity))
			return ;
		
		if (!world.isRemote)
		{
			world.removeTileEntity(x, y, z);
			world.setBlockToAir(x, y, z);
			world.markBlockForUpdate(x, y, z);
		};
		
		if (!world.isRemote) {

		}
		else
		{
			world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x,y,z, Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
	}
	
	protected static void breakthablock(ItemStack stack, World world, int x, int y, int z, int sidehit, EntityPlayer player, float refStrength, boolean breakSound)
	{
		if (world.isAirBlock(x, y, z))
			return;

		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		boolean effective = false;
		if(block != null)
		{
			for (String s : stack.getItem().getToolClasses(stack))
			{
				if (block.isToolEffective(s, meta) || stack.getItem().func_150893_a(stack, block) > 1F) effective = true;
			}
		}
		if (!effective)	return;

		float strength = ForgeHooks.blockStrength(block, player, world, x,y,z);
				
		if (!player.canHarvestBlock(block) || !ForgeHooks.canHarvestBlock(block, player, meta)|| refStrength/strength > 10f )
			return;

		if (!world.isRemote)
		{
			BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) player, x, y, z);
			if (event.isCanceled()) {
				((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
				return;
			}
		}
				
		//((MiningHammerBase)stack.getItem()).extractEnergy(stack, ((MiningHammerBase)stack.getItem()).energyPerO, false);
		extractEnergyInHammer(stack,((ItemThorHammer)stack.getItem()).energyMining );
		
		if (!world.isRemote) {

			block.onBlockHarvested(world, x, y, z, meta, player);

			if(block.removedByPlayer(world, player, x,y,z, true)) // boolean is if block can be harvested, checked above
			{
				block.onBlockDestroyedByPlayer( world, x,y,z, meta);
				block.harvestBlock(world, player, x,y,z, meta);
				player.addExhaustion(-0.025F);
			}

			EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
			mpPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
		}
		else
		{
			if (breakSound) world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
			if(block.removedByPlayer(world, player, x,y,z, true))
			{
				block.onBlockDestroyedByPlayer(world, x,y,z, meta);
			}

			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x,y,z, Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
	}
	
	public static void extractEnergyInHammer(ItemStack stack, int energy){
		((MiningHammerBase)stack.getItem()).extractEnergy(stack, energy, false);
	}
	
	public static MovingObjectPosition rayTraceMining(World world, Entity player, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
		if (!world.isRemote && player instanceof EntityPlayer) d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP) {
			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		}
		Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
		return world.rayTraceBlocks(vec3, vec31);
	}
	
	public static void updateGhostBlocks(EntityPlayer player, World world) {
		if (world.isRemote) return;
		int xPos = (int) player.posX;
		int yPos = (int) player.posY;
		int zPos = (int) player.posZ;

		for (int x = xPos - 6; x < xPos + 6; x++) {
			for (int y = yPos - 6; y < yPos + 6; y++) {
				for (int z = zPos - 6; z < zPos + 6; z++) {
					((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
					//world.markBlockForUpdate(x, y, z);
				}
			}
		}
	}
	
	public static boolean startInvoking(World w, EntityPlayer player, int x, int y, int z){

		TileEntity t = w.getTileEntity(x, y, z);
		if(t!=null && t instanceof TileHammerStand){
			((TileHammerStand)t).isInvoking = true;
			w.spawnEntityInWorld(new EntityHammerInvoking(w, player, x, y, z));
		}else return false;
		
		return true;
	}
	
	public static boolean startLightning(World world, ItemStack stack, double x, double y, double z, EntityPlayer player){
		MovingObjectPosition mo = rayTraceLightning(player,(double)ModConfig.distanceLightning);
        if (mo != null){
            switch (mo.typeOfHit){
                case BLOCK:
                	world.addWeatherEffect(new EntityLightningBolt(world, mo.blockX, mo.blockY+1, mo.blockZ));
            		extractEnergyInHammer(stack,((ItemThorHammer)stack.getItem()).energyLightning);
                    break;
                case ENTITY:
                    world.addWeatherEffect(new EntityLightningBolt(world, mo.entityHit.posX, mo.entityHit.posY, mo.entityHit.posZ));
            		extractEnergyInHammer(stack,((ItemThorHammer)stack.getItem()).energyLightning);
                    break;
                default:
                	break;
            }
        }
		return false;
	}
	
	public static MovingObjectPosition rayTraceLightning(EntityPlayer player, double distance){
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + (double) (player.worldObj.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (player instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        d3 = distance;
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return player.worldObj.rayTraceBlocks(vec3, vec31);
	}

	public static boolean startMoving(World w, EntityPlayer player, ItemStack stack){
		//ModEvents.stats = ModPlayerStats.get(player);
		
		ModPlayerStats stats = ModPlayerStats.get(player);
		if((stats.tickMovingWithHammer>0 || stack.getItem() instanceof MiningHammerBase==false))
			return false;
		if(!w.isRemote){
			stats.tickMovingWithHammer = 2;
			stats.isMovingWithHammer = true;
			if(!stats.justStartMoving)
				stats.justStartMoving = true;
			extractEnergyInHammer(stack,((ItemThorHammer)stack.getItem()).energyMoving);
			return false;
		}
		
        float f = 0.4F;
        player.motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f)*8;
        player.motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f)*8;
        player.motionY = (double)(-MathHelper.sin((player.rotationPitch + 0.0F) / 180.0F * (float)Math.PI) * f)*8;
		return true;
	}

	public static void startMiningSequence(World w, EntityPlayer player) {
		w.spawnEntityInWorld(new EntityHammerMinning(w, player, player.inventory.currentItem));
	}

	public static void startGuiTeleport(World w, EntityPlayer player) {
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDTeleport, w, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
	
	public static void startTeleporting(Entity entity, String []st) {
		int dimid = Integer.parseInt(st[0]);
		double tx = (double)Integer.parseInt(st[1]);
		double ty = (double)Integer.parseInt(st[2]);
		double tz = (double)Integer.parseInt(st[3]);

		if (entity == null || entity.worldObj.isRemote) return;
		World startWorld = entity.worldObj;
		World destinationWorld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimid);

		if (destinationWorld == null){
			return ;
		}

		Entity mount = entity.ridingEntity;
		if (entity.ridingEntity != null)
		{
			entity.mountEntity(null);
			startTeleporting(mount, st);
		}

		boolean interDimensional = startWorld.provider.dimensionId != destinationWorld.provider.dimensionId;

		startWorld.updateEntityWithOptionalForce(entity, false);//added
		if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).closeScreen();
		
		if ((entity instanceof EntityPlayerMP) && interDimensional)
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			//player.closeScreen();//added
			player.dimension = dimid;
			player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, destinationWorld.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
			((WorldServer)startWorld).getPlayerManager().removePlayer(player);

			startWorld.playerEntities.remove(player);
			startWorld.updateAllPlayersSleepingFlag();
			int i = entity.chunkCoordX;
			int j = entity.chunkCoordZ;
			if ((entity.addedToChunk) && (startWorld.getChunkProvider().chunkExists(i, j)))
			{
				startWorld.getChunkFromChunkCoords(i, j).removeEntity(entity);
				startWorld.getChunkFromChunkCoords(i, j).isModified = true;
			}
			startWorld.loadedEntityList.remove(entity);
			startWorld.onEntityRemoved(entity);
		}

		entity.setLocationAndAngles(tx, ty, tz, 0, 0);

		((WorldServer)destinationWorld).theChunkProviderServer.loadChunk((int)tx >> 4, (int)tz >> 4);

		destinationWorld.theProfiler.startSection("placing");
		if (interDimensional)
		{
			if (!(entity instanceof EntityPlayer))
			{
				NBTTagCompound entityNBT = new NBTTagCompound();
				entity.isDead = false;
				entityNBT.setString("id", EntityList.getEntityString(entity));
				entity.writeToNBT(entityNBT);
				entity.isDead = true;
				entity = EntityList.createEntityFromNBT(entityNBT, destinationWorld);
				if (entity == null)
				{
					return;
				}
				entity.dimension = destinationWorld.provider.dimensionId;
			}
			destinationWorld.spawnEntityInWorld(entity);
			entity.setWorld(destinationWorld);
		}
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		destinationWorld.updateEntityWithOptionalForce(entity, false);
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		if ((entity instanceof EntityPlayerMP))
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			if (interDimensional) {
				player.mcServer.getConfigurationManager().func_72375_a(player, (WorldServer) destinationWorld);
			}
			player.playerNetServerHandler.setPlayerLocation(tx, ty, tz, 0,0);
		}

		destinationWorld.updateEntityWithOptionalForce(entity, false);

		if (((entity instanceof EntityPlayerMP)) && interDimensional)
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.theItemInWorldManager.setWorld((WorldServer) destinationWorld);
			player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer) destinationWorld);
			player.mcServer.getConfigurationManager().syncPlayerInventory(player);

			for (PotionEffect potionEffect : (Iterable<PotionEffect>) player.getActivePotionEffects())
			{
				player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potionEffect));
			}
			
			player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startWorld.provider.dimensionId, destinationWorld.provider.dimensionId);
		}
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		if (mount != null)
		{
			entity.mountEntity(mount);
			if ((entity instanceof EntityPlayerMP)) {
				destinationWorld.updateEntityWithOptionalForce(entity, true);
			}
		}
		destinationWorld.theProfiler.endSection();
		entity.fallDistance = 0;
	}
}
