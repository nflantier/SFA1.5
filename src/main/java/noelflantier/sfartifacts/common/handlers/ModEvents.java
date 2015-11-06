package noelflantier.sfartifacts.common.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import noelflantier.sfartifacts.common.entities.EntityItemStronk;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;
import noelflantier.sfartifacts.common.items.baseclasses.MiningHammerBase;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketExtendedEntityProperties;

public class ModEvents {
	protected long serverTickCount = 0;
	protected long clientTickCount = 0;
	public List<EntityChicken> listChick = new ArrayList<EntityChicken>();
	public List<EntityChicken> listChickToRemove = new ArrayList<EntityChicken>();
    
	public long getServerTick(){
		return this.serverTickCount;
	}

	public long getClientTick(){
		return this.clientTickCount;
	}	

	@SubscribeEvent
	public void LivingAttackEvent(LivingAttackEvent event) {
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemVibraniumShield && ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "CanBlock", false) && !ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "IsThrown", false)){
				if(event.source!=null && event.source.getSourceOfDamage()!=null){
					double cop = (double)player.posZ - (double)event.source.getSourceOfDamage().posZ;
					double coa = (double)player.posX - (double)event.source.getSourceOfDamage().posX;
					double a = 360-(Math.toDegrees(Math.atan2(coa,cop))+180);
					float pr = player.rotationYaw<0?360+player.rotationYaw:player.rotationYaw;
					float ang = (float)ModConfig.shieldProtection/2;
					if( ( event.source==event.source.outOfWorld || event.source==event.source.lava || event.source==event.source.starve || event.source==event.source.drown
							|| event.source==event.source.inFire || event.source==event.source.onFire || event.source==event.source.fall ) && !event.source.isExplosion()){
						
					}else if(a<pr+ang && a>pr-ang)
						event.setCanceled(true);
				}
			}
		}else if(event.entity instanceof EntityItemStronk){
			if(event.source!=null && event.source.getSourceOfDamage()!=null && event.source==event.source.onFire || event.source==event.source.inFire)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void ExplosionEventDetonate(ExplosionEvent.Detonate event) {
		List<Entity> le = event.getAffectedEntities();
		List<Entity> toRemove = new ArrayList<Entity>();
		for(Entity e : le){
			if(e instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer)e;
				if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemVibraniumShield  && ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "CanBlock", false) && !ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "IsThrown", false)){
					double cop = (double)player.posZ - (double)event.explosion.explosionZ;
					double coa = (double)player.posX - (double)event.explosion.explosionX;
					double a = 360-(Math.toDegrees(Math.atan2(coa,cop))+180);
					float pr = player.rotationYaw<0?360+player.rotationYaw:player.rotationYaw;
					float ang = (float)ModConfig.shieldProtection/2;
					if(a<pr+ang && a>pr-ang)
						toRemove.add(e);
				}
			}
		}
		if(toRemove.size()>0){
			event.getAffectedEntities().removeAll(toRemove);
		}
	}
	
    @SubscribeEvent
    public void onTick(ServerTickEvent evt) {
    	if(evt.phase == Phase.END) {
    		serverTickCount++;
    	}
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent evt) {
    	if(evt.phase == Phase.END) {
    		clientTickCount++;
    	}
    }
    
    @SubscribeEvent
    public void entityLighted(EntityStruckByLightningEvent evt){
    	if(evt.entity instanceof EntityChicken){
    		if(!this.listChick.contains((EntityChicken)evt.entity))
    			this.listChick.add((EntityChicken)evt.entity);
    	}
    }
    
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
    	if(event.phase == Phase.END) {
	    	if(this.listChick.size()>0){
	    		for(EntityChicken ec :this.listChick){
	    			if(ec.isDead){
						Random rand = new Random();
						if(rand.nextFloat()<(float)ModConfig.chanceToSpawnMightyFeather){
	    					float f1 = rand.nextFloat() * 0.8F+0.1F;
	    					float f2 = rand.nextFloat() * 0.8F+0.1F;
	    					float f3 = rand.nextFloat() * 0.8F+0.1F;
	    					event.world.spawnEntityInWorld(new EntityItemStronk(event.world, ec.posX+f1, ec.posY+f2, ec.posZ+f3, new ItemStack(ModItems.itemMightyFeather,1,0)));
	    				}
    					this.listChickToRemove.add(ec);
	    			}
	    		}
	    		for(EntityChicken ecr :this.listChickToRemove){
	    			this.listChick.remove(ecr);
	    		}
	    		this.listChickToRemove.clear();
	    	}
    	}
    }

	
	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player == null)
				return;
			
			if(player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemVibraniumShield 
					&& ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "CanBlock", false) 
					&& !ItemNBTHelper.getBoolean(player.getCurrentEquippedItem(), "IsThrown", false)){
				if(player.rotationPitch>80){
					event.distance = (float)event.distance/4<1?0:(float)event.distance/4;
				}
			}
		}
	}
	
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	if(event.phase == Phase.END) {
			
	    	EntityPlayer player = event.player;
			if(player == null)
				return;

			ModPlayerStats stats = ModPlayerStats.get(player);
			if(stats.tickJustEatHulkFlesh>0)
				stats.tickJustEatHulkFlesh-=1;
			if(stats !=null && stats.tickHasHulkFleshEffect>0){
				stats.tickHasHulkFleshEffect-=1;
				if(event.side==Side.CLIENT)
					player.stepHeight = 2F;
				float f = Utils.isPlayerInFluid(player,1.22F);
				player.motionX *= f;
				player.motionZ *= f;
			}else if(stats.tickHasHulkFleshEffect==0){
				stats.tickHasHulkFleshEffect=-1;
				if(event.side==Side.CLIENT)
					player.stepHeight = 0.5F;
			}

			if(event.side==Side.CLIENT)
				return;
			
			if(stats.justBlockedAttack>0){
				stats.justBlockedAttack-=1;
				if(stats.justBlockedAttack==0){
					
				}
			}
			
			if(stats.isMovingWithHammer){
				player.fallDistance = 0.0F;
				stats.justStopMoving=false;
			}else if(stats.justStopMoving){
				stats.justStopMoving = false;
				ItemStack it = player.getCurrentEquippedItem();
				if(it!=null && it.getItem()!=null && it.getItem() instanceof MiningHammerBase)
					ItemNBTHelper.setBoolean(player.getCurrentEquippedItem(), "IsMoving", false);
			}
			
			//if(event.side==Side.SERVER){
				if(stats.isMovingWithHammer){
					if(!stats.justStartMoving){
		    			stats.isMovingWithHammer = !player.onGround;
		    			if(!stats.isMovingWithHammer)
		    				stats.justStopMoving=true;
					}
					stats.justStartMoving = false;
	    			stats.tickMovingWithHammer -= 1;
	    		}else
	    			stats.tickMovingWithHammer = 0;	
			//}
    	}
    }
    
    @SubscribeEvent
    public void PlayerLoggedInEvent (PlayerLoggedInEvent event){
    	if(ModConfig.isManualSpawning){
	    	ModPlayerStats stats = ModPlayerStats.get(event.player);
	    	if(!stats.startManual){
	    		stats.startManual = true;
	    		ItemStack manual = new ItemStack(ModItems.itemManual);
		        if (!event.player.inventory.addItemStackToInventory(manual)){
					Random rand = new Random();
					float f1 = rand.nextFloat() * 0.8F+0.1F;
					float f2 = rand.nextFloat() * 0.8F+0.1F;
					float f3 = rand.nextFloat() * 0.8F+0.1F;
					EntityItem it = new EntityItem(event.player.worldObj, event.player.posX+f1, event.player.posY+f2, event.player.posZ+f3, manual);
					event.player.worldObj.spawnEntityInWorld(it);
		            ModAchievements.triggerAchievement(event.player, "sfartifacts.manual");
		        }
	        }
    	}
    }
    
    /*@SubscribeEvent   //No need because respawn call clone
    public void playerRespawn(PlayerRespawnEvent event){
    	//syncExtendedProperties(event.player, event.player.worldObj);
    }*/
    
    @SubscribeEvent    //Not really needed because it call entityjoinworld
    public void playerChangedDimension(PlayerChangedDimensionEvent event){
    	syncExtendedProperties(event.player, event.player.worldObj);
    	syncAttributes(event.player);
     }
    
    @SubscribeEvent
    public void onEntityJoining (EntityJoinWorldEvent event){
    	syncExtendedProperties(event.entity, event.world);
    	syncAttributes(event.entity);
    }
        
    public void syncExtendedProperties(Entity entity ,World world){
    	if(entity instanceof EntityPlayer && !world.isRemote && ModPlayerStats.get((EntityPlayer) entity)!=null){
    		if(entity instanceof EntityPlayerMP)
    			PacketHandler.sendToPlayerMP(new PacketExtendedEntityProperties((EntityPlayer)entity),(EntityPlayerMP)entity);
    	}
    }
    
    public void syncAttributes(Entity entity){
    	if(!entity.worldObj.isRemote && entity instanceof EntityPlayerMP && entity instanceof EntityPlayer)
    		((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S20PacketEntityProperties(entity.getEntityId(), ((EntityPlayer)entity).getAttributeMap().getAllAttributes()));
    }
    
    @SubscribeEvent
    public void onEntityConstructing (EntityEvent.EntityConstructing event){
        if (event.entity instanceof EntityPlayer && ModPlayerStats.get((EntityPlayer) event.entity) == null){
        	ModPlayerStats.register((EntityPlayer) event.entity);
        }
    }
    
    @SubscribeEvent
    public void onClonePlayer(PlayerEvent.Clone event) {
    	if(ModPlayerStats.get(event.original)!=null){
	    	NBTTagCompound compound = new NBTTagCompound();
	    	ModPlayerStats.get(event.original).saveNBTData(compound);
	    	ModPlayerStats.get(event.entityPlayer).loadNBTData(compound);
	    	syncExtendedProperties(event.entity, event.entityPlayer.worldObj);
	    	syncAttributes(event.entityPlayer);
    	}
    }
    
    @SubscribeEvent
    public void bucketFill (FillBucketEvent evt){
        if (evt.current.getItem() == Items.bucket && evt.target.typeOfHit == MovingObjectType.BLOCK){
            int hitX = evt.target.blockX;
            int hitY = evt.target.blockY;
            int hitZ = evt.target.blockZ;

            if (evt.entityPlayer != null && !evt.entityPlayer.canPlayerEdit(hitX, hitY, hitZ, evt.target.sideHit, evt.current)){
                return;
            }

            Block bID = evt.world.getBlock(hitX, hitY, hitZ);
            for (int id = 0; id < ModFluids.fluidBlocks.length; id++){
                if (bID == ModFluids.fluidBlocks[id]){
                	if (evt.entityPlayer.capabilities.isCreativeMode){
                        evt.world.setBlockToAir( hitX, hitY, hitZ);
                    }
                    else{
                        evt.world.setBlockToAir( hitX, hitY, hitZ);
                    }
                	evt.setResult(Result.ALLOW);
                	evt.result = new ItemStack(ModItems.itemFilledBucket, 1, id);
                }
            }
        }
    }
}
