package wtf.worldgen.caves.types;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.enums.Modifier;
import wtf.init.WTFContent;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.caves.AbstractCaveType;

public class CaveTypeMossy extends AbstractCaveType {

	public CaveTypeMossy(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		boolean mossy = simplex.get3DNoise(gen.getWorld(), pos) > 0.5;

		if (mossy)
			gen.transformBlock(pos, Modifier.MOSS);

	}

	@Override
	public void generateFloor(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(gen.getWorld(), pos, 0.1);
		boolean mossy = simplex.get3DNoise(gen.getWorld(), pos) > 0.5;
		
		if (noise < 0.33) {
			if (mossy)
				gen.setPatch(pos, WTFContent.mossy_dirt_patch.getDefaultState());
			else
				gen.setPatch(pos, WTFContent.dirt_patch.getDefaultState());
		}
		
		if (mossy)
			gen.transformBlock(pos, Modifier.MOSS);
	}

	@Override
	public void generateCeilingAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		gen.genSpeleothem(pos, getSpelSize(random, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		gen.genSpeleothem(pos, getSpelSize(random, depth), depth, false);
	}

	@Override
	public void generateWall(GeneratorMethods gen, Random random, BlockPos pos, float depth, int height) {
		double noise = simplex.get3DNoiseScaled(gen.getWorld(), pos, 0.1);
		boolean mossy = simplex.get3DNoise(gen.getWorld(), pos) > 0.5;

		if (height < 3 && noise < 0.33) {
			gen.replaceBlock(pos, Blocks.DIRT.getDefaultState());

			if (mossy)
				gen.transformBlock(pos, Modifier.MOSS);
		}

		if (mossy)
			gen.transformBlock(pos, Modifier.MOSS);
	}
}




