package wtf.worldgen.trees;

import java.util.HashMap;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import wtf.worldgen.trees.types.AbstractTreeType;
import wtf.worldgen.trees.types.AcaciaTree;
import wtf.worldgen.trees.types.BigTree;
import wtf.worldgen.trees.types.JungleGiant;
import wtf.worldgen.trees.types.JungleTree;
import wtf.worldgen.trees.types.RedwoodTree;
import wtf.worldgen.trees.types.DarkOakTree;
import wtf.worldgen.trees.types.SimpleTree;
import wtf.worldgen.trees.types.SwampTree;
import wtf.worldgen.trees.types.Taiga1Tree;
import wtf.worldgen.trees.types.Taiga2Tree;

public class TreeTypeGetter {

	public static HashMap<WorldGenerator, AbstractTreeType> treeTypes = new HashMap<>();

	final static IBlockState woodOak = Blocks.LOG.getDefaultState();
	final static IBlockState woodSpruce = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
	final static IBlockState woodBirch = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
	final static IBlockState woodJungle = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
	final static IBlockState woodAcacia = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
	final static IBlockState darkOakBlocks = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);

	final static IBlockState leavesOak = Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
	final static IBlockState leavesSpruce = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
	final static IBlockState leavesBirch = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
	final static IBlockState leavesJungle = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
	final static IBlockState leavesAcacia = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
	final static IBlockState leavesDarkOak = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

	public static AbstractTreeType getTree(World world, WorldGenerator oldTree) {
		if (treeTypes.containsKey(oldTree))
			return treeTypes.get(oldTree);

		if (oldTree instanceof WorldGenShrub) {
			treeTypes.put(oldTree, null);
			return null;
		}

		if (oldTree instanceof WorldGenTrees) {
			IBlockState wood = ObfuscationReflectionHelper.getPrivateValue(WorldGenTrees.class, (WorldGenTrees)oldTree, "field_76532_c");
			IBlockState leaf = ObfuscationReflectionHelper.getPrivateValue(WorldGenTrees.class, (WorldGenTrees)oldTree, "field_76530_d");
			Boolean vines = ObfuscationReflectionHelper.getPrivateValue(WorldGenTrees.class, (WorldGenTrees)oldTree, "field_76531_b");

			if (wood == woodJungle) {
				treeTypes.put(oldTree, new JungleTree());
				return new JungleTree();
			} else {
				treeTypes.put(oldTree, new SimpleTree(wood, wood, leaf, vines));
				return new SimpleTree(wood, wood, leaf, vines);
			}
		} else if (oldTree instanceof WorldGenMegaPineTree) {
			treeTypes.put(oldTree, new RedwoodTree());
			return new RedwoodTree();
		} else if (oldTree instanceof WorldGenCanopyTree) {
			treeTypes.put(oldTree, new DarkOakTree());
			return new DarkOakTree();
		} else if (oldTree instanceof WorldGenBigTree) {
			treeTypes.put(oldTree, new BigTree(woodOak, woodOak, leavesOak));
			return new BigTree(woodOak, woodOak, leavesOak);
		} else if (oldTree instanceof WorldGenBirchTree) {
			treeTypes.put(oldTree, new SimpleTree(woodBirch, woodBirch, leavesBirch, false));
			return new SimpleTree(woodBirch, woodBirch, leavesBirch, false);
		} else if (oldTree instanceof WorldGenMegaJungle) {
			treeTypes.put(oldTree, new JungleGiant());
			return new JungleGiant();
		} else if (oldTree instanceof WorldGenSavannaTree) {
			treeTypes.put(oldTree, new AcaciaTree());
			return new AcaciaTree();
		} else if (oldTree instanceof WorldGenSwamp) {
			treeTypes.put(oldTree, new SwampTree());
			return new SwampTree();
		} else if (oldTree instanceof WorldGenTaiga1) {
			treeTypes.put(oldTree, new Taiga1Tree());
			return new Taiga1Tree();
		} else if (oldTree instanceof WorldGenTaiga2) {
			treeTypes.put(oldTree, new Taiga2Tree());
			return new Taiga2Tree();
		}
		//else if (Loader.isModLoaded("RTG") && OverworldGenConfig.replaceRTGBirches && oldTree instanceof TreeRTGBetulaPapyrifera){
		//	treeTypes.put(oldTree, new PoplarTree(world));
		//	return new PoplarTree(world);
		//}

		treeTypes.put(oldTree, null);
		//System.out.println("Skipping unsupported tree type- this tree will not be replaced, but instead generate normally " + oldTree.getClass());
		return null;
	}
}

