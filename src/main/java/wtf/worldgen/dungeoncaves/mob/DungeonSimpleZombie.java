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

public class DungeonSimpleZombie extends DungeonAbstractSimple {

	public DungeonSimpleZombie(String name) {
		super(name);
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("zombie"), 5);
	}
	
	IBlockState block = null;
	
	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		IBlockState temp = BlockSets.getTransformedState(world.getBlockState(cave.centerpos.getFloorPos()), Modifier.FRACTURED);
		block = BlockSets.getTransformedState(temp, Modifier.MOSS);
		
		if (block == null)
			block = Blocks.MOSSY_COBBLESTONE.getDefaultState();

		return true;
	}

	public void generate(World world, BlockPos pos) {
		replace(world, pos, block);
	}
}
