package wtf.worldgen.caves.types;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeIceRocky extends CaveTypeRocky {

	public CaveTypeIceRocky(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		super.generateFloor(world, rand, pos, depth);

		if (simplex.get3DNoiseScaled(world, pos,0.2) < 0.5)
			setPatch(world, pos, WTFContent.ice_patch.getDefaultState());
	}
}
