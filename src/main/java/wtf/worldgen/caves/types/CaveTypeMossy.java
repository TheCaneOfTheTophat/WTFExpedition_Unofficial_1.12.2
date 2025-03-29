package wtf.worldgen.caves.types;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeMossy extends AbstractCaveType {

	public CaveTypeMossy(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;

		if (mossy)
			modify(world, pos, Modifier.MOSS);

	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;
		
		if (noise < 0.33) {
			if (mossy)
				setPatch(world, pos, WTFContent.mossy_dirt_patch.getDefaultState());
			else
				setPatch(world, pos, WTFContent.dirt_patch.getDefaultState());
		}
		
		if (mossy)
			modify(world, pos, Modifier.MOSS);
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
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		boolean mossy = simplex.get3DNoise(world, pos) > 0.5;

		if (height < 3 && noise < 0.33) {
			replace(world, pos, Blocks.DIRT.getDefaultState());

			if (mossy)
				modify(world, pos, Modifier.MOSS);
		}

		if (mossy)
			modify(world, pos, Modifier.MOSS);
	}
}




