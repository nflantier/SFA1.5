package noelflantier.sfartifacts.common.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import cpw.mods.fml.common.IWorldGenerator;

public class ModWorldGenOre implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
		case 0:
			generateSurface(random, chunkX * 16, chunkZ * 16, world);
			break;
		case 1:
			generateEnd(random, chunkX * 16, chunkZ * 16, world);
			break;
		case -1:
			generateNether(random, chunkX * 16, chunkZ * 16, world);
			break;
		default:
			generateSurface(random, chunkX * 16, chunkZ * 16, world);
			break;
	}
	}

	private void generateNether(Random random, int x, int z, World world) {
		
	}

	private void generateEnd(Random random, int x, int z, World world) {
		
	}

	private void generateSurface(Random random, int x, int z, World world) {
		addOreSpawn(ModBlocks.blockOreAsgardite, Blocks.stone, world, random, x, z, 5, 12, 10, 40, 120);
		BiomeGenBase b = world.getBiomeGenForCoords(x, z);
		if(b.biomeID==35 || b.biomeID==36 || b.biomeID==163 || b.biomeID==164)
			addOreSpawn(ModBlocks.blockOreVibranium, Blocks.stone, world, random, x, z, 2, 4, 6, 3, 18);
		//this.genStandardOre1(1, this.diamondGen, 0, 16); 7vain
	}
	
	public void addOreSpawn(final Block block, final Block blockToReplace, final World world, final Random random, final int chunkXPos, final int chunkZPos, final int minVainSize, final int maxVainSize, final int chancesToSpawn, final int minY, final int maxY) {
		for (int i = 0; i < chancesToSpawn; i++) {
			final int posX = chunkXPos + random.nextInt(16);
			final int posY = minY + random.nextInt(maxY - minY);
			final int posZ = chunkZPos + random.nextInt(16);
			new WorldGenMinable(block, 0, minVainSize+random.nextInt(maxVainSize - minVainSize), blockToReplace).generate(world, random, posX, posY, posZ);
		}
	}
}
