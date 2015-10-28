package noelflantier.sfartifacts.common.world.village;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.helpers.PillarStructures;

public class ComponentPillar  extends StructureVillagePieces.House1{

    private int averageGroundLevel = -1;
    public ComponentPillar()
    {
    }

    public ComponentPillar(Start villagePiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super();
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
    }
    
	public static Object buildComponent(Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 7, 19, 7, p4);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentPillar(startPiece, p5, random, structureboundingbox, p4) : null;
	}

    @Override
    public boolean addComponentParts (World world, Random random, StructureBoundingBox sbb)
    {
    	
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 18, 0);
        }
        
        Random rd = new Random();
        int material = rd.nextInt(PillarMaterials.values().length);
        int pillar = rd.nextInt(PillarStructures.values().length)+1;
        PillarStructures ps = PillarStructures.getStructureFromId(pillar);
        String str ="0_0_0";
        while(!str.equals("end")){
        	String[] strParts = str.split("_");
        	int xT = Integer.parseInt(strParts[0]) + 3;
        	int yT = Integer.parseInt(strParts[1]);
        	int zT = Integer.parseInt(strParts[2]) + 3;
            str = (String)ps.structure.get(str);
            
        	if(random.nextFloat()<0.80)
        		this.placeBlockAtCurrentPosition(world, PillarMaterials.values()[material].block, 0, xT, yT, zT, sbb);
        }
    	return true;
    }
}
