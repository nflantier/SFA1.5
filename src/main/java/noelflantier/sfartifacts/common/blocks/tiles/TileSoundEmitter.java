package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.entities.ai.EntityAITargetBlock;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitter;
import noelflantier.sfartifacts.common.recipes.MobsPropertiesForSpawning;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;

public class TileSoundEmitter extends TileMachine implements ITileGlobalNBT{
	
	//PROCESSING
	public boolean isEmitting = false;
	public int frequencySelected = 0;
	public int frequencyEmited = 0;
    public String entityNameToSpawn;
	public Map<Integer, String> listScannedFrequency = new HashMap<Integer, String>();

	//SPAWNER
	public LivingEntitySpawnerBaseLogic spawnerBaseLogic = new LivingEntitySpawnerBaseLogic(){
		public void blockEvent(int par1)
		{
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Blocks.mob_spawner, par1, 0);
		}
		public World getSpawnerWorld()
		{
			return worldObj;
		}
		public int getSpawnerX()
		{
			return xCoord;
		}
		public int getSpawnerY()
		{
			return yCoord;
		}
		public int getSpawnerZ()
		{
			return zCoord;
		}	

		@Override
		public String getEntityNameToSpawn() {
			return entityNameToSpawn;
		}
		@Override
		public boolean spawnConditions() {
			if(getEnergyStored(ForgeDirection.UNKNOWN)<=0 && getFluidTanks().get(0).getFluidAmount()<=0)
				return false;
			MobsPropertiesForSpawning mpfs = SoundEmitterConfig.getInstance().getMobsProperties(entityNameToSpawn, getEnergyStored(ForgeDirection.UNKNOWN), getFluidTanks().get(0).getFluidAmount());
			if(mpfs!=null){
				int nbmax = mpfs.nbMaxSpawning==-1?this.maxSpawnCount:mpfs.nbMaxSpawning;
				int spx = 0;
				for(int i = nbmax;i>=this.minSpawnCount;i--){
					if(getFluidTanks().get(0).getFluidAmount()>=mpfs.fluidneeded.amount * nbmax
							&& getEnergyStored(ForgeDirection.UNKNOWN)>=mpfs.energyneeded * nbmax){
						spx = i;
						break;
					}
				}
				this.spawnCount = spx;
				this.attractedToSpawner = mpfs.isAttractedToSpawner;
				this.spawnEntityOnce = mpfs.isSpawningOnce;
				this.customX = mpfs.customX;
				this.customY = mpfs.customY;
				this.customZ = mpfs.customZ;
				if(spx<=0){
					return false;
				}else{
					FluidStack st = mpfs.fluidneeded.copy();
					st.amount = st.amount*spx;
					extractEnergy(ForgeDirection.UNKNOWN, mpfs.energyneeded*spx, false);
					drain(ForgeDirection.UNKNOWN, st, true);
					this.minSpawnRange = 5;
					this.spawnRange = 10;
					return true;
				}
			}
			/*if(mpForSpawning.get(r)!=null){
				int nbmax = mpForSpawning.get(r).nbMaxSpawing==-1?this.maxSpawnCount:mpForSpawning.get(r).nbMaxSpawing;
				int spx = 0;
				for(int i = nbmax;i>=this.minSpawnCount;i--){
					if(getFluidTanks().get(0).getFluidAmount()>=mpForSpawning.get(r).fluidneeded.amount * nbmax
							&& getEnergyStored(ForgeDirection.UNKNOWN)>=mpForSpawning.get(r).rfneeded * nbmax){
						spx = i;
						break;
					}
				}
				
				this.attractedToSpawner = mpForSpawning.get(r).isAttractedToSpawner;
				this.spawnEntityOnce = mpForSpawning.get(r).isSpawningOnce;
				this.customX = mpForSpawning.get(r).customX;
				this.customY = mpForSpawning.get(r).customY;
				this.customZ = mpForSpawning.get(r).customZ;
				
				this.spawnCount = spx;
				if(spx<=0){
					return false;
				}else{
					FluidStack st = mpForSpawning.get(r).fluidneeded.copy();
					st.amount = st.amount*spx;
					extractEnergy(ForgeDirection.UNKNOWN, mpForSpawning.get(r).rfneeded*spx, false);
					drain(ForgeDirection.UNKNOWN, st, true);
					this.minSpawnRange = 5;
					this.spawnRange = 10;
					return true;
				}
			}*/
			return false;
		}
		@Override
		public void entityJustCreated(Entity entity) {
            entity.getEntityData().setIntArray(SoundEmitterConfig.KEY_SPAWN, new int[]{getSpawnerX(),getSpawnerY(),getSpawnerZ()});
		}
		@Override
		public void entityJustSpawned(Entity entity) {
			if(entity instanceof EntityLiving && entity instanceof EntityCreature && this.attractedToSpawner){
				((EntityLiving)entity).targetTasks.addTask(0, new EntityAITargetBlock((EntityCreature) entity, 0,true,false,getSpawnerX(),getSpawnerY(),getSpawnerZ()));
			}
		}
		@Override
		public void finishSpawning() {
			if(this.spawnEntityOnce){
				this.spawnEntityOnce = false;
				isEmitting = false;
			}
		}
	};
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[1];
	
	public TileSoundEmitter(){
		super("Sound Emitter");
		this.hasFL = true;
		this.hasRF = true;
    	this.energyCapacity = ModConfig.capacitySoundEmiter;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity/100);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.tankCapacity = ModConfig.capacityAsgarditeSoundEmitter;
		this.tank.setCapacity(this.tankCapacity);
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}
	
	@Override
	public void init(){
		super.init();
		for(ForgeDirection f:ForgeDirection.values()){
			this.recieveSides.add(f);
			this.extractSides.add(f);
		}
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar2 = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(4);add(5);add(ForgeDirection.UNKNOWN.ordinal());}};
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, ar2);
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
	
	@Override
	public void processPackets() {
	    PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
        PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
		PacketHandler.sendToAllAround(new PacketSoundEmitter(this),this);
	}

	@Override
	public void processMachine(){
    	this.processInventory();
		if(this.isEmitting){
			if(this.entityNameToSpawn==null){
				this.entityNameToSpawn = SoundEmitterConfig.getInstance().getNameForFrequency(this.frequencyEmited);
			}else{
				spawnerBaseLogic.updateSpawner();
			}
			/*if(entitiesNameForSpawning==null){
				entityNameForSpawning = "";
				entitiesNameForSpawning = new ArrayList<String>();
				mpForSpawning = new ArrayList<MobsPropertiesForSpawing>();
				ArrayList<Integer> t = SoundEmitterHelper.getIdsForFrequency(this.frequencyEmited);
				for(int i = 0;i<t.size();i++){
					entitiesNameForSpawning.add(SoundEmitterHelper.spawningRulesIDForRules.get(t.get(i)).nameEntity);
					mpForSpawning.add(SoundEmitterHelper.spawningRulesIDForRules.get(t.get(i)));
				}
			}else{
				spawnerBaseLogic.updateSpawner();
			}*/
		}
	}

	public boolean processInventory(){
		if(this.items[0]!=null){
			if(this.tank.getFluidAmount()<this.tank.getCapacity()){
				FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.items[0]);
				if(fluid!=null && fluid.amount>0){
					ItemStack emptyItem = this.items[0].copy();
					int avail = this.tank.fill(fluid, true);
					fluid.amount = fluid.amount-avail;
				    if(this.items[0].getItem().hasContainerItem(this.items[0])) {
				    	emptyItem = this.items[0].getItem().getContainerItem(this.items[0]);
				    }
					FluidContainerRegistry.fillFluidContainer(fluid, emptyItem);
					this.items[0] = emptyItem;
				}
			}
		}
		return true;
	}
	
	@Override
	public ItemStack[] getItems() {
		return this.items;
	}	    

	@Override
	public String getInventoryName() {
		return "Sound Emitter";
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return  slot==0 && fluid != null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite ;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack))
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack))
			return true;
		return false;
	}
	
	public void writeToItem(ItemStack stack){
	    if(stack == null) {
	        return;
	    }
	    if(stack.stackTagCompound == null) {
	        stack.stackTagCompound = new NBTTagCompound();
	    }
	    NBTTagCompound root = stack.stackTagCompound;
	    writeToNBT(root);
	}

	public void readFromItem(ItemStack stack){
	    if(stack == null || stack.stackTagCompound == null) {
	        return;
	    }
	    readFromNBT(stack.stackTagCompound);
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

		spawnerBaseLogic.writeToNBT(nbt);
		nbt.setBoolean("isEmitting", this.isEmitting);
		nbt.setInteger("frequencySelected", this.frequencySelected);
		nbt.setInteger("frequencyEmited", this.frequencyEmited);
		nbt.setInteger("sizefrequencyscanned", this.listScannedFrequency.size());
		if(this.listScannedFrequency.size()>0){
			int k = 0;
			for (Map.Entry<Integer, String> entry : this.listScannedFrequency.entrySet()){
				nbt.setInteger("frequency"+k, entry.getKey());
				nbt.setString("frequencystring"+k, entry.getValue());
				k++;
			}
		}
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

		spawnerBaseLogic.readFromNBT(nbt);
		this.isEmitting = nbt.getBoolean("isEmitting");
		this.frequencySelected = nbt.getInteger("frequencySelected");
		this.frequencyEmited = nbt.getInteger("frequencyEmited");
		
		int size = nbt.getInteger("sizefrequencyscanned");
		if(size>0){
			for(int i = 0 ; i<size ; i++){
				this.listScannedFrequency.put(nbt.getInteger("frequency"+i), nbt.getString("frequencystring"+i));
			}
		}
    }

	@Override
	public void addToWaila(List<String> list) {
		super.addToWaila(list);
		String str = "";
		if(this.getEnergyStored(ForgeDirection.UNKNOWN)<SoundEmitterConfig.getInstance().getRfForFrequency(frequencyEmited))
			str += "Not enough RF";
		if(this.getFluidTanks().get(0).getFluidAmount()<SoundEmitterConfig.getInstance().getFlForFrequency(frequencyEmited))
			str += str == ""?"Not enough Fluid":" and Fluid";
		list.add("Emiting : "+(this.isEmitting?str==""?this.frequencyEmited+" "+SoundEmitterConfig.getInstance().getRealEntityName(frequencyEmited):""+str:"No"));
	}
}
