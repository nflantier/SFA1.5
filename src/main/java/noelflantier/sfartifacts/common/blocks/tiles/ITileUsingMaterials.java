package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.Random;

import noelflantier.sfartifacts.common.helpers.PillarMaterials;

public interface ITileUsingMaterials {
	default PillarMaterials getMaterial(int id){
		return PillarMaterials.values()[id];
	};
	default float getEnergyRatio(int id){
		return getMaterial(id).energyRatio;
	};
	default void setNaturalEnergy(float ne, int id){
		ne = getEnergyRatio(id);
	};
	default boolean getRandom(int id, Random r){
		return r.nextInt(100)>getEnergyRatio(id);
	}
}
