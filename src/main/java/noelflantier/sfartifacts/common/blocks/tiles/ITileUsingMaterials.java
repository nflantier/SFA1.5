package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.Random;

import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public interface ITileUsingMaterials {
	default PillarMaterials getMaterial(int id){
		return PillarMaterials.values()[id];
	};
	default int getNaturalEnergy(int id){
		return getMaterial(id).naturalEnergy;
	};
	default void setNaturalEnergy(int ne, int id){
		ne = getNaturalEnergy(id);
	};
	default boolean getRandom(int id){
		Random rd = new Random();
		int f = rd.nextInt(100);
		return f>getNaturalEnergy(id);
	}
}
