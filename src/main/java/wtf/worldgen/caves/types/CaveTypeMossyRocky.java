package wtf.worldgen.caves.types;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeMossyRocky extends CaveTypeRocky {

	public CaveTypeMossyRocky(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		super.generateCeiling(world, rand, pos, depth);

		if (simplex.get3DNoiseShifted(world, pos, 100) > 0.80)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		super.generateFloor(world, rand, pos, depth);

		if (simplex.get3DNoiseShifted(world, pos, 100) > 0.80)
			modify(world, pos, Modifier.MOSS);
	}


	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		super.generateWall(world, rand, pos, depth, height);

		if (simplex.get3DNoiseShifted(world, pos, 100) > 0.80)
			modify(world, pos, Modifier.MOSS);
	}
}
