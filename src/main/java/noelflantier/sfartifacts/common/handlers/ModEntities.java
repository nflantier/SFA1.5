package noelflantier.sfartifacts.common.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.entities.EntityHulk;
import noelflantier.sfartifacts.common.entities.EntityItemParticle;
import noelflantier.sfartifacts.common.entities.EntityItemStronk;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;
import noelflantier.sfartifacts.common.entities.EntityShieldThrowCaptain;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void  loadEntities(){
		createEntity(EntityHammerMinning.class,"thorhammerthrow", 0, 100, 5, true);
		createEntity(EntityHammerInvoking.class,"thorhammerinvoking", 1, 100, 5, true);
		createEntity(EntityItemParticle.class,"itemparticle", 2, 32, 5, true);
		createEntity(EntityItemStronk.class,"itemstronk", 3, 32, 5, true);
		createEntity(EntityShieldThrow.class,"vibraniumshieldthrow", 4, 100, 5, true);
		createEntity(EntityHulk.class,"hulk", 5, 100, 1, true);
		createEntity(EntityShieldThrowCaptain.class,"captainamericashieldthrow", 6, 100, 5, true);
		createEntity(EntityHoverBoard.class, "hoverboard", 7, 64, 1, true);
	}
		
	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo){
    	EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo);
	}
	
}
