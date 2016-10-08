package wtf.ores.oregenerators;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.ores.ChunkDividedOreMap;
import wtf.ores.OreGenAbstract;
import wtf.utilities.wrappers.ChunkCoords;
import wtf.utilities.wrappers.ChunkScan;
import wtf.utilities.wrappers.OrePos;

public class OreGenCluster extends OreGenAbstract {

	public OreGenCluster(IBlockState blockstate, float maxGenRangeHeight, float minGenRangeHeight, int maxPerChunk,	int minPerChunk, boolean genDense) {
		super(blockstate, maxGenRangeHeight, minGenRangeHeight, maxPerChunk, minPerChunk, genDense);

	}

	
	@Override
	public void doOreGen(World world, ChunkDividedOreMap map, Random random, ChunkCoords coords, ChunkScan chunkscan) throws Exception {

		int blocksPerChunk = this.getBlocksPerChunk(world, coords, random, chunkscan.surfaceAvg);

		int blocksReq = (int) (6F*this.veinDensity);

		while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && random.nextInt(blocksReq) < blocksPerChunk)){
			int x=coords.getWorldX()+random.nextInt(12)+2;
			int y = this.getGenStartHeight(chunkscan.surfaceAvg, random);
			int z = coords.getWorldZ()+random.nextInt(16);

			blocksPerChunk -= genVein(world, map, random, chunkscan, new BlockPos(x,y,z));
						
		}
	}

	@Override
	public int genVein(World world, ChunkDividedOreMap map, Random random, ChunkScan chunkscan, BlockPos pos) throws Exception {
		int blocksPerChunk=0;
		

		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos, chunkscan.surfaceAvg);
		}
		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos.up(), chunkscan.surfaceAvg);
		}
		blocksPerChunk += genStarPosition(map, world, random, pos.down(), chunkscan.surfaceAvg);
		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos.north(), chunkscan.surfaceAvg);
		}
		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos.south(), chunkscan.surfaceAvg);
		}
		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos.east(), chunkscan.surfaceAvg);
		}
		if (random.nextFloat() < this.veinDensity){
			blocksPerChunk += genStarPosition(map, world, random, pos.west(), chunkscan.surfaceAvg);
		}
		return blocksPerChunk;

	}

	private int genStarPosition(ChunkDividedOreMap map, World world, Random random, BlockPos pos, double surface) throws Exception {
		int densityToSet;
		if (genDenseOres){
			densityToSet = getDensityToSet(random, pos.getY(), surface);
		}
		else {
			densityToSet = 0;
		}

		map.put(new OrePos(pos, densityToSet), this.oreBlock);
		return densityToSet+1;
	}


	@Override
	public int blocksReq() {
		// TODO Auto-generated method stub
		return (int) (12*this.veinDensity);
	}





}
