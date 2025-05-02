package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.blocks.BlockDecoAnim;
import wtf.init.BlockSets;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.DungeonAbstractSimple;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonSimpleMagma extends DungeonAbstractSimple {

	public DungeonSimpleMagma(String name) {
		super(name);
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("magma_cube"), 3);
	}
	
	IBlockState block = null;
	
	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		block = BlockSets.getTransformedState(getBlockStateCompatible(world, cave.centerPos.getFloorPos()), Modifier.LAVA_CRUST);

		if (block == null)
			block = BlockSets.getTransformedState(Blocks.OBSIDIAN.getDefaultState(), Modifier.LAVA_CRUST);

		return true;
	}

	@Override
	public void generate(World world, BlockPos pos) {
		if (simplex.get3DNoise(world, pos) < 0.8)
			replace(world, pos, block.withProperty(BlockDecoAnim.FAST, true));
	}
}