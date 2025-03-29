package wtf.worldgen.caves.types.nether;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

public class NetherNormal extends AbstractCaveType {

	public NetherNormal() {
		super("NetherNormal", 0, 0);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}
