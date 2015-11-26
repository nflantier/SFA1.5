package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.InjectorRecipe;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;

public class TileInjector extends TileMachine implements ITileUsingMaterials, ITileGlobalNBT, IUseSFARecipes{

	//PROCESSING
	public int tickToInject = 10;
	public boolean isRunning[] = new boolean[3];
	public int currentTickToInject[] = new int[3];
	public String currentRecipeName[] = new String[]{"none","none","none"};
	public int currentRecipeId[] = new int[3];

	//INVENTORY
	public ItemStack[] items = new ItemStack[13];
	
	public TileInjector(){
		super("Injector");
		this.hasFL = true;
		this.hasRF = true;
    	this.energyCapacity = 100000;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity/100);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.tankCapacity = 100000;
		this.tank.setCapacity(this.tankCapacity);
	}	
	
	@Override
	public void init(){
		super.init();
		//this.naturalEnergy = this.getNaturalEnergy(this.getBlockMetadata());
		for(ForgeDirection f:ForgeDirection.values()){
			this.recieveSides.add(f);
			this.extractSides.add(f);
		}
		this.fluidAndSide =  new Hashtable<Fluid, List<Integer>>();
		List<Integer> ar = new ArrayList<Integer>();
		ar.add(ForgeDirection.getOrientation(this.side).getOpposite().ordinal());
		ar.add(ForgeDirection.UNKNOWN.ordinal());
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, ar);
	}

	@Override
	public void processPackets() {	
		PacketHandler.sendToAllAround(new PacketEnergy(this.xCoord, this.yCoord, this.zCoord, this.getEnergyStored(ForgeDirection.UNKNOWN), this.getMaxEnergyStored(ForgeDirection.UNKNOWN)),this);
		PacketHandler.sendToAllAround(new PacketFluid(this.xCoord, this.yCoord, this.zCoord, new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new int[]{ModFluids.fluidLiquefiedAsgardite.getID()}),this);
		PacketHandler.sendToAllAround(new PacketInjector(this),this);
	}
    
	@Override
	public void processMachine() {
        if(!this.isRedStoneEnable){
        	if(this.tank.getFluidAmount()<=this.tank.getCapacity()){
	    		for(int or : this.fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite)){
	    			ForgeDirection fd = ForgeDirection.getOrientation(or);
	    			TileEntity t = this.worldObj.getTileEntity(this.xCoord+fd.offsetX,this.yCoord+fd.offsetY, this.zCoord+fd.offsetZ);
	        		if(t !=null && t instanceof IFluidHandler && t instanceof TileBlockPillar){
	        			ForgeDirection fdt = fd.getOpposite();
	        			FluidStack av = ((IFluidHandler)t).drain(fdt, new FluidStack(ModFluids.fluidLiquefiedAsgardite, 1000), false);
	        			if(av!=null && av.amount>0){
	    	    			int ci = this.fill(fd, av, false);
	    	    			if(ci>0){
	    	    				((IFluidHandler)t).drain(fdt, ci, true);
	    	    				this.fill(fd, av, true);
	    	    			}
	        			}
	        		}
	    		}
        	}
        	this.processInjecting();
        	this.processInventory();
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
	
	public void isRunningProcess(int idline){
		if(this.currentRecipeName[idline]!=null && !this.currentRecipeName[idline].equals("none")){
			ISFARecipe recipe = RecipesRegistry.instance.getRecipeForUsage(getUsageName(),this.currentRecipeName[idline]);
			if(recipe!=null && this.getEnergyStored(ForgeDirection.UNKNOWN)>=recipe.getEnergyCost()/this.tickToInject 
					&& this.getFluidTanks().get(0).getFluidAmount()>recipe.getFluidCost()/this.tickToInject){
				this.currentTickToInject[idline]-=1;
				if(this.getRandom(this.getBlockMetadata(),this.randomMachine)){
					this.extractEnergy(ForgeDirection.UNKNOWN, recipe.getEnergyCost()/this.tickToInject, false);
					this.tank.drain(recipe.getFluidCost()/this.tickToInject, true);
				}
				if(this.currentTickToInject[idline]<=0){
					if(RecipesRegistry.instance.canRecipeStack(recipe, getOutputStacks(idline))){
						int size = recipe.getOutputs().size();
						for(RecipeOutput ro : recipe.getOutputs()){
							if(ro.canStackWithItemStack(items[idline*2+6+1]) && size>0){
								size-=1;
								if(items[idline*2+6+1]==null){
									items[idline*2+6+1] = ro.getOutputItem().copy();
								}else{
									items[idline*2+6+1].stackSize+=ro.getOutputItem().stackSize;
								}
							}else if(ro.canStackWithItemStack(items[idline*2+6+1+1]) && size>0){
								size-=1;
								if(items[idline*2+6+1+1]==null){
									items[idline*2+6+1+1] = ro.getOutputItem().copy();
								}else{
									items[idline*2+6+1+1].stackSize+=ro.getOutputItem().stackSize;
								}
							}
							if(size<=0){
								break;
							}
						}
					}
					this.isRunning[idline] = false;
					this.currentRecipeName[idline] = "none";
				}
			}else{
				this.currentRecipeName[idline] = "";
			}
		}
		/*if(this.currentRecipeId[idline]!=-1){
			InjectorRecipe cr = InjectorRecipe.values()[this.currentRecipeId[idline]];
			if(this.getEnergyStored(ForgeDirection.UNKNOWN)>=cr.energyAmount/this.tickToInject 
					&& this.tank.getFluidAmount()>=cr.fluidAmount/this.tickToInject){
				this.currentTickToInject[idline]-=1;
				
				if(this.getRandom(this.getBlockMetadata(),this.randomMachine)){
					this.extractEnergy(ForgeDirection.UNKNOWN, cr.energyAmount/this.tickToInject, false);
					this.tank.drain(cr.fluidAmount/this.tickToInject, true);
				}
				
				if(this.currentTickToInject[idline]<=0){
					//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					for(int idcaseres =0;idcaseres<2;idcaseres++){
						if(this.items[idline*2+idcaseres+6+1]==null){
							this.items[idline*2+idcaseres+6+1] = new ItemStack(cr.result);
							this.isRunning[idline] = false;
							this.currentRecipeId[idline]=-1;
							return;
						}else if(this.items[idline*2+idcaseres+6+1].getItem()==cr.result){
							if(this.items[idline*2+idcaseres+6+1].stackSize+1<=this.items[idline*2+idcaseres+6+1].getMaxStackSize()){
								this.items[idline*2+idcaseres+6+1].stackSize++;
								this.isRunning[idline] = false;
								this.currentRecipeId[idline]=-1;
								return;
							}
						}
					}
				}
			}
		}*/
	}
	
	public void isNotRunningProcess(int idline){
		this.currentTickToInject[idline] = this.tickToInject;
		this.currentRecipeName[idline]= "none";

		List<ISFARecipe> recipes = RecipesRegistry.instance.getOrderedRecipes(this, RecipesRegistry.instance.getInputFromItemStack(getInputStacks(idline)));
		if(recipes!=null && !recipes.isEmpty()){
			for(ISFARecipe recipe : recipes){
				if(recipe!=null && recipe.getOutputs()!=null){
					if(RecipesRegistry.instance.canRecipeStack(recipe, getOutputStacks(idline))){
						this.currentRecipeName[idline]=recipe.getUid();
						this.isRunning[idline] = true;
						if(recipe.getInputs().size()<=1){
							if(items[idline*2+1] == null || !recipe.getInputs().get(0).isItemStackSame(items[idline*2+1]))
								items[idline*2+1+1] = RecipesRegistry.instance.processRecipeOnInput(recipe, items[idline*2+1+1]);
							items[idline*2+1] = RecipesRegistry.instance.processRecipeOnInput(recipe, items[idline*2+1]);
						}else{
							items[idline*2+1+1] = RecipesRegistry.instance.processRecipeOnInput(recipe, items[idline*2+1+1]);
							items[idline*2+1] = RecipesRegistry.instance.processRecipeOnInput(recipe, items[idline*2+1]);	
						}
						break;
					}
				}
			}
		}
	}

	public List<ItemStack> getInputStacks(int idline){
		return new ArrayList<ItemStack>(){{
			add(items[idline*2+1]);
			add(items[idline*2+1+1]);
		}};
	}
	
	public List<ItemStack> getOutputStacks(int idline){
		return new ArrayList<ItemStack>(){{
			add(items[idline*2+1+6]);
			add(items[idline*2+1+1+6]);
		}};
	}
	
	public int getStackSize(InjectorRecipe ir, ItemStack stack){
		for(ItemStack sta : ir.recipe){
			if(sta.getItem()==stack.getItem() && sta.getItemDamage()==stack.getItemDamage()){
				return sta.stackSize;
			}
		}
		return 0;
	}
	
	public boolean checkContainsItem(InjectorRecipe ir, ItemStack stack){
		for(ItemStack sta : ir.recipe){
			if(stack!=null && sta.getItem()==stack.getItem() && sta.getItemDamage()==stack.getItemDamage() && sta.stackSize<=stack.stackSize)
				return true;
		}
		return false;
	}
	
	public boolean processInjecting(){
		for(int i=0;i<this.isRunning.length;i++){
			if(this.isRunning[i]){
				this.isRunningProcess(i);
			}else{
				this.isNotRunningProcess(i);
			}
		}
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
        
        for(int i =0;i<this.isRunning.length;i++)
        	nbt.setBoolean("isRunning"+i, this.isRunning[i]);

        for(int i =0;i<this.currentTickToInject.length;i++)
        	nbt.setInteger("currentTickToInject"+i, this.currentTickToInject[i]);

        for(int i =0;i<this.currentRecipeId.length;i++)
        	nbt.setInteger("currentRecipeId"+i, this.currentRecipeId[i]);
        
        //for(int i =0;i<this.currentRecipeName.length;i++)
        //	nbt.setString("currentRecipeName"+i, this.currentRecipeName[i]);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        for(int i =0;i<this.isRunning.length;i++)
    		this.isRunning[i] = nbt.getBoolean("isRunning"+i);

        for(int i =0;i<this.currentTickToInject.length;i++)
    		this.currentTickToInject[i] = nbt.getInteger("currentTickToInject"+i);
        
        for(int i =0;i<this.currentRecipeId.length;i++)
    		this.currentRecipeId[i] = nbt.getInteger("currentRecipeId"+i);
        
        //for(int i =0;i<this.currentRecipeName.length;i++)
    		//this.currentRecipeName[i] = nbt.getString("currentRecipeName"+i);
    }
    
	@Override
	public ItemStack[] getItems() {
		return this.items;
	}
	

	@Override
	public String getInventoryName() {
		return "Injector";
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
	    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		return  ( slot==0 && fluid != null && fluid.getFluid()==ModFluids.fluidLiquefiedAsgardite ) || (slot>0 && slot<7);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
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
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
	
	@Override
	public String getUsageName() {
		return InjectorRecipesHandler.USAGE_INJECTOR;
	}
	@Override
	public int getEnergy() {
		return this.getEnergyStored(ForgeDirection.UNKNOWN);
	}
	@Override
	public int getFluid() {
		return this.getFluidTanks().get(0).getFluidAmount();
	}
	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeBase.class;
	}
}
