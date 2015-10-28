package noelflantier.sfartifacts.common.entities;

import java.util.Random;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.baseclasses.ToolHammerBase;

public class EntityHammerInvoking extends EntityThrowable  implements IEntityAdditionalSpawnData {

	private int orX,orY,orZ;
	private static double decY = 100;
	private boolean canFall = false;
	private EntityPlayer invoker;
	private Random rand = new Random();
	private int rf = 0;
	private int tickEnergy = 0;
	private TileHammerStand hs;

    
	public EntityHammerInvoking(World w) {
		super(w);
	}
	
	public EntityHammerInvoking(World w, EntityPlayer player, int x, int y, int z) {
		super(w, x+0.5, y+decY+0, z+0.5);
		this.invoker = player;
		this.orX = x;
		this.orY = y;
		this.orZ = z;
	}

	@Override
	protected void entityInit() {
		
	}
    
	@Override
    public void onUpdate()
    {
		super.onUpdate();
		if(!this.canFall){
			this.motionY = 0;
			if(this.hs!=null){
				TileMasterPillar tp = this.hs.getMasterTile();
				if(tp!=null && tp.getEnergyStored(ForgeDirection.UNKNOWN)+this.rf>=ModConfig.rfNeededThorHammer){
					
					if(this.tickEnergy<1){
						this.tickEnergy = 2;
						if(this.rf>=ModConfig.rfNeededThorHammer)
							this.canFall = true;
						int maxAvailable = tp.extractEnergy(ForgeDirection.UNKNOWN, ModConfig.rfNeededThorHammer-this.rf<10000?ModConfig.rfNeededThorHammer-this.rf:10000, true);
						this.rf += tp.extractEnergyWireless(maxAvailable, false, this.hs.xCoord, this.hs.yCoord, this.hs.zCoord);
	           		}
					this.tickEnergy--;
					
				}else{
					return;
				}
			}else{
				TileEntity t = this.worldObj.getTileEntity(this.orX, this.orY, this.orZ);
				if(t!=null && t instanceof TileHammerStand){
					this.hs = (TileHammerStand)t;
				}else
					this.setDead();
			}
		}else{
			super.onUpdate();
	
			if(this.rand.nextFloat()<0.2F){
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.orX, this.orY, this.orZ));
			}
			if(this.posY<this.orY)breakHammerStand();
		}
    }

	public void breakHammerStand(){
		if(this.worldObj.isRemote){this.setDead();return;}
		
		this.worldObj.newExplosion(this, (int)this.orX, (int)this.orY-2,(int)this.orZ, 4.0F, true, true);
		
		TileEntity t = this.worldObj.getTileEntity((int)this.orX, (int)this.orY,(int)this.orZ);
		if(t!=null && t instanceof TileHammerStand){
			((TileHammerStand)t).hasInvoked = true;
			((TileHammerStand)t).isInvoking = false;
			ItemStack it = new ItemStack(ModItems.itemThorHammer, 1, 1);
			it = ItemNBTHelper.setInteger(it, "Energy", ModConfig.rfThorhammer);
			it = ItemNBTHelper.setInteger(it, "AddedCapacityLevel", 0);
			it = ItemNBTHelper.setInteger(it, "Radius", 0);
			it = ItemNBTHelper.setBoolean(it, "CanMagnet", false);
			it = ItemNBTHelper.setBoolean(it, "IsMagnetOn", false);
			it = ItemNBTHelper.setBoolean(it, "IsMoving", false);
			it = ItemNBTHelper.setBoolean(it, "IsThrown", false);
			it = ItemNBTHelper.setBoolean(it, "CanThrowLightning", false);
			it = ItemNBTHelper.setBoolean(it, "CanThrowToMove", false);
			it = ItemNBTHelper.setBoolean(it, "CanBeConfigByHand", false);
			it = ItemNBTHelper.setBoolean(it, "CanTeleport", false);
			it = ItemNBTHelper.setInteger(it, "Mode", 0);
			it = ItemNBTHelper.setTagList(it,"EnchStored", new NBTTagList());
			((TileHammerStand)t).items[0] = it;
			this.worldObj.setBlockMetadataWithNotify(t.xCoord, t.yCoord, t.zCoord, t.getBlockMetadata()+4, 1);
			this.worldObj.markBlockForUpdate(t.xCoord, t.yCoord, t.zCoord);
		}
		this.setDead();
	}
	
	@Override
	protected void onImpact(MovingObjectPosition mop) {	
		if(this.worldObj.isRemote)return;
		
		ItemStack st = new ItemStack(ModItems.itemThorHammer);
		if(mop.typeOfHit==MovingObjectType.BLOCK){
			if(this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ)!=ModBlocks.blockHammerStand)
			HammerHelper.breakthablock(this.worldObj, mop.blockX, mop.blockY, mop.blockZ);
			//HammerHelper.breakingSequenceSimple(st,  mop.blockX, mop.blockY, mop.blockZ, 1, 1,this.invoker==null?Minecraft.getMinecraft().thePlayer:this.invoker, mop);
		}else if(mop.typeOfHit==MovingObjectType.ENTITY && mop.entityHit!=null && mop.entityHit instanceof EntityLivingBase){
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, null), ((ToolHammerBase)st.getItem()).getDamageVsEntity());
		}
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setDouble("oreX", this.orX);
		compound.setDouble("oreY", this.orY);
		compound.setDouble("oreZ", this.orZ);
		compound.setInteger("rf", this.rf);
		compound.setBoolean("canFall", this.canFall);
		//compound.setString("invokerName", this.invoker.getUniqueID().toString());
		//compound.setInteger("invokerID", this.invoker.getEntityId());//this.invoker.worldObj.getPlayerEntityByName(p_72924_1_)
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.orX = compound.getInteger("oreX");
		this.orY = compound.getInteger("oreY");
		this.orZ = compound.getInteger("oreZ");
		this.rf = compound.getInteger("rf");
		this.canFall = compound.getBoolean("canFall");
		//this.invoker = this.invoker.worldObj.getPlayerEntityByName(compound.getString("invokerName"));
	}

	@Override
	public void writeSpawnData(ByteBuf buf) {
	    buf.writeInt(this.orX);
	    buf.writeInt(this.orY);
	    buf.writeInt(this.orZ);
	    buf.writeInt(this.rf);
	    buf.writeBoolean(this.canFall);
	   // buf.writeInt(this.invoker.getEntityId());
	    
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
	    this.orX = buf.readInt();
	    this.orY = buf.readInt();
	    this.orZ = buf.readInt();
	    this.rf = buf.readInt();
	    this.canFall = buf.readBoolean();
	    //int id = buf.readInt();
		//Entity e = this.invoker.worldObj.getEntityByID(id);
		//this.invoker = e instanceof EntityPlayer?(EntityPlayer)e:null;
		
	}
}
