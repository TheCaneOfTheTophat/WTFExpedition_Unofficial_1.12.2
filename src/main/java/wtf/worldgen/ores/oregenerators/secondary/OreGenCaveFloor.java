package wtf.worldgen.ores.oregenerators.secondary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.utilities.wrappers.UnsortedChunkCaves;


public class OreGenCaveFloor extends OreGenAbstract {

	private final OreGenAbstract veinType;
	private final List<surface> surfaceList;
	
	public enum surface {
		floor, wall, ceiling
	}
	
	public OreGenCaveFloor(OreGenAbstract vein, IBlockState state, String name, ArrayList<surface> list) {
		super(state, name);
		veinType = vein;
		surfaceList = list;
	}

	//Not sure exactly why it's crashing...
	//something in the get random sets is returning a null
	//I mean, get wall is definitely going to be problematic, but that doesn't explain why the floow is causing the crash

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		if (caves.size() > 0) {
			int blocksPerChunk = this.getBlocksPerChunk(world, rand, pos, surfaceAverage, biomeLeniency);

			int maxHeight = Math.min(MathHelper.floor(maxGenRangeHeight * surfaceAverage), maxY);
			int minHeight = Math.max(MathHelper.floor(minGenRangeHeight * surfaceAverage), minY);

			//I will need to access the size of the vein to do partial generation chances like I am for the other veins- which means I would need to seperate out the veinsize method, and throw it in
			//the abstract
			//System.out.println("gen " + numToGenerate);
			int blockReqs = this.blocksReq();
			while (blocksPerChunk > blockReqs || (blocksPerChunk > 0 && rand.nextInt(blockReqs) < blocksPerChunk)){
				surface surfaceGen = surfaceList.get(rand.nextInt(surfaceList.size()));
				BlockPos orePos = null;
				CaveListWrapper cave = caves.getRandomCave(rand);
				CavePosition cavepos = cave.getRandomPosition(rand);
				
				if (cavepos != null) {
					switch (surfaceGen) {
					case ceiling:
						orePos = cavepos.getCeilingPos();
						break;
					case floor:
						orePos = cavepos.getFloorPos();
						break;
					case wall:
						orePos = cave.getRandomWall(rand);
						break;
					default:
						break;
					}

					if (orePos != null) {
						boolean generate = checkBiomes(world, orePos, biomeLeniency);

						if(!(orePos.getY() <= maxHeight) || !(orePos.getY() >= minHeight))
							generate = false;

						blocksPerChunk -= generate ? veinType.genVein(world, rand, orePos, surfaceAverage, caves) : 1;
					} else
						blocksPerChunk -= 1;
				} else
					blocksPerChunk -= 1;
			}
		}
	}

	@Override
	public int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves) {
		return 0;
	}

	@Override
	public int blocksReq() {
		return veinType.blocksReq();
	}
}
