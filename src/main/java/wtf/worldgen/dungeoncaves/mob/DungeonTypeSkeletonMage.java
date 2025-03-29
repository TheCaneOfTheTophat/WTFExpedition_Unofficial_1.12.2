package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.utilities.wrappers.CavePosition;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeSkeletonMage extends DungeonSimpleSkeleton {

	public DungeonTypeSkeletonMage() {
		super("SkeletonMage");
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		replace(world, pos.getFloorPos().up(2), Blocks.ENCHANTING_TABLE.getDefaultState());
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID,"skeleton_mage"), 2);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (height == 3)
			replace(world, pos, Blocks.BOOKSHELF.getDefaultState());
		else
			super.generateWall(world, rand, pos, depth, height);
	}
}
