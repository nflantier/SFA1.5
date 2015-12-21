package noelflantier.sfartifacts.common.blocks.tiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.Coord4;

public abstract class TileAsgardianMachine extends TileMachine implements ITileCanHavePillar, ITileCanTakeRFonlyFromPillars{
	
	//STRUCTURE
	public Coord4 master;
	
	public TileAsgardianMachine(){
		
	}

	public TileAsgardianMachine(String name){
		super(name);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
    }

	@Override
    public void setVariables(Object... params){
    	if(params[0] instanceof Coord4){
    		this.master = (Coord4)params[0];
    	}
    }

	@Override
	public void init(){
		super.init();
	}
	
	@Override
	public Coord4 getMasterCoord() {
		return this.master;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {	
		if(ModConfig.isAMachinesWorksOnlyWithPillar){
			return 0;
		}	
		return this.isRedStoneEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {	
		if(ModConfig.isAMachinesWorksOnlyWithPillar){
			return 0;
		}	
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //STRUCTURE
        if(this.hasMaster()){
	        nbt.setInteger("masterx", this.getMasterX());
	        nbt.setInteger("mastery", this.getMasterY());
	        nbt.setInteger("masterz", this.getMasterZ());
        	nbt.setBoolean("hasmaster", true);
        }else
        	nbt.setBoolean("hasmaster", false);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //STRUCTURE
    	if(nbt.getBoolean("hasmaster"))
    		this.master = new Coord4(nbt.getInteger("masterx"),nbt.getInteger("mastery"),nbt.getInteger("masterz"));
    }
	
	@Override
	public int receiveOnlyFromPillars(int maxReceive, boolean simulate) {
		return this.isRedStoneEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	@Override
	public int extractOnlyFromPillars(int maxExtract, boolean simulate) {
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public void addToWaila(List<String> list) {
		super.addToWaila(list);
		if(this.hasMaster())
			list.add("Pillar at : "+this.master.x+", "+this.master.y+", "+this.master.z);
		else
			list.add("Not connected to a pillar");		
	}

}
