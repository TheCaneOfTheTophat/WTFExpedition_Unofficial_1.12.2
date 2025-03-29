package wtf.worldgen.caves.types;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeDirtWater extends AbstractCaveType {

	public CaveTypeDirtWater(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(world,pos, 0.2) < 0.33 )
			setWaterPatch(world, pos);
		else if (simplex.get3DNoiseScaled(world, pos, 0.2) > 0.66)
			setPatch(world, pos, WTFContent.dirt_patch.getDefaultState());
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (height < 3 && simplex.get3DNoiseScaled(world, pos, 0.2) > 0.66)
			replace(world, pos.up(), Blocks.DIRT.getDefaultState());
	}
}
