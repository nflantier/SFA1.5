package noelflantier.sfartifacts.common.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
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
		createEntity(EntityHulk.class,"hulk", 5, 100, 1, true);//0x239925, 0x136B23
		createEntity(EntityShieldThrowCaptain.class,"captainamericashieldthrow", 6, 100, 5, true);
	}
	
	/*public static void createEgg(int id, int solidColor, int spotColor){
		EntityList.entityEggs.put(Integer.valueOf(id), new EntityList.EntityEggInfo(id, solidColor, spotColor));
	}*/
	
	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo){
    	//createEntity(entityClass,entityName,EntityRegistry.findGlobalUniqueEntityId());
    	EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo);
	}
	
	/*public static void createEntityWithEgg(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo, int color1, int color2){
    	int tid = EntityRegistry.findGlobalUniqueEntityId();
		//createEntity(entityClass,entityName,tid);
		//createEgg(tid, color1, color2);
    	//EntityRegistry.registerGlobalEntityID(entityClass, entityName, tid, color1, color2);
    	EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo);
	}*/
	
	/*public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id){
    	EntityRegistry.registerGlobalEntityID(entityClass, entityName, id);
	}*/
	
}
