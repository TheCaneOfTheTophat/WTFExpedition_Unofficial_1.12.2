package wtf.worldgen;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.SurfacePos;
import wtf.worldgen.trees.TreeGenMethods;
import wtf.worldgen.trees.TreeInstance;
import wtf.worldgen.trees.TreeTypeGetter;
import wtf.worldgen.trees.types.AbstractTreeType;
import wtf.worldgen.trees.types.Shrub;

public class TreeGenerator {

	private static SimplexHelper simplex = new SimplexHelper("TreeSimplex");

	public static boolean shouldTreePosGenerate(World world, Random random, BlockPos pos) {
		double noise = (simplex.get2DNoise(world, pos.getX() / 32D, pos.getZ() / 32D));
		double rand = random.nextFloat(); 
		double noise2 = noise - (noise - WTFExpeditionConfig.bigTreeReplacementPercentage) * WTFExpeditionConfig.simplexBigTreeScale;
		return rand < noise2;
	}

	public static void generate(World world, Random random, SurfacePos[][] surfacePositions) {
		Queue<Shrub> shrubQ = new LinkedList<>();
		
		for (int loopx = 0; loopx < 4; loopx++) {
			for (int loopz = 0; loopz < 4; loopz++) {
				int rand1 = random.nextInt(4);
				int rand2 = random.nextInt(4);

				SurfacePos pos = surfacePositions[4 * loopx + rand1][4 * loopz + rand2];
				Biome biome = world.getBiome(pos);
				double numTrees = Math.max(biome.decorator.treesPerChunk, 0);

				if(biome instanceof BiomeForest && ObfuscationReflectionHelper.getPrivateValue(BiomeForest.class, ((BiomeForest) biome), "type") == BiomeForest.Type.ROOFED)
					numTrees = 16;

				double genChance = (16 * numTrees / 256) + (biome.decorator.extraTreeChance / 16F);
				
				if (random.nextFloat() > genChance || pos.generated || !shouldTreePosGenerate(world, random, pos))
					continue;
				
				WorldGenerator oldTree = biome.getRandomTreeFeature(random);
				
				if (oldTree instanceof WorldGenShrub)
						shrubQ.add(new Shrub((WorldGenShrub) oldTree));
				else if (oldTree == null)
					continue;

				AbstractTreeType treeType = TreeTypeGetter.getTree(world, oldTree);
				
				if (treeType != null) {
					try {
						for(int x0 = -2; x0 < 3; x0++) {
							for (int z0 = -2; z0 < 3; z0++) {
								int x = Math.max(Math.min(4 * loopx + rand1 + x0, 15), 0);
								int z = Math.max(Math.min(4 * loopz + rand2 + z0, 15), 0);

								surfacePositions[x][z].setGenerated();
							}
						}

                        TreeGenMethods.tryGenerate(new TreeInstance(world, random, pos, simplex.get2DNoise(world, pos.getX() / 100D, pos.getZ() / 100D), treeType));
                    }
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		while (!shrubQ.isEmpty()) {
			SurfacePos surfacePos = null;

			for(int tryloop = 0; tryloop < 5; tryloop++) {
				int x = random.nextInt(16);
				int z = random.nextInt(16);

				if(!surfacePositions[x][z].generated) {
					surfacePos = surfacePositions[x][z];
					break;
				}
			}

			shrubQ.poll().generate(world, random, surfacePos);
		}
	}
}
