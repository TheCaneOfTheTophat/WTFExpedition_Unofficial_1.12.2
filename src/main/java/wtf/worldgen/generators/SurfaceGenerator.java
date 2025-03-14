package wtf.worldgen.generators;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.config.WTFExpeditionConfig;
import wtf.enums.Modifier;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.ChunkCoords;
import wtf.utilities.wrappers.ChunkScan;
import wtf.utilities.wrappers.SurfacePos;
import wtf.worldgen.GeneratorMethods;

public class SurfaceGenerator {

	private static SimplexHelper simplex = new SimplexHelper("SurfaceGenerator");
	
	public void generate(World world, ChunkCoords chunkcoords, Random random, ChunkScan chunkscan, GeneratorMethods gen) {
		for (SurfacePos[] posArray : chunkscan.surface) {
			for (SurfacePos pos : posArray) {
				Biome biome = world.getBiome(pos);
				if (BiomeDictionary.hasType(biome, Type.RIVER)){
					if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.riverFracturedBlockPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.riverFracturedBlockPercent)
						gen.transformBlock(pos, Modifier.FRACTURED);
				} else if (BiomeDictionary.hasType(biome, Type.MOUNTAIN)) {
					if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.mountainFracturedChunkPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.mountainFracturedBlockPercent)
						gen.transformBlock(pos, Modifier.FRACTURED);
				} else if (BiomeDictionary.hasType(biome, Type.FOREST )) {
					if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.forestMossyChunkPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.forestMossyBlockPercent)
						gen.transformBlock(pos, Modifier.MOSS);
				}
			}
		}
	}
}
