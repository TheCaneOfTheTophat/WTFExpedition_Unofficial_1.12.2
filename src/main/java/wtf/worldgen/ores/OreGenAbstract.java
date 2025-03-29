package wtf.worldgen.ores;

import java.util.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public abstract class OreGenAbstract {

	public final IBlockState oreBlock;

	public HashMap<BiomeDictionary.Type, Float> biomeModifier = new HashMap<>();
	public HashSet<Integer> dimension = new HashSet<>();
	
	public float maxGenRangeHeight;
	public float minGenRangeHeight;
	public int maxPerChunk;
	public int minPerChunk;
	public float veinDensity = 1F;
	private final SimplexHelper simplex;
	public boolean genDenseOres;
	public final ArrayList<BiomeDictionary.Type> reqBiomeTypes = new ArrayList<>();
	
	public OreGenAbstract(IBlockState state, int[] genRange, int[] minMaxPerChunk, boolean denseGen) {
		this.oreBlock = state;
		this.maxGenRangeHeight = genRange[1] / 100F;
		this.minGenRangeHeight = genRange[0] / 100F;
		this.maxPerChunk = minMaxPerChunk[1];
		this.minPerChunk = minMaxPerChunk[0];
		genDenseOres = denseGen;
		simplex = new SimplexHelper(state.toString());
	}

	public final void generate(World world, Random random, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		if (this.dimension.contains(world.provider.getDimension()))
			doOreGen(world, random, pos, surfaceAverage, caves, water);
	}
	
	public abstract void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water);
	
	public abstract int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves);
	
	public abstract int blocksReq();
	
	protected int getBlocksPerChunk(World world, Random rand, ChunkPos pos, double surfaceAvg) {
		int genNum = WTFExpeditionConfig.simplexOreGen ? (int) (simplex.get2DNoise(world, pos.getXStart() / 8D, pos.getZStart() / 8D) * (this.maxPerChunk - this.minPerChunk) + this.minPerChunk) : (int) (rand.nextFloat() * (maxPerChunk - minPerChunk) + minPerChunk);

		Set<Type> biomeTypes = BiomeDictionary.getTypes(world.getBiome(new BlockPos(pos.getXStart() + 8, surfaceAvg, pos.getZStart() + 8)));
		for (Type biome : biomeTypes) {
			if (biomeModifier.containsKey(biome)) {
				genNum += (int) ((minPerChunk + (maxPerChunk - minPerChunk) / 2F) * biomeModifier.get(biome));
			}
		}

		return (int) (genNum * (float) surfaceAvg / world.getSeaLevel());
	}

	public int getGenStartHeight(double surfaceAvg, Random random) {
		int maxHeight = MathHelper.floor((float) (maxGenRangeHeight * surfaceAvg));
		int minHeight = MathHelper.floor((float) (minGenRangeHeight * surfaceAvg));
		int range = maxHeight - minHeight;

		if (range < 1)
			range = 1;
		
		return random.nextInt(range) + minHeight;
	}

	public int getDensityToSet(Random random, double height, double surfaceAvg) {
		double depth = height / (surfaceAvg * maxGenRangeHeight);
		double rand = random.nextFloat() + random.nextFloat() - 1;
		
		double density = depth * 3 + rand;
		
		if (density < 1)
			return 0;
		else if (density > 2)
			return 2;

		return 1;
	}

	public void setVeinDensity(float density) {
		this.veinDensity = density;
	}
}

