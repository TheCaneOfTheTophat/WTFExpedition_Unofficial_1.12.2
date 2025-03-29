package wtf.worldgen.caves.types;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.worldgen.caves.AbstractCaveType;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeVolcanic extends AbstractCaveType {

	public CaveTypeVolcanic(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos);

		 if (noise < 0.2)
			modify(world, pos, Modifier.LAVA_CRUST);
		else if (noise < 0.4)
			modify(world, pos, Modifier.FRACTURED);

	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos);

		if (noise < 0.2)
			modify(world, pos, Modifier.LAVA_CRUST);
		else if (noise < 0.4)
				modify(world, pos, Modifier.FRACTURED);

	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos.up());

		if (noise < 0.2)
			 setCeilingAddon(world, pos, Modifier.FRACTURED);
		else
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);

		setCeilingAddon(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoise(world, pos.down());

		if (noise < 0.2)
			 setFloorAddon(world, pos, Modifier.FRACTURED);
		else
			genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);

		setFloorAddon(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		double noise = simplex.get3DNoise(world, pos);

		if (noise < 0.2)
			modify(world, pos, Modifier.LAVA_CRUST);
		else if (noise < 0.4)
				modify(world, pos, Modifier.FRACTURED);

	}
}
