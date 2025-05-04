package wtf.worldgen.caves.types;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeFungal extends AbstractCaveType {

	public CaveTypeFungal(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(world, pos, 0.33) < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (world.getBlockState(pos) == Blocks.DIRT.getDefaultState() || simplex.get3DNoise(world, pos.getX() / 3D, pos.getY(), pos.getZ() / 3D) < 0.33)
			replace(world, pos, Blocks.MYCELIUM.getDefaultState());
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextBoolean())
			if (pos.getY() < 53)
				replace(world, pos, Blocks.BROWN_MUSHROOM.getDefaultState());
			else
				replace(world, pos, Blocks.RED_MUSHROOM.getDefaultState());
		else
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (simplex.get3DNoiseScaled(world, pos, 0.33) < 0.33)
			replace(world, pos, Blocks.DIRT.getDefaultState());
	}
}
