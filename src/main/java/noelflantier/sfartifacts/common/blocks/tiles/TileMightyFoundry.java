package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundry;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;

public class TileMightyFoundry extends TileMachine implements ITileGlobalNBT, IUseSFARecipes{

	//PROCESSING
	public boolean isLocked = false;
	public boolean isRunning = false;
	public String currentRecipeName = "none";
	public double progression = 0.0D;
	public int numberofItemAllreadyCasted = 0;
	public int initialTickToMelt = 1000;
	public int tickToMelt = 1000;
	public int currentTickToMelt = 0;
	public FluidStack fluidNeededPerOneItem = new FluidStack(FluidRegistry.LAVA, 500);
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[7];
	
	public TileMightyFoundry() {
		super("Mighty Foundry");
		this.hasFL = true;
		this.hasRF = true;
    	this.energyCapacity = ModConfig.capacityMightyFoundry;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity/100);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.tankCapacity = ModConfig.capacityLavaMightyFoundry;
		this.tank.setCapacity(this.tankCapacity);
	}
	
	@Override
	public void init(){
		super.init();
		for(ForgeDirection f:ForgeDirection.values()){
			this.recieveSides.add(f);
			this.extractSides.add(f);
		}
		if(this.currentTickToMelt==0)
			this.currentTickToMelt = this.tickToMelt;
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar2 = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(4);add(5);add(ForgeDirection.UNKNOWN.ordinal());}};
		this.fluidAndSide.put(FluidRegistry.LAVA, ar2);
	}
	
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
		PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{FluidRegistry.LAVA.getID()}),this);
		PacketHandler.sendToAllAround(new PacketMightyFoundry(this),this);  
	}

	@Override
	public void processMachine() {        
        this.processInventory();
        
        if(this.isLocked && this.getStackInSlot(1)!=null)
        	this.processFoundry();
        if(this.isRunning && this.getStackInSlot(1)==null){
        	resetFoundry();
        } 
	}
	
	public void resetFoundry(){
		this.isRunning = false;
		this.progression = 0.0D;
		this.currentRecipeName = "none";
		this.numberofItemAllreadyCasted = 0;
	}

	public List<ItemStack> getInputStacks(){
		return new ArrayList<ItemStack>(){{
			add(getStackInSlot(2));
			add(getStackInSlot(3));
			add(getStackInSlot(4));
			add(getStackInSlot(5));
		}};
	}
	
	public boolean processFoundry(){
		ItemStack mold = this.getStackInSlot(1);
		if(this.currentRecipeName==null || this.currentRecipeName.equals("none")){
			ISFARecipe recipe = RecipesRegistry.instance.getRecipeWithMoldMeta(mold.getItemDamage());
			if(recipe!=null){
				this.currentRecipeName = recipe.getUid();
				this.initialTickToMelt = RecipeMightyFoundry.class.cast(recipe).getTickPerItem();
				this.tickToMelt = this.initialTickToMelt;
				this.currentTickToMelt = this.initialTickToMelt;
			}
		}
		ISFARecipe recipe = RecipesRegistry.instance.getRecipeForUsage(getUsageName(),this.currentRecipeName);
		if(recipe!=null){
			int itemQ = RecipeMightyFoundry.class.cast(recipe).getItemQuantity();
			this.progression = (double)this.numberofItemAllreadyCasted/(double)itemQ;
			if(this.isRunning){
				if(this.currentTickToMelt<this.tickToMelt){
					this.currentTickToMelt+=1;
				}else{
					if(this.numberofItemAllreadyCasted>=itemQ){
						this.setInventorySlotContents(6, RecipesRegistry.instance.getResultForRecipe(recipe));
						this.setInventorySlotContents(1, null);
						this.isLocked = false;
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						resetFoundry();
					}else{
						if(RecipesRegistry.instance.isRecipeCanBeDone(recipe, getInputStacks(), this)){
							this.extractEnergy(ForgeDirection.UNKNOWN, recipe.getEnergyCost(), false);
							if(this.tank.getFluidAmount()>=this.fluidNeededPerOneItem.amount){
								this.tank.drain(this.fluidNeededPerOneItem.amount, true);
								this.tickToMelt = this.initialTickToMelt/4;
							}else{
								this.tickToMelt = this.initialTickToMelt;
							}
							this.numberofItemAllreadyCasted+=1;
							this.currentTickToMelt = 0;
							int size = recipe.getInputs().size();
							for(RecipeInput ri: recipe.getInputs()){
								for(int i = 0;i < 4; i++){
									if(RecipesRegistry.instance.getInputFromItemStack(items[2+i]).isRecipeElementSame(ri)){
										items[2+i].stackSize -= ri.getItemStack().stackSize;
										if(items[2+i].stackSize<=0){
											items[2+i]=null;
										}
										size-=1;
									}
									if(size<=0)
										break;
								}
								if(size<=0)
									break;
							}
						}
					}
				}
			}else{
				this.isRunning = true;
				//this.currentRecipeName = recipe.getUid();
				this.numberofItemAllreadyCasted = 0;
			}
		}else{
			this.currentRecipeName = "none";
		}
				
		return false;
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

		nbt.setBoolean("isLocked", this.isLocked);
		nbt.setBoolean("isRunning", this.isRunning);
		nbt.setInteger("tickToMelt",this.tickToMelt);
		nbt.setInteger("currentTickToMelt",this.currentTickToMelt);
		nbt.setString("currentRecipeName", this.currentRecipeName);
		nbt.setInteger("numberofItemAllreadyCasted", this.numberofItemAllreadyCasted);
		
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

		this.isLocked = nbt.getBoolean("isLocked");
		this.isRunning = nbt.getBoolean("isRunning");
		this.tickToMelt = nbt.getInteger("tickToMelt");
		this.currentTickToMelt = nbt.getInteger("currentTickToMelt");
		this.currentRecipeName = nbt.getString("currentRecipeName");
		this.numberofItemAllreadyCasted = nbt.getInteger("numberofItemAllreadyCasted");
		
    }

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		ItemStack it = this.getStackInSlot(slot);
		if(slot==1 && this.isLocked)
			return null;
		if(it!=null){
			if(it.stackSize < i)
				this.setInventorySlotContents(slot, null);
			else{
				int s = it.stackSize;
				it = it.splitStack(i);
				if(s-i<=0)
					this.setInventorySlotContents(slot, null);
				this.markDirty();
			}
		}
		return it;
	}

	@Override
	public String getInventoryName() {
		return "MightyFoundry";
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return 	( slot==0 && fluid != null && fluid.getFluid()==FluidRegistry.LAVA ) || 
				( slot==1 && stack.getItem() instanceof ItemMold && ItemNBTHelper.getInteger(stack, "idmold", 0)>0 ) ||
				( slot>1 && slot<6 && this.getStackInSlot(1)!= null && isItemValidForSlot(1,this.getStackInSlot(1)) && this.isLocked );
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2,3,4,5};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack) && side!=0 || side!=1)
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if(this.isItemValidForSlot(slot, stack) && side!=0 || side!=1)
			return true;
		return false;
	}

	@Override
	public ItemStack[] getItems() {
		return this.items;
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}

	@Override
	public void addToWaila(List<String> list) {
		super.addToWaila(list);
		if(this.isRunning){
			boolean hasm = this.getStackInSlot(2)==null&&this.getStackInSlot(3)==null&&this.getStackInSlot(4)==null&&this.getStackInSlot(5)==null;
			list.add("Forging : "+(!this.isLocked?"Mold not locked":!hasm?(this.progression*100)+"%":"No materials"));
		}else{
			list.add(""+(this.getStackInSlot(1)==null?"No mold":!this.isLocked?"Mold not locked":"Mold locked"));
		}
	}

	@Override
	public String getUsageName() {
		return MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY;
	}

	@Override
	public int getEnergy() {
		return this.getEnergyStored(ForgeDirection.UNKNOWN);
	}

	@Override
	public int getFluid() {
		return 0;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeMightyFoundry.class;
	}
}
