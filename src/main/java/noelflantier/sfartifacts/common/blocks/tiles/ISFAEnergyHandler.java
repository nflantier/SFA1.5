package noelflantier.sfartifacts.common.blocks.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public interface ISFAEnergyHandler extends IEnergyHandler{

	public EnergyStorage getEnergyStorage();
	public void setLastEnergyStored(int lastEnergyStored);
}
