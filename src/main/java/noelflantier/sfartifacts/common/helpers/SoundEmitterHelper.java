package noelflantier.sfartifacts.common.helpers;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.entities.EntityHulk;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;

public class SoundEmitterHelper {
	
	public static int globalid = 0;
	public static int lowestFrequency = -1000;
	public static int highestFrequency = 1000;
	public static int clampFrequency = 0;
	public static Random seedingFrequency = new Random(133769);
	public static String KEY_SPAWN = "SpawnedBySoundEmitter";
	
	public static SoundEmitterHelper instance = new SoundEmitterHelper();
	public static Map<Integer, MobsPropertiesForSpawing> spawningRulesIDForRules = new HashMap<Integer, MobsPropertiesForSpawing>();
	public static Map<Class,Integer> classForID = new HashMap<Class,Integer>();
	public static Map<Integer,Integer> frequencyForID = new HashMap<Integer,Integer>();
	//private final Map<String, Map<String, IMachineRecipe>> machineRecipes = new HashMap<String, Map<String, IMachineRecipe>>();
	
	public static int addEntity(Class cl, MobsPropertiesForSpawing mpfs, boolean setfrequency){
		int cid = globalid++;
		mpfs.idEntity = cid;
		mpfs.classEntity = cl;
		spawningRulesIDForRules.put(cid, mpfs);
		classForID.put(cl, cid);
		if(setfrequency)
			setRandomFrequencyForId(cid);
		return cid;
	}

	public static int addEntity(Class cl, MobsPropertiesForSpawing mpfs, boolean setfrequency, String name){
		mpfs.nameEntity = name;
		return addEntity(cl,mpfs, setfrequency);
	}
	
	public static int addEntity(Class cl, MobsPropertiesForSpawing mpfs, int frequency){
		int i = addEntity(cl,mpfs, false);
		frequencyForID.put(frequency, i);
		return i;
	}
	
	public static ArrayList<Integer> getIdsForFrequency(int frequency){
		ArrayList<Integer> t = new ArrayList<Integer>();
		for(int i = frequency-clampFrequency;i<=frequency+clampFrequency;i++)
			if(frequencyForID.containsKey(i))
				t.add(frequencyForID.get(i));
		return t;				
	}
	
	public static int setRandomFrequencyForId(int id){
		int r = seedingFrequency.nextInt(lowestFrequency*-1+highestFrequency)-((lowestFrequency*-1+highestFrequency)/2);
		if(frequencyForID.containsKey(r))
			return setRandomFrequencyForId(id);
		frequencyForID.put(r, id);
		//System.out.println(".........................................................  "+r);
		return r;
	}
	
	public static void loadSpawningRules(){
		addEntity(EntityCreeper.class,new MobsPropertiesForSpawing(50,new FluidStack(ModFluids.fluidLiquefiedAsgardite,10),null), true);
		addEntity(EntitySkeleton.class,new MobsPropertiesForSpawing(50,new FluidStack(ModFluids.fluidLiquefiedAsgardite,10),null), true);
		addEntity(EntitySpider.class,new MobsPropertiesForSpawing(50,new FluidStack(ModFluids.fluidLiquefiedAsgardite,10),null), true);
		addEntity(EntityZombie.class,new MobsPropertiesForSpawing(50,new FluidStack(ModFluids.fluidLiquefiedAsgardite,10),null), true);
		addEntity(EntityGiantZombie.class,new MobsPropertiesForSpawing(1500,new FluidStack(ModFluids.fluidLiquefiedAsgardite,1500),null,1), true);
		addEntity(EntitySlime.class,new MobsPropertiesForSpawing(1500,new FluidStack(ModFluids.fluidLiquefiedAsgardite,800),null), true);
		addEntity(EntityGhast.class,new MobsPropertiesForSpawing(5000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,2000),null), true);
		addEntity(EntityPigZombie.class,new MobsPropertiesForSpawing(5000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,2000),null), true);
		addEntity(EntityEnderman.class,new MobsPropertiesForSpawing(5000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,2000),null), true);
		addEntity(EntitySilverfish.class,new MobsPropertiesForSpawing(500,new FluidStack(ModFluids.fluidLiquefiedAsgardite,300),null), true);
		addEntity(EntityBlaze.class,new MobsPropertiesForSpawing(1000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,500),null), true);
		addEntity(EntityMagmaCube.class,new MobsPropertiesForSpawing(1500,new FluidStack(ModFluids.fluidLiquefiedAsgardite,800),null), true);
		addEntity(EntityDragon.class,new MobsPropertiesForSpawing(250000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,50000),null,1), true);
		addEntity(EntityWither.class,new MobsPropertiesForSpawing(250000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,50000),null,1), true);
		addEntity(EntityWitch.class,new MobsPropertiesForSpawing(400,new FluidStack(ModFluids.fluidLiquefiedAsgardite,100),null), true);
		addEntity(EntityMooshroom.class,new MobsPropertiesForSpawing(200,new FluidStack(ModFluids.fluidLiquefiedAsgardite,100),null), true);
		addEntity(EntityIronGolem.class,new MobsPropertiesForSpawing(2500,new FluidStack(ModFluids.fluidLiquefiedAsgardite,800),null), true);
		addEntity(EntityVillager.class,new MobsPropertiesForSpawing(5000,new FluidStack(ModFluids.fluidLiquefiedAsgardite,2000),null), true);
		
		addEntity(EntityHulk.class,new MobsPropertiesForSpawing(ModConfig.rfNeededToSpawnHulk,new FluidStack(ModFluids.fluidLiquefiedAsgardite,ModConfig.fluidNeededToSpawnHulk),null,1), true);
		
		
		Iterator <Map.Entry<String,Class>>iterator = EntityList.stringToClassMapping.entrySet().iterator();
        while (iterator.hasNext()){
        	Map.Entry<String,Class> entry = iterator.next();
        	if(Utils.getAllSuperclasses(entry.getValue()).contains(EntityLivingBase.class) && !Modifier.isAbstract( entry.getValue().getModifiers() )){
        		if(!classForID.containsKey(entry.getValue())){
        			addEntity(entry.getValue(),new MobsPropertiesForSpawing(ModConfig.rfNeededToSpawnDefault,new FluidStack(ModFluids.fluidLiquefiedAsgardite,ModConfig.fluidNeededToSpawnDefault),null), true, entry.getKey());
        		}else{
        			if(spawningRulesIDForRules.get(classForID.get(entry.getValue()))!=null)
        				spawningRulesIDForRules.get(classForID.get(entry.getValue())).nameEntity = entry.getKey();
        		}
        	}
        }
	}
	
	public static class MobsPropertiesForSpawing {
		
		public int idEntity = -1;
		public Class classEntity;
		public String nameEntity;
		public int rfneeded = 0;
		public int nbMaxSpawing = -1;
		public FluidStack fluidneeded = new FluidStack(ModFluids.fluidLiquefiedAsgardite,0);
		public List<ItemStack> itemsneeded = null;
		
		public MobsPropertiesForSpawing(int rf, FluidStack fl, List<ItemStack> it){
			this.rfneeded = rf;
			this.fluidneeded = fl;
			this.itemsneeded = it;
		}
		
		public MobsPropertiesForSpawing(int rf, FluidStack fl, List<ItemStack> it, int nbMaxSpawning){
			this.rfneeded = rf;
			this.fluidneeded = fl;
			this.itemsneeded = it;
			this.nbMaxSpawing = nbMaxSpawning;
		}
	}
}
