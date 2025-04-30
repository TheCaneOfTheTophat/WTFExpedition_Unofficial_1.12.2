package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.BlockSets;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.DungeonAbstractSimple;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonSimpleSkeleton extends DungeonAbstractSimple {

	public DungeonSimpleSkeleton(String name) {
		super(name);
	}

	IBlockState block = null;
	
	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		block = BlockSets.getTransformedState(getBlockStateCompatible(world, cave.centerPos.getFloorPos()), Modifier.BRICK);

		if (block == null)
			block = Blocks.STONEBRICK.getDefaultState();
		
		return true;
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("skeleton"), 5);
	}

	public void generate(World world, BlockPos pos) {
			replace(world, pos, block);
	}
}
