package wtf.worldgen.trees.types;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.config.WTFExpeditionConfig;
import wtf.worldgen.trees.TreeGenMethods;
import wtf.worldgen.trees.TreeInstance;
import wtf.worldgen.trees.components.Branch;

public class JungleTree extends AbstractTreeType {

	public JungleTree() {
		super(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false));

		leafYMin = 0;
		leafYMax = 2;
		leafRad = WTFExpeditionConfig.shrinkMassiveTrees ? 2.0 : 3.5;
		airGenerate = true;
		airGenHeight = 3;
		growDense = false;
		cocoa = true;
		rootFinalAngle = 0.5;
		vines = 3;
		rootInitialAngle = 1F;
		rootIncrementAngle = 0.3F;
	}

	@Override
	public int getBranchesPerNode(double nodeHeight, double scale) {
		return 3;
		//(int) MathHelper.clamp(random.nextInt(5-3), 1, 2);
	}

	@Override
	public double getBranchRotation(double scale, double numBranches) {
		return numBranches;
	}

	@Override
	public double getBranchSeperation(double scale) {
		return random.nextInt(3) + 3;
	}

	@Override
	public double getBranchPitch(double scale) {
		return 0.3 + random.nextFloat() / 5;
	}

	@Override
	public double getBranchLength(double scale, double trunkHeight, double nodeHeight) {
		double bottom = this.getLowestBranchRatio() * trunkHeight;
		double distFromBottom = nodeHeight - bottom;
		double branchSectionLength = trunkHeight - bottom;
		double taper = 1 - MathHelper.clamp(distFromBottom / branchSectionLength, 0.1, 0.9);

		double length = trunkHeight / 2.5 * taper;
		return WTFExpeditionConfig.shrinkMassiveTrees ? Math.min(length, 4) : length;
	}

	@Override
	public double getTrunkHeight(double scale) {
		return 16 + random.nextInt(16) + 16 * scale;
	}

	@Override
	public double getRootLength(double trunkHeight) {
		return trunkHeight / (WTFExpeditionConfig.shrinkMassiveTrees ? 6 : 3);
	}

	@Override
	public double getTrunkDiameter(double scale) {
		return 2;
	}

	@Override
	public int getTrunkColumnHeight(double trunkHeight, double currentRadius, double maxRadius) {
		return (int) trunkHeight;
	}

	@Override
	public double getLowestBranchRatio() {
		return random.nextFloat() / 2 + 0.25;
	}

	@Override
	public int getNumRoots(double trunkDiameter) {
		return MathHelper.floor(PId2 * (trunkDiameter + 1));
	}

	@Override
	public void doLeafNode(TreeInstance tree, Branch branch, BlockPos pos) {
		double height = pos.getY() - tree.y;
		double taper = MathHelper.clamp((tree.type.leafTaper) * (tree.trunkHeight - height) / tree.trunkHeight, tree.type.leafTaper, 1);
		double radius = MathHelper.clamp(tree.type.leafRad * taper, 1, tree.type.leafRad);
		double ymin = tree.type.leafYMin;
		double ymax = tree.type.leafYMax;

		for (double yloop = ymin; yloop < ymax; yloop++) {
			double sliceRadSq = (radius + 1) * (radius + 1) - (yloop * yloop);
			double slicedRadSqSmall = radius * radius - (yloop * yloop);

			if (sliceRadSq > 0) {
				for (double xloop = -radius; xloop < radius + 1; xloop++) {
					for (double zloop = -radius; zloop < radius + 1; zloop++) {
						double xzDistanceSq = xloop * xloop + zloop * zloop;

						BlockPos leafPos = new BlockPos(xloop + pos.getX(), yloop + pos.getY(), zloop + pos.getZ());

						if (xzDistanceSq < slicedRadSqSmall)
							tree.setLeaf(leafPos);
						else if (xzDistanceSq < sliceRadSq) {
							if (tree.random.nextBoolean()) {
								tree.setLeaf(leafPos);

								if (tree.type.vines > 0 && MathHelper.absMax(xloop, zloop) > yloop && tree.random.nextBoolean())
									TreeGenMethods.genVine(tree, leafPos, xloop, zloop);
							}
						}
					}
				}
			}
		}
	}
}
