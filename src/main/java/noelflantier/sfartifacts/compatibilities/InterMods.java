package noelflantier.sfartifacts.compatibilities;

import cpw.mods.fml.common.Loader;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class InterMods {
	public static boolean hasNei = false;
	public static boolean hasIc2 = false;
	public static double[] voltageTier = new double[]{32*32,128*128,256*256,256*256};
	
	public static void loadConfig(){
    	hasNei = Loader.isModLoaded("NotEnoughItems");
    	hasIc2 = Loader.isModLoaded("IC2");
	}
	
	public static double convertRFtoEU(int rf, int maxTier){
		return rf/(double)ModConfig.oneEuToRf;
	}
	public static int convertEUtoRF(double eu){
		return (int)(eu*ModConfig.oneEuToRf);
	}
	public static double injectEnergy(TileEntity sink, ForgeDirection fd, double amount, double voltage, boolean simulate){
		double demanded = Math.max(0, ((IEnergySink)sink).getDemandedEnergy());
		double accepted = Math.min(demanded, amount);
		if(!simulate)
			((IEnergySink)sink).injectEnergy(fd, amount, voltage);
		return amount-accepted;
	}
	public static double injectEnergy(TileEntity sink, ForgeDirection fd, double amount, boolean simulate){
		double demanded = Math.max(0, ((IEnergySink)sink).getDemandedEnergy());
		double accepted = Math.min(demanded, amount);
		if(!simulate)
			((IEnergySink)sink).injectEnergy(fd, amount, getVoltageByTier(sink));
		return amount-accepted;
	}
	public static double getVoltageByTier(TileEntity sink){
		return voltageTier[((IEnergySink)sink).getSinkTier()-1];
	}
	public static void loadIC2Tile(TileEntity tile){
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile)tile));
	}
	public static void unloadIC2Tile(TileEntity tile){
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile)tile));
	}
}
