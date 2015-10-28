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
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void  loadEntities(){
		createEntity(EntityHammerMinning.class,"thorhammerthrow", EntityRegistry.findGlobalUniqueEntityId(), 100, 5, true);
		createEntity(EntityHammerInvoking.class,"thorhammerinvoking", EntityRegistry.findGlobalUniqueEntityId(), 100, 5, true);
		createEntity(EntityItemParticle.class,"itemparticle", EntityRegistry.findGlobalUniqueEntityId(), 32, 5, true);
		createEntity(EntityItemStronk.class,"itemstronk", EntityRegistry.findGlobalUniqueEntityId(), 32, 5, true);
		createEntity(EntityShieldThrow.class,"vibraniumshieldthrow", EntityRegistry.findGlobalUniqueEntityId(), 100, 5, true);
		int hulkid = EntityRegistry.findGlobalUniqueEntityId();
		createEntity(EntityHulk.class,"Hulk", hulkid, 100, 1, true);
		createEgg(hulkid, 0x239925, 0x136B23);
		//EntityRegistry.findGlobalUniqueEntityId()
	}
	
	public static void createEgg(int id, int solidColor, int spotColor){
		EntityList.entityEggs.put(Integer.valueOf(id), new EntityList.EntityEggInfo(id, solidColor, spotColor));
	}
	
	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo){
    	createEntity(entityClass,entityName,id);
    	EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo);
	}
	
	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id){
    	EntityRegistry.registerGlobalEntityID(entityClass, entityName, id);
	}
	
}
