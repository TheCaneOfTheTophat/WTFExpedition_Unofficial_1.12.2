package wtf.worldgen.ores.oregenerators.secondary;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public class OreGenUnderWater extends OreGenAbstract {

	public OreGenUnderWater(OreGenAbstract vein, IBlockState state, int[] genRange, int[] minMaxPerChunk, boolean denseGen) {
		super(state, genRange, minMaxPerChunk, denseGen, vein.simplex);
		veinType = vein;
	}

	private final OreGenAbstract veinType;
	
	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		if (!water.isEmpty()) {
			int blocksPerChunk = this.getBlocksPerChunk(world,rand , pos, surfaceAverage);
			int blocksReq = this.blocksReq();

			int maxHeight = MathHelper.floor(maxGenRangeHeight * surfaceAverage);
			int minHeight = MathHelper.floor(minGenRangeHeight * surfaceAverage);
		
			while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && rand.nextInt(blocksReq) < blocksPerChunk)) {
				boolean generate = true;

				if(water.isEmpty())
					break;

				BlockPos orePos = water.get(rand.nextInt(water.size()));

				Biome biome = world.getBiomeForCoordsBody(orePos);

				if (!reqBiomeTypes.isEmpty()) {
					for (BiomeDictionary.Type type : reqBiomeTypes) {
						if (!BiomeDictionary.hasType(biome, type))
							generate = false;
					}
				}

				if(orePos.getY() <= maxHeight || orePos.getY() >= minHeight)
					blocksPerChunk -= generate ? veinType.genVein(world, rand, orePos, surfaceAverage, caves) : 1;
				else
					water.remove(orePos);
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
