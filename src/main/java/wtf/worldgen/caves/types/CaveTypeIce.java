package wtf.worldgen.caves.types;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeIce extends AbstractCaveType {

	public CaveTypeIce(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(world,pos, 0.2) < 0.5) {
			//in shallow caves, depth = 0 to 0.33
			if (simplex.get3DNoiseScaled(world, pos, 0.1) / 3 > depth)
				setPatch(world, pos, WTFContent.ice_patch.getDefaultState());
			else
				setPatch(world, pos, Blocks.SNOW_LAYER.getDefaultState());
		}
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genIcicle(world, pos);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}
