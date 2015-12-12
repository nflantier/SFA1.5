package noelflantier.sfartifacts.common.blocks.tiles;

public interface ITileCanTakeRFonlyFromPillars {
	int receiveOnlyFromPillars(int maxReceive, boolean simulate);
	int extractOnlyFromPillars(int maxExtract, boolean simulate);
}
