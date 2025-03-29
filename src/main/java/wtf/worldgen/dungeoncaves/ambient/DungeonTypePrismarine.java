package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypePrismarine extends AbstractDungeonType {

	public DungeonTypePrismarine(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return cave.getAvgFloor() < 48;
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.PRISMARINE.getDefaultState());
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.PRISMARINE.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (height == 3)
			replace(world, pos, Blocks.SEA_LANTERN.getDefaultState());
		else
			replace(world, pos, Blocks.PRISMARINE.getDefaultState());
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

}
