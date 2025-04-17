package wtf.init;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.apache.commons.lang3.tuple.Pair;
import wtf.blocks.BlockSpeleothem;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.StoneAndOre;
import wtf.worldgen.replacers.Replacer;

public class BlockSets {

	public static HashMap<IBlockState, BlockSpeleothem> speleothemMap = new HashMap<>();
    public static HashSet<String> adjacentFracturingBlocks = new HashSet<>();
	public static HashMap<StoneAndOre, IBlockState> stoneAndOre = new HashMap<>();
	public static HashMap<Block, Float> explosiveBlocks = new HashMap<>();
    public static HashSet<Material> replaceableMaterial = new HashSet<>(Arrays.asList(Material.AIR, Material.GROUND, Material.ROCK, Material.WATER, Material.LAVA, Material.PLANTS, Material.SAND, Material.CLAY, Material.SNOW, Material.WEB));
	public static HashSet<Block> meltBlocks = new HashSet<>(Arrays.asList(Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.FLOWING_WATER, Blocks.FIRE));
	public static HashMap<Block, Replacer> replacementMap = new HashMap<>();
	public static HashMap<Pair<IBlockState, Modifier>, IBlockState> blockTransformer = new HashMap<>();

	public static IBlockState getTransformedState(IBlockState state, Modifier mod) {
		return blockTransformer.get(Pair.of(state, mod));
	}

    public static void initBlockSets() {
		explosiveBlocks.put(Blocks.TNT, 4F);
		explosiveBlocks.put(Blocks.REDSTONE_ORE, 2F);
		explosiveBlocks.put(Blocks.LIT_REDSTONE_ORE, 3F);
		explosiveBlocks.put(Blocks.REDSTONE_TORCH, 1F);
		explosiveBlocks.put(Blocks.REDSTONE_BLOCK, 8F);
		explosiveBlocks.put(Blocks.REDSTONE_WIRE, 0.9F);
	}
}
