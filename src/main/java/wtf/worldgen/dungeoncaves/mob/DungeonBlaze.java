package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.DungeonAbstractSimple;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonBlaze extends DungeonAbstractSimple {

	public DungeonBlaze(String name) {
		super(name);
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("blaze"), 2);
	}
	
	IBlockState block = null;
	
	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		block = Blocks.NETHER_BRICK.getDefaultState();
		return true;
	}

	public void generate(World world, BlockPos pos) {
			replace(world, pos, block);
	}

	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
		int x = pos.getX() % 10;
		int z = pos.getZ() % 10;

		if (x == 1 || x == 5 || x == 8 || z == 1 || z == 5 || z == 8)
			replace(world, pos.down(), Blocks.NETHER_BRICK_FENCE.getDefaultState());
	}
}
