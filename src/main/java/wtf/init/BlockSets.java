package wtf.init;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import wtf.utilities.wrappers.StateAndModifier;
import wtf.utilities.wrappers.StoneAndOre;
import wtf.worldgen.replacers.LavaReplacer;
import wtf.worldgen.replacers.NonSolidNoReplace;
import wtf.worldgen.replacers.Replacer;


public class BlockSets {

	public enum Modifier {
		COBBLE, CRACKED, LAVA_CRUST, MOSSY, WATER_DRIP, LAVA_DRIP, FROZEN, SOUL, BRICK
	}

	public static Set<String> adjacentFracturingBlocks = new HashSet<>();

	//private static IBlockState[] defOreStates = {Blocks.IRON_ORE.getDefaultState(), Blocks.DIAMOND_ORE.getDefaultState(), Blocks.LAPIS_ORE.getDefaultState(), Blocks.GOLD_ORE.getDefaultState(),
	//		Blocks.EMERALD_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState(), Blocks.LIT_REDSTONE_ORE.getDefaultState(), Blocks.COAL_ORE.getDefaultState()};
	public static HashMap<StoneAndOre, IBlockState> stoneAndOre = new HashMap<>();

	public static HashMap<Block, Float> explosiveBlocks = new HashMap<>();

	public static HashMap<Item, Item> itemReplacer = new HashMap<>();

	//WorldGenHashSets

	private static Block[] listReplaceBlocks = {Blocks.STONE, WTFContent.natural_sandstone, WTFContent.natural_red_sandstone, Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND, Blocks.AIR, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.OBSIDIAN, Blocks.WATER, Blocks.FLOWING_WATER, WTFContent.ice_patch, Blocks.NETHERRACK, Blocks.SNOW, Blocks.SNOW_LAYER};
	public static HashSet<Block> ReplaceHashset = new HashSet<>(Arrays.asList(listReplaceBlocks));

	private static Block[] listSurfaceBlocks = {Blocks.DIRT, Blocks.SAND, WTFContent.natural_sandstone, WTFContent.natural_red_sandstone, Blocks.GRASS, Blocks.STONE, Blocks.GRAVEL, Blocks.CLAY, Blocks.HARDENED_CLAY, Blocks.STAINED_HARDENED_CLAY};
	public static HashSet<Block> surfaceBlocks = new HashSet<>(Arrays.asList(listSurfaceBlocks));

	public static HashSet<Block> treeReplaceableBlocks = new HashSet<>();

	public static HashSet<Block> nonSolidBlockSet = new HashSet<>();
	public static HashSet<Block> liquidBlockSet = new HashSet<>();

	private static Block[] defMelt = {Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.FLOWING_WATER, Blocks.FIRE};
	public static HashSet<Block> meltBlocks = new HashSet<>(Arrays.asList(defMelt));

	public static HashMap<Block, Replacer> isNonSolidAndCheckReplacement = new HashMap<>();

	public static HashMap<StateAndModifier, IBlockState> blockTransformer = new HashMap<>();

	public static IBlockState getTransformedState(IBlockState oldstate, Modifier mod) {
		return blockTransformer.get(new StateAndModifier(oldstate, mod));
	}

	public static boolean hasCobble(IBlockState state) {
		return blockTransformer.containsKey(new StateAndModifier(state, Modifier.COBBLE));
	}

	private static HashSet<Block> cobble = new HashSet<>();
	
	public static HashSet<Block> riverBlocks = new HashSet<>();

	public static void initBlockSets() {
        for (Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
            if (!block.getDefaultState().isBlockNormalCube() && block.getDefaultState().getMaterial() != Material.WATER) {
                nonSolidBlockSet.add(block);
                if (!isNonSolidAndCheckReplacement.containsKey(block))
                    new NonSolidNoReplace(block);
            }

            if (block.getDefaultState().getMaterial() == Material.GROUND)
                surfaceBlocks.add(block);

            if (block.getDefaultState().getMaterial() == Material.AIR) {
                nonSolidBlockSet.add(block);
                if (!isNonSolidAndCheckReplacement.containsKey(block))
                    new NonSolidNoReplace(block);
            }

            if (block.getDefaultState().getMaterial() == Material.WATER) {
                liquidBlockSet.add(block);
                nonSolidBlockSet.add(block);
                if (!isNonSolidAndCheckReplacement.containsKey(block))
                    new NonSolidNoReplace(block);
            }
            if (block.getDefaultState().getMaterial() == Material.LAVA) {
                nonSolidBlockSet.add(block);
                nonSolidBlockSet.add(block);
                if (!isNonSolidAndCheckReplacement.containsKey(block))
                    new NonSolidNoReplace(block);
            }

            if (block.getDefaultState().getMaterial() == Material.SNOW) {
                nonSolidBlockSet.add(block);
                if (!isNonSolidAndCheckReplacement.containsKey(block))
                    new NonSolidNoReplace(block);
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
		
		//new NonSolidNoReplace(Blocks.BROWN_MUSHROOM_BLOCK);
		//new NonSolidNoReplace(Blocks.RED_MUSHROOM_BLOCK);
		new NonSolidNoReplace(Blocks.LEAVES);
		new NonSolidNoReplace(Blocks.LEAVES2);

		explosiveBlocks.put(Blocks.TNT, 4F);
		explosiveBlocks.put(Blocks.REDSTONE_ORE, 2F);
		explosiveBlocks.put(Blocks.LIT_REDSTONE_ORE, 3F);
		explosiveBlocks.put(Blocks.REDSTONE_TORCH, 1F);
		explosiveBlocks.put(Blocks.REDSTONE_BLOCK, 8F);
		//differentiate between lit and unlit redstone wire
		explosiveBlocks.put(Blocks.REDSTONE_WIRE, 0.9F);

		blockTransformer.put(new StateAndModifier(Blocks.COBBLESTONE.getDefaultState(), Modifier.MOSSY), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		blockTransformer.put(new StateAndModifier(blockTransformer.get(new StateAndModifier(Blocks.STONE.getDefaultState(), Modifier.MOSSY)), Modifier.COBBLE), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		
		blockTransformer.put(new StateAndModifier(Blocks.STONE.getDefaultState(), Modifier.BRICK), Blocks.STONEBRICK.getDefaultState());
		blockTransformer.put(new StateAndModifier(Blocks.NETHERRACK.getDefaultState(), Modifier.BRICK), Blocks.NETHER_BRICK.getDefaultState());
		blockTransformer.put(new StateAndModifier(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), Modifier.BRICK), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH));
		blockTransformer.put(new StateAndModifier(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), Modifier.BRICK), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH));
		blockTransformer.put(new StateAndModifier(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Modifier.BRICK), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE_SMOOTH));
		blockTransformer.put(new StateAndModifier(Blocks.DIRT.getDefaultState(), Modifier.BRICK), Blocks.BRICK_BLOCK.getDefaultState());
		blockTransformer.put(new StateAndModifier(Blocks.GRAVEL.getDefaultState(), Modifier.BRICK), Blocks.STONEBRICK.getDefaultState());
		blockTransformer.put(new StateAndModifier(Blocks.SAND.getDefaultState(), Modifier.BRICK), Blocks.SANDSTONE.getDefaultState());
		blockTransformer.put(new StateAndModifier(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), Modifier.BRICK), Blocks.RED_SANDSTONE.getDefaultState());
		
		for (Entry<StateAndModifier, IBlockState> entry : blockTransformer.entrySet()) {
			if (entry.getKey().modifier == Modifier.COBBLE)
				cobble.add(entry.getValue().getBlock());
		}

		new LavaReplacer(Blocks.LAVA); //replaces lava below y=11 in a biome specific manner
	}
}
