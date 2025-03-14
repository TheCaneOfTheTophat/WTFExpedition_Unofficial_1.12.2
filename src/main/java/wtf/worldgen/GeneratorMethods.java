package wtf.worldgen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.apache.commons.lang3.tuple.Pair;
import wtf.blocks.BlockIcicle;
import wtf.blocks.BlockRoots;
import wtf.blocks.BlockSpeleothem;
import wtf.enums.IcicleType;
import wtf.enums.Modifier;
import wtf.enums.SpeleothemType;
import wtf.config.WTFExpeditionConfig;
import wtf.init.BlockSets;
import wtf.init.WTFContent;
import wtf.utilities.wrappers.BlockMap;
import wtf.utilities.wrappers.ChunkCoords;
import wtf.worldgen.generators.queuedgen.QMobSpawner;
import wtf.worldgen.generators.queuedgen.QModify;
import wtf.worldgen.generators.queuedgen.QOreGen;
import wtf.worldgen.generators.queuedgen.QReplace;
import wtf.worldgen.generators.queuedgen.QReplaceNoCheck;
import wtf.worldgen.generators.queuedgen.QTreeReplace;


public class GeneratorMethods{

	public final Chunk chunk;
	public final BlockMap blockmap;
	public final Random random;

	public GeneratorMethods(World world, ChunkCoords coords, Random random) {
		chunk = coords.getChunk(world);
		blockmap = new BlockMap(world, coords);
		this.random = random;
	}

	public World getWorld(){
		return chunk.getWorld();
	}

	/**
	 **Used to set a block, based on a modifier and the block at the given location
	 **/
	public boolean transformBlock(BlockPos pos, Modifier modifier) {
		return blockmap.add(pos, new QModify(modifier));
	}

	/**
	 **Used to replace a block.  Checks that the block is replaceable first
	 **/
	public boolean replaceBlock(BlockPos pos, IBlockState state) {
		return blockmap.add(pos, new QReplace(state));
	}

	/**
	 **Used to replace a block without checking that the block is replaceable first
	 **/
	public boolean overrideBlock(BlockPos pos, IBlockState state) {
		return blockmap.add(pos, new QReplaceNoCheck(state));
	}

	public boolean setOreBlock(BlockPos pos, IBlockState oreState, int density) {
		return blockmap.add(pos, new QOreGen(oreState, density));
	}

	/**
	 **Used to set a block floor y+1, based on a modifier and the block at the given location.  e.g. cobblestone boulders on top of stone
	 **/
	public boolean setFloorAddon(BlockPos pos, Modifier modifier) {
		IBlockState oldState = getWorld().getBlockState(pos.down());
		IBlockState newState = BlockSets.blockTransformer.get(Pair.of(oldState, modifier));

		if (newState != null && !blockmap.posQueued(pos.down()))
			return replaceBlock(pos, newState);

		return false;
	}

	public boolean setCeilingAddon(BlockPos pos, Modifier modifier) {
		IBlockState oldState = getWorld().getBlockState(pos.up());
		IBlockState newState = BlockSets.blockTransformer.get(Pair.of(oldState, modifier));

		if (newState != null && !blockmap.posQueued(pos.up()))
			return replaceBlock(pos, newState);

		return false;
	}
	
	public void setWaterPatch(BlockPos pos) {
		if (WTFExpeditionConfig.enablePuddles)
			setPatch(pos, WTFContent.puddle.getDefaultState());
	}
	
	
	private Material[] repset = {Material.SNOW, Material.ICE, Material.PACKED_ICE, Material.LAVA, Material.WATER, Material.AIR, Material.GRASS};
	private HashSet<Material> rephashset = new HashSet<>(Arrays.asList(repset));
	
	public void setPatch(BlockPos pos, IBlockState patch) {
		if (isAir(pos.up())  && !rephashset.contains(getWorld().getBlockState(pos).getMaterial()))
			replaceBlock(pos.up(), patch);
	}

	public void genFloatingStone(BlockPos pos) {
		//When implementing non-vanilla stone, this method needs to call the UBifier
		replaceBlock(pos, Blocks.STONE.getDefaultState());
	}

	public boolean genSpeleothem(BlockPos pos, int size, float depth, boolean frozen) {

		if (blockmap.posQueued(pos.up()) || blockmap.posQueued(pos) || blockmap.posQueued(pos.down()))
			return false;

		if (frozen && depth > 0.9) {
			genIcicle(pos);
			return true;
		}

		IBlockState above = getWorld().getBlockState(pos.up());
		IBlockState below = getWorld().getBlockState(pos.down());
		int remaining = size;
		int direction;
		BlockSpeleothem speleothem;// = WTFBlocks.speleothemMap.get(oldState);
		
		if (isAir(pos.up()) && !isAir(pos.down())){
			direction = 1;
			speleothem = WTFContent.speleothemMap.get(below);
		} else if (!isAir(pos.up()) && isAir(pos.down())){
			direction = -1;
			speleothem = WTFContent.speleothemMap.get(above);
		} else
			return false;

		if (speleothem == null) {
			if (direction == -1) {
				if (above.getBlock().hashCode() == Blocks.DIRT.hashCode() && depth > 0.7) {
					genRoot(pos);
					return true;
				} else if (frozen || above.getMaterial() == Material.ICE || above.getMaterial() == Material.PACKED_ICE) {
					genIcicle(pos);
					return true;
				}
				//hanging glow shrooms
			}

			return false;	
		}
		
		if (depth > 1)
			return false;

		if (frozen)
			speleothem = speleothem.frozen;

		while (remaining > 0) {
			IBlockState next = getWorld().getBlockState(pos.up(direction));
			boolean nextQueued = blockmap.posQueued(pos.up(direction));
			IBlockState set;

			if (!nextQueued && remaining == size) {//first block
				if (isAir(pos.up(direction))) {
					if (size > 1)
						set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_base) : speleothem.getBlockState(SpeleothemType.stalactite_base);
					else
						set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_small) : speleothem.getBlockState(SpeleothemType.stalactite_small);
				} else
					return false; //cave size = 1, generate nothing
			} else if (!nextQueued && remaining > 1) { //middle block
				if (isAir(pos.up(direction)))
					set = speleothem.getBlockState(SpeleothemType.speleothem_column);
				else if (next.hashCode() == speleothem.parentBackground.hashCode()){
					set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalactite_base) : speleothem.getBlockState(SpeleothemType.stalagmite_base);
					remaining = 0;
				} else
					set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
			} else { //last block
				set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
			}

			replaceBlock(pos, set);
			pos = pos.up(direction);
			remaining --;
		}

		return true;
	}

	/**
	 **Generates an icicle hanging from the ceiling
	 **/
	public void genIcicle(BlockPos pos) {
		if (random.nextBoolean() && isAir(pos.down())) {
			replaceBlock(pos, WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_base));
			replaceBlock(pos.down(), WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_tip));
		} else
			replaceBlock(pos, WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_small));
	}

	/**
	 **Generates vines hanging from the ceiling
     **/
	public void GenVines(BlockPos pos, EnumFacing facing) {
		if (!isAir(pos))
			return;

		IBlockState block = null;

		switch(facing) {
		case EAST:
			block = Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST,  true);
			break;
		case NORTH:
			block = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH,  true);
			break;
		case SOUTH:
			block = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH,  true);
			break;
		case WEST:
			block = Blocks.VINE.getDefaultState().withProperty(BlockVine.WEST,  true);
			break;
		}

		replaceBlock(pos, block);
	}

	public void genRoot(BlockPos pos) {
		Biome biome = chunk.getBiome(pos, chunk.getWorld().getBiomeProvider());
		if (BiomeDictionary.hasType(biome, Type.CONIFEROUS))
			replaceBlock(pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.SPRUCE));
		else if (BiomeDictionary.hasType(biome, Type.SAVANNA))
			replaceBlock(pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.ACACIA));
		else if (BiomeDictionary.hasType(biome, Type.JUNGLE))
			replaceBlock(pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.JUNGLE));
		else if (biome.getBiomeName().contains("dark"))
			replaceBlock(pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.DARK_OAK));
		else if (biome.getBiomeName().contains("birch"))
			replaceBlock(pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.BIRCH));
		else
			replaceBlock(pos, WTFContent.roots.getDefaultState());
	}

	/**
	 **Checks if spawners are enabled, and then generates a mob spawner
	 **/
	public void spawnVanillaSpawner(BlockPos pos, ResourceLocation entityName, int count) {
		blockmap.add(pos, new QMobSpawner(this.getWorld(), pos, entityName, count));
	}

	private boolean isAir(BlockPos pos) {
		return getWorld().isAirBlock(pos) && !blockmap.posQueued(pos);
	}

	public boolean setTreeBlock(BlockPos pos, IBlockState state) {
		return blockmap.add(pos, new QTreeReplace(pos, state));
	}
}
