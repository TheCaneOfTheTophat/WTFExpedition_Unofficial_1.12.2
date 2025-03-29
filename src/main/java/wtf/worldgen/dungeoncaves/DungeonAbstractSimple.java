package wtf.worldgen.dungeoncaves;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.CaveListWrapper;

import java.util.Random;

public abstract class DungeonAbstractSimple extends AbstractDungeonType {

	public DungeonAbstractSimple(String name) {
		super(name, 0, 0);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return true;
	}

	public abstract void generate(World world, BlockPos pos);
	
	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		generate(world, pos);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		generate(world, pos);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		//no ceiling addons
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		//no floor addons
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		generate(world, pos);
	}
}
