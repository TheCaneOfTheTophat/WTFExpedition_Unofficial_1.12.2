package wtf.worldgen.caves.types.nether;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.caves.AbstractCaveType;

public class NetherFrozen extends AbstractCaveType {

	//Ash instead of snow
	//Tar, instead of lava
	
	public NetherFrozen() {
		super("NetherFrozen", 0, 0);
	}

	@Override
	public void generateCeiling(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateCeilingAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateWall(GeneratorMethods gen, Random random, BlockPos pos, float depth, int height) {}

}
