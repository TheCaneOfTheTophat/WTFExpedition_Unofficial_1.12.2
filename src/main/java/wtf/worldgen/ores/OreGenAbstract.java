package wtf.worldgen.ores;

import java.util.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public abstract class OreGenAbstract {

	public final IBlockState oreBlock;

	public HashMap<BiomeDictionary.Type, Float> biomeModifier = new HashMap<>();
	public HashSet<Integer> dimension = new HashSet<>();
	public boolean dimensionWhiteList;
	
	public float maxGenRangeHeight;
	public float minGenRangeHeight;
	public int maxPerChunk;
	public int minPerChunk;
	public float veinDensity = 1F;
	public final SimplexHelper simplex;
	public boolean genDenseOres;
	public final ArrayList<BiomeDictionary.Type> reqBiomeTypes = new ArrayList<>();
	public boolean biomeWhiteList;
	public int biomeLeniency;
	
	public OreGenAbstract(IBlockState state, String name, int[] genRange, int[] minMaxPerChunk, boolean dimensionWhiteList, boolean biomeWhiteList, boolean denseGen, int biomeLeniency) {
		this.oreBlock = state;
		this.maxGenRangeHeight = genRange[1] / 100F;
		this.minGenRangeHeight = genRange[0] / 100F;
		this.maxPerChunk = minMaxPerChunk[1];
		this.minPerChunk = minMaxPerChunk[0];
		genDenseOres = denseGen;
		this.biomeLeniency = biomeLeniency;
		this.dimensionWhiteList = dimensionWhiteList;
		this.biomeWhiteList = biomeWhiteList;
		this.simplex = new SimplexHelper(name, true);
	}

	public OreGenAbstract(IBlockState state, int[] genRange, int[] minMaxPerChunk, boolean dimensionWhiteList, boolean biomeWhiteList, boolean denseGen, int biomeLeniency, SimplexHelper simplex) {
		this.oreBlock = state;
		this.maxGenRangeHeight = genRange[1] / 100F;
		this.minGenRangeHeight = genRange[0] / 100F;
		this.maxPerChunk = minMaxPerChunk[1];
		this.minPerChunk = minMaxPerChunk[0];
		genDenseOres = denseGen;
		this.biomeLeniency = biomeLeniency;
		this.dimensionWhiteList = dimensionWhiteList;
		this.biomeWhiteList = biomeWhiteList;
		this.simplex = simplex;
	}

	public final void generate(World world, Random random, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		boolean dimensionInList = this.dimension.contains(world.provider.getDimension());

		if (dimensionWhiteList == dimensionInList)
			doOreGen(world, random, pos, surfaceAverage, caves, water);
	}
	
	public abstract void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water);
	
	public abstract int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves);
	
	public abstract int blocksReq();
	
	protected int getBlocksPerChunk(World world, Random rand, ChunkPos pos, double surfaceAvg, int biomeLeniency) {
		int genNum = WTFExpeditionConfig.simplexOreGen ? (int) (simplex.get2DNoise(world, (pos.getXStart() + 8) / 8D, (pos.getZStart() + 8) / 8D) * (this.maxPerChunk - this.minPerChunk) + this.minPerChunk) : (int) (rand.nextFloat() * (maxPerChunk - minPerChunk) + minPerChunk);

		BlockPos centerPos = new BlockPos(pos.getXStart() + 16, surfaceAvg, pos.getZStart() + 16);
		Set<Type> biomeTypes = new HashSet<>();

		if(biomeLeniency == 0)
			biomeTypes = BiomeDictionary.getTypes(world.getBiome(centerPos));
		else {
			for(BlockPos boxPos : BlockPos.getAllInBoxMutable(centerPos.add(biomeLeniency, 0, biomeLeniency), centerPos.add(-biomeLeniency, 0, -biomeLeniency)))
				biomeTypes.addAll(BiomeDictionary.getTypes(world.getBiome(boxPos)));
		}

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

	public boolean checkBiomes(World world, BlockPos pos, int biomeLeniency) {
		boolean valid = true;

		if (!reqBiomeTypes.isEmpty()) {
			valid = false;
			Set<Biome> biomes = new HashSet<>();

			if(biomeLeniency == 0)
				biomes.add(world.getBiomeForCoordsBody(pos));
			else {
				for(BlockPos boxPos : BlockPos.getAllInBoxMutable(pos.add(biomeLeniency, 0, biomeLeniency), pos.add(-biomeLeniency, 0, -biomeLeniency)))
					biomes.add(world.getBiomeForCoordsBody(boxPos));
			}

			for (BiomeDictionary.Type type : reqBiomeTypes) {
				for(Biome biome : biomes) {
					boolean biomeInList = BiomeDictionary.hasType(biome, type);

					if (biomeWhiteList == biomeInList)
						valid = true;
				}
			}
		}

		return valid;
	}
}

