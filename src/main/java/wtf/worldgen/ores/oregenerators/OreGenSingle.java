package wtf.worldgen.ores.oregenerators;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import wtf.worldgen.GenMethods;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public class OreGenSingle extends OreGenAbstract {

	public OreGenSingle(IBlockState state, String name) {
		super(state, name);
	}

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		int blocksPerChunk = this.getBlocksPerChunk(world, rand, pos, surfaceAverage, biomeLeniency);

		while (blocksPerChunk > 0) {
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
		int densityToSet;

		if (genDenseOres)
			densityToSet = getDensityToSet(rand, pos.getY(), surfaceAverage);
		else
			densityToSet = 0;

		GenMethods.setOre(world, pos, this.oreBlock, densityToSet);

		return densityToSet + 1;
	}

	@Override
	public int blocksReq() {
		return 2;
	}
}
