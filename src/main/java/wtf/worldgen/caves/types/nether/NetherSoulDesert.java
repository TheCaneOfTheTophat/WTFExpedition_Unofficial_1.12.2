package wtf.worldgen.caves.types.nether;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.enums.Modifier;
import wtf.init.WTFContent;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.caves.AbstractCaveType;

public class NetherSoulDesert extends AbstractCaveType{

	//Soulsand, mixed with something else to make it traversable 
	//Stone or bone cacti
	
	public NetherSoulDesert() {
		super("NetherSoulDesert", 0, 1);
	}

	@Override
	public void generateCeiling(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		//Some sort of use of magma blocks, with obsidian stalactites?
	}

	@Override
	public void generateFloor(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		if (simplex.get3DNoise(gen.getWorld(), pos) < 0.25)
			gen.replaceBlock(pos, Blocks.SOUL_SAND.getDefaultState());

		gen.transformBlock(pos, Modifier.FRACTURED);
	}

	@Override
	public void generateCeilingAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		for (int rand = random.nextInt(3); rand > -1; rand--)
			gen.replaceBlock(pos.up(rand), WTFContent.red_cactus.getDefaultState());
	}

	@Override
	public void generateWall(GeneratorMethods gen, Random random, BlockPos pos, float depth, int height) {}

}
