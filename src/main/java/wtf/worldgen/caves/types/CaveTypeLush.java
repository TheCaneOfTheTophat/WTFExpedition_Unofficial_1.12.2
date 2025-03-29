package wtf.worldgen.caves.types;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.blocks.BlockRoots;
import wtf.init.WTFContent;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.AdjPos;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeLush extends AbstractCaveType {

	public CaveTypeLush(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}
	
	final IBlockState leaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.DECAYABLE, Boolean.FALSE);
	final IBlockState roots = WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.JUNGLE);
	final IBlockState fern = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN);
	final IBlockState sapling = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE);

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;
		
		if (noise < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());
		if (mossy)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		boolean mossy =  simplex.get3DNoise(world, pos) > 0.5;
		
		if (noise < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());

		if (simplex.get3DNoiseShifted(world, pos, -100) > 0.66)
			if (mossy)
				setPatch(world, pos, WTFContent.mossy_dirt_patch.getDefaultState());
			else
				setPatch(world, pos, WTFContent.dirt_patch.getDefaultState());
		
		if (simplex.get3DNoise(world, pos) > 0.75)
			modify(world, pos, Modifier.FRACTURED);

		if (mossy)
			modify(world, pos, Modifier.MOSS);
		
	}


	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos.up(), 0.1);

		if (noise < 0.33)
			replace(world, pos, roots);
		else {
			setCeilingAddon(world, pos, Modifier.FRACTURED);

			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3) + 1; loop > -1; loop--)
					genVines(world, pos.east().down(loop), EnumFacing.WEST);

			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3) + 1; loop > -1; loop--)
					genVines(world, pos.west().down(loop), EnumFacing.EAST);


			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3) + 1; loop > -1; loop--)
					genVines(world, pos.north().down(loop), EnumFacing.SOUTH);


			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3) + 1; loop > -1; loop--)
					genVines(world, pos.south().down(loop), EnumFacing.NORTH);
		}
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos.down(), 0.1);

		if (noise < 0.165)
			replace(world, pos, fern);

		else if (noise < 0.33)
			replace(world, pos, sapling);

		else if (rand.nextBoolean()) {
			replace(world, pos, leaves);
			replace(world, pos.north(), leaves);
			replace(world, pos.south(), leaves);
			replace(world, pos.east(), leaves);
			replace(world, pos.west(), leaves);
			replace(world, pos.up(), leaves);
		}
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;
		
		if (noise < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());
		
		if (simplex.get3DNoise(world, pos) > 0.75)
			modify(world, pos, Modifier.FRACTURED);

		if (mossy)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height){
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;

		if (mossy)
			genVines(world, pos, pos.getFace(rand));
	}
}
