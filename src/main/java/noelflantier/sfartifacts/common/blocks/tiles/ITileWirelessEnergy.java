package noelflantier.sfartifacts.common.blocks.tiles;

public interface ITileWirelessEnergy {
	int receiveEnergyWireless(int maxReceive, boolean simulate, int x, int y, int z);
	int extractEnergyWireless(int maxExtract, boolean simulate, int x, int y, int z);
}
