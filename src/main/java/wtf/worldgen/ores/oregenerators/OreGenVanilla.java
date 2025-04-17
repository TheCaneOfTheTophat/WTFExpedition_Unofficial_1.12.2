package wtf.worldgen.ores.oregenerators;

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

import java.util.ArrayList;
import java.util.Random;

public class OreGenVanilla extends OreGenAbstract {
	public OreGenVanilla(IBlockState state, String name, int[] genRange, int[] minMaxPerChunk, boolean denseGen, int blocks) {
		super(state, name, genRange, minMaxPerChunk, denseGen);
		blocksPerCluster = blocks;
	}

	protected final int blocksPerCluster;

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		int blocksPerChunk = this.getBlocksPerChunk(world, rand , pos, surfaceAverage);
		int blocksReq = this.blocksReq();
		
		while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && rand.nextInt(blocksReq) < blocksPerChunk)) {
			int genHeight = getGenStartHeight(surfaceAverage, rand);
			boolean generate = true;

			BlockPos orePos = new BlockPos(pos.getXStart() + 8, genHeight, pos.getZStart() + 8);

			Biome biome = world.getBiomeForCoordsBody(orePos);

			if (!reqBiomeTypes.isEmpty()) {
				for (BiomeDictionary.Type type : reqBiomeTypes) {
					if (!BiomeDictionary.hasType(biome, type))
						generate = false;
				}
			}

			blocksPerChunk -= generate ? genVein(world, rand , orePos, surfaceAverage, caves) : 1;
		}
	}

	@Override
	public int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves) {
		float f = rand.nextFloat() * (float) Math.PI;
		int blocksSet = 0;
		
		double d0 = pos.getX()+ MathHelper.sin(f) * blocksPerCluster / 8.0F;
		double d1 = pos.getX() - MathHelper.sin(f) * blocksPerCluster / 8.0F;
		
		double d2 = pos.getZ() + 8 + MathHelper.cos(f) * blocksPerCluster / 8.0F;
		double d3 = pos.getZ() + 8 - MathHelper.cos(f) * blocksPerCluster / 8.0F;
		
		double d4 = pos.getY() + rand.nextInt(3) - 2;
		double d5 = pos.getY() + rand.nextInt(3) - 2;

		for (int l = 0; l <= blocksPerCluster; ++l) {
			double d6 = d0 + (d1 - d0) * l / blocksPerCluster;
			double d7 = d4 + (d5 - d4) * l / blocksPerCluster;
			double d8 = d2 + (d3 - d2) * l / blocksPerCluster;
			double d9 = rand.nextDouble() * blocksPerCluster / 16.0D;
			double d10 = (MathHelper.sin(l * (float)Math.PI / blocksPerCluster) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin(l * (float)Math.PI / blocksPerCluster) + 1.0F) * d9 + 1.0D;
			int i1 = MathHelper.floor(d6 - d10 / 2.0D);
			int j1 = MathHelper.floor(d7 - d11 / 2.0D);
			int k1 = MathHelper.floor(d8 - d10 / 2.0D);
			int l1 = MathHelper.floor(d6 + d10 / 2.0D);
			int i2 = MathHelper.floor(d7 + d11 / 2.0D);
			int j2 = MathHelper.floor(d8 + d10 / 2.0D);

			for (int k2 = i1; k2 <= l1; ++k2) {
				double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);

				if (d12 * d12 < 1.0D) {
					for (int l2 = j1; l2 <= i2; ++l2) {
						double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);

						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int i3 = k1; i3 <= j2; ++i3) {
								int densityToSet;

								if (genDenseOres)
									densityToSet = getDensityToSet(rand, l2, surfaceAverage);
								else
									densityToSet = 0;

								if (rand.nextFloat() < this.veinDensity) {
									blocksSet += densityToSet + 1;
									GenMethods.setOre(world, new BlockPos(k2, l2, i3), this.oreBlock, densityToSet);
								}
							}
						}
					}
				}
			}
		}

		return blocksSet;
	}

	@Override
	public int blocksReq() {
		return (int) (blocksPerCluster * this.veinDensity) * 2;
	}
}
