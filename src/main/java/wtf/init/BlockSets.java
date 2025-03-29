package wtf.init;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.StoneAndOre;
import wtf.worldgen.replacers.Replacer;

public class BlockSets {

    public static HashSet<String> adjacentFracturingBlocks = new HashSet<>();

	public static HashMap<StoneAndOre, IBlockState> stoneAndOre = new HashMap<>();

	public static HashMap<Block, Float> explosiveBlocks = new HashMap<>();

    //WorldGenHashSets

	public static HashSet<Block> ReplaceHashset = new HashSet<>(Arrays.asList(Blocks.STONE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND, Blocks.AIR, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.OBSIDIAN, Blocks.WATER, Blocks.FLOWING_WATER, WTFContent.ice_patch, Blocks.NETHERRACK, Blocks.SNOW, Blocks.SNOW_LAYER));

	public static HashSet<Block> surfaceBlocks = new HashSet<>(Arrays.asList(Blocks.DIRT, Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.GRASS, Blocks.STONE, Blocks.GRAVEL, Blocks.CLAY, Blocks.HARDENED_CLAY, Blocks.STAINED_HARDENED_CLAY));

	public static HashSet<Block> treeReplaceableBlocks = new HashSet<>();

	public static HashSet<Block> nonSolidBlockSet = new HashSet<>();

	public static HashSet<Block> meltBlocks = new HashSet<>(Arrays.asList(Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.FLOWING_WATER, Blocks.FIRE));

	public static HashMap<Block, Replacer> replacementMap = new HashMap<>();

	public static HashMap<Pair<IBlockState, Modifier>, IBlockState> blockTransformer = new HashMap<>();

	public static IBlockState getTransformedState(IBlockState oldstate, Modifier mod) {
		return blockTransformer.get(Pair.of(oldstate, mod));
	}

    public static void initBlockSets() {

        for (Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
            if (!block.getDefaultState().isBlockNormalCube() && block.getDefaultState().getMaterial() != Material.WATER) {
                nonSolidBlockSet.add(block);
            }

            if (block.getDefaultState().getMaterial() == Material.GROUND)
                surfaceBlocks.add(block);

            if (block.getDefaultState().getMaterial() == Material.AIR) {
                nonSolidBlockSet.add(block);
            }

            if (block.getDefaultState().getMaterial() == Material.WATER) {
                nonSolidBlockSet.add(block);
            }
            if (block.getDefaultState().getMaterial() == Material.LAVA) {
                nonSolidBlockSet.add(block);
            }

            if (block.getDefaultState().getMaterial() == Material.SNOW) {
                nonSolidBlockSet.add(block);
            }

            if (block.getDefaultState().getMaterial() == Material.PLANTS) {
                ReplaceHashset.add(block);
            }

            try {
                if (block.isReplaceable(null, null)) {
                    treeReplaceableBlocks.add(block);
                    ReplaceHashset.add(block);
                }
            } catch (Exception e) {
                treeReplaceableBlocks.add(block);
            }

            if (block.getDefaultState().getMaterial() == Material.PLANTS)
                treeReplaceableBlocks.add(block);
        }

		explosiveBlocks.put(Blocks.TNT, 4F);
		explosiveBlocks.put(Blocks.REDSTONE_ORE, 2F);
		explosiveBlocks.put(Blocks.LIT_REDSTONE_ORE, 3F);
		explosiveBlocks.put(Blocks.REDSTONE_TORCH, 1F);
		explosiveBlocks.put(Blocks.REDSTONE_BLOCK, 8F);
		//differentiate between lit and unlit redstone wire
		explosiveBlocks.put(Blocks.REDSTONE_WIRE, 0.9F);

//		blockTransformer.put(Pair.of(Blocks.SAND.getDefaultState(), Modifier.BRICK), Blocks.SANDSTONE.getDefaultState());
//		blockTransformer.put(Pair.of(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), Modifier.BRICK), Blocks.RED_SANDSTONE.getDefaultState());
	}
}
