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
import wtf.worldgen.WorldGenListener;

public abstract class OreGenAbstract {

	public final IBlockState oreBlock;

	public HashMap<BiomeDictionary.Type, Float> biomeModifier = new HashMap<>();
	public HashSet<Integer> dimension = new HashSet<>();
	public boolean dimensionWhiteList;
	
	public float maxGenRangeHeight;
	public float minGenRangeHeight;
	public int maxY;
	public int minY;
	public int maxPerChunk;
	public int minPerChunk;
	public float veinDensity = 1F;
	public final SimplexHelper simplex;
	public boolean genDenseOres;
	public final ArrayList<BiomeDictionary.Type> reqBiomeTypes = new ArrayList<>();
	public boolean biomeWhiteList;
	public int biomeLeniency;
	
	public OreGenAbstract(IBlockState state, String name) {
		this.oreBlock = state;

		if(WorldGenListener.oreSimplexMap.get(name) == null) {
			this.simplex = new SimplexHelper(name, true);
			WorldGenListener.oreSimplexMap.put(name, this.simplex);
		} else
			this.simplex = WorldGenListener.oreSimplexMap.get(name);
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
		int genNum = WTFExpeditionConfig.simplexOreGen ? (int) (simplex.get2DNoise(world, (pos.getXStart() + 16) * WTFExpeditionConfig.oreSimplexScale, (pos.getZStart() + 16) * WTFExpeditionConfig.oreSimplexScale) * (this.maxPerChunk - this.minPerChunk) + this.minPerChunk) : (int) (rand.nextFloat() * (maxPerChunk - minPerChunk) + minPerChunk);

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
		int maxHeight = Math.min(MathHelper.floor((float) (maxGenRangeHeight * surfaceAvg)), maxY);
		int minHeight = Math.max(MathHelper.floor((float) (minGenRangeHeight * surfaceAvg)), minY);
		int range = (maxHeight - minHeight) + 1;

		if (range < 1)
			range = 1;
		
		return random.nextInt(range) + minHeight;
	}

	public int getDensityToSet(Random random, double height, double surfaceAvg) {
		double depth = height / Math.min(surfaceAvg * maxGenRangeHeight, maxY);
		double rand = random.nextFloat() + random.nextFloat() - 1;
		
		double density = depth * 3 + rand;
		
		if (density < 1)
			return 0;
		else if (density > 2)
			return 2;

		return 1;
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

	public void setVeinDensity(float density) {
		this.veinDensity = density;
	}

	public void setSurfaceGenRange(int[] genRange) {
		this.maxGenRangeHeight = genRange[1] / 100F;
		this.minGenRangeHeight = genRange[0] / 100F;
	}

	public void setMinMaxPerChunk(int[] minMaxPerChunk) {
		this.maxPerChunk = minMaxPerChunk[1];
		this.minPerChunk = minMaxPerChunk[0];
	}

	public void setHardYGenRange(int[] genRange) {
		this.maxY = genRange[1];
		this.minY = genRange[0];
	}

	public void setDimensionWhiteList(boolean whiteList) {
		this.dimensionWhiteList = whiteList;
	}

	public void setBiomeWhiteList(boolean whiteList) {
		this.biomeWhiteList = whiteList;
	}

	public void setGenDenseOres(boolean denseOres) {
		this.genDenseOres = denseOres;
	}

	public void setBiomeLeniency(int biomeLeniency) {
		this.biomeLeniency = biomeLeniency;
	}
}

