package wtf.worldgen.caves.types;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.init.WTFContent;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.caves.AbstractCaveType;

public class CaveTypeDirtWater extends AbstractCaveType {

	public CaveTypeDirtWater(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(gen.getWorld(),pos, 0.2) < 0.33 )
			gen.setWaterPatch(pos);
		else if (simplex.get3DNoiseScaled(gen.getWorld(), pos, 0.2) > 0.66)
			gen.setPatch(pos, WTFContent.dirt_patch.getDefaultState());
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
		if (height < 3 && simplex.get3DNoiseScaled(gen.getWorld(), pos, 0.2) > 0.66)
			gen.replaceBlock(pos.up(), Blocks.DIRT.getDefaultState());
	}
}
