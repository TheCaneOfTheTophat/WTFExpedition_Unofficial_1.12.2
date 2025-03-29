package wtf.worldgen.caves.types.nether;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class NetherSoulDesert extends AbstractCaveType{

	//Soulsand, mixed with something else to make it traversable 
	//Stone or bone cacti
	
	public NetherSoulDesert() {
		super("NetherSoulDesert", 0, 1);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		//Some sort of use of magma blocks, with obsidian stalactites?
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoise(world, pos) < 0.25)
			replace(world, pos, Blocks.SOUL_SAND.getDefaultState());

		modify(world, pos, Modifier.FRACTURED);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		for (int i = rand.nextInt(3); i > -1; i--)
			replace(world, pos.up(i), WTFContent.red_cactus.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}
