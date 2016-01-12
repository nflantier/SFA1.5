package noelflantier.sfartifacts.common.world.village;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class VillagePillarHandler  implements IVillageCreationHandler{

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return  new PieceWeight(ComponentPillar.class, 50, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return ComponentPillar.class;
	}

	@Override
	public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
		return ComponentPillar.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}

}
