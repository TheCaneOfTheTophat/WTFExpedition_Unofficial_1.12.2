package wtf.worldgen.ores.oregenerators;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import wtf.worldgen.GenMethods;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public class OreGenCloud extends OreGenAbstract {

	public final double radius;

	public OreGenCloud(IBlockState state, int[] genRange, int[] minMaxPerChunk, boolean denseGen, int diameter) {
		super(state, genRange, minMaxPerChunk, denseGen);
		this.radius = diameter / 2F;
	}

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		int blocksPerChunk = this.getBlocksPerChunk(world, rand, pos, surfaceAverage);
		int blocksReq = this.blocksReq();

		while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && rand.nextInt(blocksReq) < blocksPerChunk)) {
			int x = pos.getXStart() + rand.nextInt(16);
			int y = this.getGenStartHeight(surfaceAverage, rand);
			int z = pos.getZStart() + rand.nextInt(16);
			boolean generate = true;

			BlockPos orePos = new BlockPos(x, y, z);

			Biome biome = world.getBiomeForCoordsBody(orePos);

			if (!reqBiomeTypes.isEmpty()) {
				for (BiomeDictionary.Type type : reqBiomeTypes) {
					if (!BiomeDictionary.hasType(biome, type))
						generate = false;
				}
			}

			blocksPerChunk -= generate ? genVein(world, rand, orePos, surfaceAverage, caves) : 1;
		}
	}

	@Override
	public int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves) {
		int blocksSet = 0;

		for (int xloop = (int) -radius; xloop < radius + 1; xloop++) {
			for (int yloop = (int) -radius; yloop < radius + 1; yloop++) {
				for (int zloop = (int) -radius; zloop < radius + 1; zloop++) {
					double distance = MathHelper.sqrt(xloop * xloop + yloop * yloop + zloop * zloop);
					if (distance < radius) {
						int densityToSet;

						if (genDenseOres)
							densityToSet = getDensityToSet(rand, pos.getY() + yloop, surfaceAverage, distance, radius);
						else
							densityToSet = 0;

						if (rand.nextFloat() < this.veinDensity / 2 + (1 - distance / radius) / 2) {
							blocksSet += densityToSet + 1;
							GenMethods.setOre(world, new BlockPos(pos.getX() + xloop, pos.getY() + yloop, pos.getZ() + zloop), this.oreBlock, densityToSet);
						}
					}
				}
			}
		}
		return blocksSet;

	}

	public int getDensityToSet(Random random, double height, double surfaceAvg, double radius, double maxRadius) {
		double depth = height / (surfaceAvg * maxGenRangeHeight);
		double rand = random.nextFloat() + random.nextFloat() - 1;
		double radRatio = radius / maxRadius;
		double density = depth * 1.5 + radRatio * 1.5 + rand;

		if (density < 1) {
			return 0;
		} else if (density > 2) {
			return 2;
		}

		return 1;
	}

	@Override
	public int blocksReq() {
		return (int) (1.3333 * Math.PI * radius * radius * radius * this.veinDensity) * 2;
	}
}
