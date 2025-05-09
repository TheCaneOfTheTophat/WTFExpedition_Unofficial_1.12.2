package wtf.worldgen.trees;

import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.init.BlockSets;
import wtf.worldgen.GenMethods;
import wtf.worldgen.trees.components.Branch;
import wtf.worldgen.trees.components.ColumnTrunk;
import wtf.worldgen.trees.components.Root;
import wtf.worldgen.trees.types.AbstractTreeType;

public class TreeGenMethods {

	private static final float PIx2 = (float) Math.PI * 2;
	private static final float PId4 = (float) Math.PI / 4;

	public static void tryGenerate(TreeInstance tree) {
		if (genTrunk(tree)) {
			float offset = genTop(tree, tree.random.nextFloat() * PIx2);
			doBranches(tree, offset);
			genMainRoots(tree);
			tree.setBlocksForPlacement(tree.world);
		}
	}
	
	protected static boolean genTrunk(TreeInstance tree) {
		int startloop = tree.trunkDiameter > 1 ? MathHelper.floor(-(tree.trunkRadius)) : 0;
		int finishLoop = MathHelper.ceil(tree.trunkRadius);
		
		for (int loopX =startloop; loopX < finishLoop; loopX++) {
			for (int loopZ = startloop; loopZ  < finishLoop; loopZ++) {
				double currentRadius = Math.sqrt(loopX*loopX + loopZ*loopZ);

				if (tree.trunkRadius <= 1 || currentRadius <= tree.trunkRadius) {
					double startHeight = tree.y+(tree.type.getTrunkColumnHeight(tree.trunkHeight, currentRadius, tree.trunkRadius));
					ColumnTrunk column = new ColumnTrunk(tree.oriX+loopX, tree.oriZ+loopZ, startHeight);

					BlockPos pos = column.currentPos();
					
					while (GenMethods.isNonSolid(tree.world.getBlockState(pos)) && pos.getY() > tree.y - 4){
						if (!tree.type.airGenerate || pos.getY()-tree.pos.getY() > tree.type.airGenHeight)
							tree.setTrunk(pos);
						
						pos = column.nextPos();
					}
					
					if (Math.abs(column.currentY-tree.y) > 2) {
						//if y is too high, it hit a block on the way down
						//if y is too low, the ground it really uneven
						return false;
					}
					
					IBlockState surface = tree.world.getBlockState(pos);
					
					if (tree.type.waterGenerate < 1 && tree.world.getBlockState(pos.up()).getMaterial().isLiquid())
						return false;

					if (!tree.type.canGrowOn.contains(surface.getBlock()))
						return false;

				}
			}	
		}

		return true;
	}

	public static float genTop(TreeInstance tree, float offset) {
		//Spruce cone top
		if (tree.type.leaftype == AbstractTreeType.LeafStyle.SPRUCE) {
			//starting at tree height - branch seperation
			double nodeHeight = tree.trunkHeight-1;

			//looping for multiple branches per node
			int crownLoops = 4;//(int) MathHelper.clamp(tree.trunkHeight/7, 3, 12);
			
			while (nodeHeight < tree.trunkHeight+crownLoops) {
				int branches = tree.type.getBranchesPerNode(0.1, tree.scale);
				
				for (int loopMultiBranches = 0; loopMultiBranches < branches; loopMultiBranches++) {
					float offSetForBranch = offset + PIx2/branches*loopMultiBranches; 

					float vecX = MathHelper.cos(offSetForBranch);
					float vecZ = MathHelper.sin(offSetForBranch); 

					Branch branch = new Branch(tree, tree.oriX, nodeHeight + tree.y, tree.oriZ, vecX, tree.type.getBranchPitch(tree.scale), vecZ, (tree.type.getBranchLength(tree.scale, tree.trunkHeight, nodeHeight)));
					genBranch(tree, branch);

					offset+=tree.type.getBranchRotation(tree.scale, branches);
				}
				//end branch loop
				offset += PId4;
				nodeHeight+=1;
			}
			//end branch node loop
		} else {
			for (double topLoop = tree.type.topLimitUp; topLoop < tree.type.topLimitDown; topLoop+=tree.type.topLimitIncrement) {	//Top Loop
				int branches =  tree.type.getBranchesPerNode(0.1, tree.scale);
				offset += tree.type.getBranchRotation(tree.scale, branches);

				for (int loopMultiBranches = 0; loopMultiBranches < branches; loopMultiBranches++) {
					double offSetForBranch = offset + PIx2/branches*loopMultiBranches; //

					double sinTop = Math.sin(topLoop);
					double vecY = Math.cos(topLoop);
					double vecX = Math.cos(offSetForBranch) * sinTop;
					double vecZ = Math.sin(offSetForBranch) * sinTop;

					//full taper here, because it's the very top

					Branch branch = new Branch(tree, tree.oriX, tree.trunkHeight-1 + tree.y, tree.oriZ, vecX, vecY, vecZ, tree.type.getBranchLength(tree.scale, tree.trunkHeight, tree.trunkHeight));

					genBranch(tree, branch);
					offset += tree.type.getBranchRotation(tree.scale, branches);
				}
			}
		}

		return offset;
	}

	public static void doBranches(TreeInstance tree, float offset) {
		//starting at tree height - branch seperation
		double nodeHeight = tree.trunkHeight-tree.type.getBranchSeperation(tree.scale);

		//looping for multiple branches per node
		int lowestBranch = MathHelper.floor(tree.trunkHeight*tree.type.getLowestBranchRatio());
		while (nodeHeight > lowestBranch) {
			double bottom = tree.type.getLowestBranchRatio()*tree.trunkHeight;
			double distFromBottom = nodeHeight - bottom;
			double branchSectionLength = tree.trunkHeight-bottom;
			double taper = 1 - MathHelper.clamp(distFromBottom/branchSectionLength, 0.1, 1);
			
			int branches = tree.type.getBranchesPerNode(taper, tree.scale);
			
			for (int loopMultiBranches = 0; loopMultiBranches < branches; loopMultiBranches++){
				float offSetForBranch = offset + PIx2/branches*loopMultiBranches; 

				float vecX = MathHelper.cos(offSetForBranch);
				float vecZ = MathHelper.sin(offSetForBranch);
			
				Branch branch = new Branch(tree, tree.oriX, nodeHeight + tree.y, tree.oriZ, vecX, tree.type.getBranchPitch(tree.scale), vecZ, (tree.type.getBranchLength(tree.scale, tree.trunkHeight, nodeHeight)));
				genBranch(tree, branch);

				offset+=tree.type.getBranchRotation(tree.scale, branches);
			}
			//end branch loop
			offset += PId4;
			nodeHeight-=tree.type.getBranchSeperation(tree.scale);
		}
		//end branch node loop
	}
	
	public static void genMainRoots(TreeInstance tree) {
		double offset = tree.random.nextDouble() * PIx2;

		for (double angleDown = tree.type.rootInitialAngle; angleDown > tree.type.rootFinalAngle; angleDown-=tree.type.rootIncrementAngle) {	//Top Loop
			int numRoots = tree.type.getNumRoots(tree.trunkDiameter);
			//doo a loop manually, witha while loop for only x and z

			for (int loopMultiBranches = 0; loopMultiBranches < numRoots; loopMultiBranches++) {
				double offSetForBranch = offset + Math.PI/numRoots*loopMultiBranches;
				double vecY = Math.cos(angleDown);
				double sinY = Math.sin(angleDown);
				double vecX = Math.cos(offSetForBranch) * sinY;
				double vecZ = Math.sin(offSetForBranch) * sinY;
				Root root = new Root(tree, tree.oriX, tree.y + tree.rootLevel + tree.type.airGenHeight, tree.oriZ, vecX, vecY, vecZ, tree.type.getRootLength(tree.trunkHeight));

				while (tree.inTrunk(root.lateralNext())) {
					//just incrementing out with lateralNext(), no need to do anything here
				}

				while (root.hasNext()) { // need to include some sort of check - 50% chance that adjacent stone fractures
					BlockPos pos = root.pos();

					tree.setRoot(pos);
					if (tree.type.rootWall)
						for (int loop = 1; GenMethods.isNonSolid(tree.world.getBlockState(pos.down(loop))) && loop > tree.type.airGenHeight + 1; loop++)
							tree.setRoot(pos.down(loop));

					if (tree.random.nextFloat() < tree.type.rootDecoRate) {
						if (tree.random.nextBoolean() && tree.type.decoDown != null) {
							BlockPos decoPos = pos.down();
							if (tree.world.isAirBlock(decoPos))
								tree.setDeco(decoPos, tree.type.decoDown);
						} else if(tree.type.decoUp != null) {
							BlockPos decoPos = pos.up();
							if (tree.world.isAirBlock(decoPos))
								tree.setDeco(decoPos, tree.type.decoUp);
						}
					}

					if (canReplaceRoot(tree, pos.down()))
						root.growDown(tree);
					else if (!BlockSets.groundMaterial.contains(tree.world.getBlockState(pos.down()).getMaterial()))
						root.growLateral();
					else
						root.next();
				}

				offset+=PId4; //rotate 45 degrees
			}
		}
	}

	protected static boolean genBranch(TreeInstance tree, Branch branch) {
		while (tree.inTrunk(branch.pos()))
			branch.next();

		if (tree.type.cocoa && tree.random.nextFloat() < 0.3)
			genCocoa(tree, branch);

		BlockPos pos = branch.pos();

		while (branch.hasNext()) {
			if (!canReplace(tree, pos))
				return false;
			
			switch (tree.type.leaftype) {
				case BASIC:
					tree.setBranch(pos, branch.axis);
					break;
				case SPRUCE:
					tree.type.doLeafNode(tree, branch, pos);
					break;
				default:
					break;
			}

			pos = branch.next();
		}

		if (!canReplace(tree, pos))
			return false;

		switch (tree.type.leaftype) {
			case BASIC:
				tree.type.doLeafNode(tree, branch, branch.pos());
				break;
			case SPRUCE:
				//pos = branch.next();
				//tree.setLeaf(pos);
				break;
			default:
				break;
		}

		return true;
	}


	public static void genVine(TreeInstance tree, BlockPos pos, double xloop, double zloop) {
		IBlockState block;

		if (Math.abs(xloop) > Math.abs(zloop)) {
			if (xloop > 0) {
				block = Blocks.VINE.getDefaultState().withProperty(BlockVine.WEST,  true);
				pos = pos.east();
			} else {
				block = Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST,  true);
				pos = pos.west();
			}
		} else {
			if (zloop > 0) {
				block = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH,  true);
				pos = pos.south();
			} else {
				block = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH,  true);
				pos = pos.north();
			}
		}

		for (int loop = tree.random.nextInt(tree.type.vines) + 1; loop > -1; loop--) {
			if (tree.world.isAirBlock(pos.down(loop)))
				tree.setDeco(pos.down(loop), block);
			else
				break;
		}
	}

	protected static void genCocoa(TreeInstance tree, Branch branch) {
		BlockPos pos = branch.pos().down();
		if (Math.abs(branch.vecX) > Math.abs(branch.vecZ)) {
			if (branch.vecX > 0) {//East
				if (tree.inTrunk(pos.west())) {
					//System.out.println("west");
					tree.setDeco(pos, Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.WEST));
				}
			} else { //West
				if (tree.inTrunk(pos.east())) {
					//System.out.println("east");
					tree.setDeco(pos, Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.EAST));
				}
			}
		} else {
			if (branch.vecZ > 0){ //South
				if (tree.inTrunk(pos.south())) {
					//System.out.println("south");
					tree.setDeco(pos, Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH));
				}
			} else {  //North
				if (tree.inTrunk(pos.north())) {
					//System.out.println("north");
					tree.setDeco(pos, Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
				}
			}
		}
	}

	protected static boolean canReplace(TreeInstance tree, BlockPos pos) {
		IBlockState state = tree.world.getBlockState(pos);

		if (GenMethods.isTreeReplaceable(tree.world, pos, tree.world.getBlockState(pos)))
			return true;

		if (tree.type.growDense)
            return state.getMaterial() == Material.LEAVES;

		return false;
	}

	protected static HashSet<Material> rootReplaceable = new HashSet<>(Arrays.asList(Material.AIR, Material.CACTUS, Material.GOURD, Material.GRASS, Material.LEAVES, Material.VINE, Material.PLANTS, Material.ICE, Material.PACKED_ICE, Material.GROUND, Material.SAND, Material.WATER, Material.SNOW));

	protected static boolean canReplaceRoot(TreeInstance tree, BlockPos pos) {
		return rootReplaceable.contains(tree.world.getBlockState(pos).getMaterial());
	}
}
