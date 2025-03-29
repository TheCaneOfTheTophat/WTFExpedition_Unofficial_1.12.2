package wtf.worldgen.caves.types;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.AdjPos;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeSwamp extends AbstractCaveType {

	public CaveTypeSwamp(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextFloat() < 0.15)
			modify(world, pos, Modifier.WET);
		else {
			if (simplex.get3DNoise(world, pos) < 0.1)
				modify(world, pos, Modifier.FRACTURED);
			if (simplex.get3DNoise(world, pos) - 0.5 > depth)
				modify(world, pos, Modifier.MOSS);
		}
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(world, pos, 0.5) < 0.2)
			setWaterPatch(world, pos);

		if (simplex.get3DNoise(world, pos) < 0.1)
			modify(world, pos, Modifier.FRACTURED);

		if (simplex.get3DNoise(world, pos)- 0.5 > depth)
			modify(world, pos, Modifier.MOSS);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextBoolean() && setCeilingAddon(world, pos, Modifier.FRACTURED)) {
			for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
				genVines(world, pos.east().down(loop), EnumFacing.WEST);
			for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
				genVines(world, pos.west().down(loop), EnumFacing.EAST);
			for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
				genVines(world, pos.north().down(loop), EnumFacing.SOUTH);
			for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
				genVines(world, pos.south().down(loop), EnumFacing.NORTH);
		} else
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextBoolean() && setFloorAddon(world, pos, Modifier.FRACTURED)) {
			genVines(world, pos.east(), EnumFacing.WEST);
			genVines(world, pos.west(), EnumFacing.EAST);
			genVines(world, pos.north(), EnumFacing.SOUTH);
			genVines(world, pos.south(), EnumFacing.NORTH);
		} else
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}


	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (simplex.get3DNoiseScaled(world, pos, 0.5) < 0.2)
			setWaterPatch(world, pos);

		if (simplex.get3DNoise(world, pos) < 0.1)
			modify(world, pos, Modifier.FRACTURED);

		if (simplex.get3DNoise(world, pos) - 0.5 > depth)
			modify(world, pos, Modifier.MOSS);

	}

	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
		if (simplex.get3DNoise(world, pos) > depth * 2)
			genVines(world, pos, pos.getFace(rand));
	}
}
