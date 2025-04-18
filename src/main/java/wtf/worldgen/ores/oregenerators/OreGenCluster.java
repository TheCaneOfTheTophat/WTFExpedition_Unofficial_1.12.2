package wtf.worldgen.ores.oregenerators;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import wtf.worldgen.GenMethods;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public class OreGenCluster extends OreGenAbstract {
	
	public OreGenCluster(IBlockState state, String name, int[] genRange, int[] minMaxPerChunk, boolean denseGen, int biomeLeniency) {
		super(state, name, genRange, minMaxPerChunk, denseGen, biomeLeniency);
	}

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		int blocksPerChunk = this.getBlocksPerChunk(world, rand, pos, surfaceAverage, biomeLeniency);
		int blocksReq = (int) (6F * this.veinDensity);

		while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && rand.nextInt(blocksReq) < blocksPerChunk)) {
			int x = pos.getXStart() + rand.nextInt(16) + 8;
			int y = this.getGenStartHeight(surfaceAverage, rand);
			int z = pos.getZStart() + rand.nextInt(16) + 8;

			BlockPos orePos = new BlockPos(x, y, z);
			boolean generate = checkBiomes(world, orePos, biomeLeniency);

			blocksPerChunk -= generate ? genVein(world, rand, orePos, surfaceAverage, caves) : 1;
		}
	}

	@Override
	public int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves) {
		int blocksPerChunk = 0;

		if (rand.nextFloat() < this.veinDensity)
			blocksPerChunk += genStarPosition(world, rand, pos, surfaceAverage);

		for(EnumFacing face : EnumFacing.values()) {
			if (rand.nextFloat() < this.veinDensity)
				blocksPerChunk += genStarPosition(world, rand, pos.offset(face), surfaceAverage);
		}

		return blocksPerChunk;
	}

	private int genStarPosition(World world, Random random, BlockPos pos, double surface) {
		int densityToSet;

		if (genDenseOres)
			densityToSet = getDensityToSet(random, pos.getY(), surface);
		else
			densityToSet = 0;

		GenMethods.setOre(world, new BlockPos(pos), this.oreBlock, densityToSet);
		return densityToSet + 1;
	}

	@Override
	public int blocksReq() {
		return (int) (12 * this.veinDensity);
	}
}
