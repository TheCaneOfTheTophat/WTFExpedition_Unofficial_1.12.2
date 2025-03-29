package wtf.worldgen.caves.types;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeRocky extends AbstractCaveType {

	public CaveTypeRocky(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos);

		if (noise < 0.05)
			modify(world, pos, Modifier.CRACKED);

		else if (noise > 0.75)
			modify(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos);

		if (noise < 0.05)
			modify(world, pos, Modifier.CRACKED);

		else if (noise > 0.75)
			modify(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextBoolean())
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
		else
			setFloorAddon(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		double noise = simplex.get3DNoise(world, pos);

		if (noise < 0.05)
			modify(world, pos, Modifier.CRACKED);
		else if (noise > 0.75)
			modify(world, pos, Modifier.FRACTURED);
	}
}