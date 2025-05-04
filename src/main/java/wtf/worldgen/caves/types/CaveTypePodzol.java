package wtf.worldgen.caves.types;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.enums.Modifier;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypePodzol extends AbstractCaveType {

	IBlockState podzol = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);

	public CaveTypePodzol(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoise(world, pos) > 0.66)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if(world.getBlockState(pos) == Blocks.DIRT.getDefaultState())
			replace(world, pos, podzol, false);
		else if (simplex.get3DNoiseScaled(world, pos, 0.2) < 0.33)
			setPatch(world, pos, WTFContent.podzol_patch.getDefaultState());
		else if (simplex.get3DNoise(world, pos) > 0.66)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		Block mushroom = simplex.get3DNoiseScaled(world, pos, 0.2) > 0.5 ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM;
		replace(world, pos, mushroom.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (height < 3 && simplex.get3DNoiseScaled(world, pos, 0.2) < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());
		if (simplex.get3DNoise(world, pos) > 0.66)
			modify(world, pos, Modifier.MOSS);
	}
}
