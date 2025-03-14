package wtf.worldgen.dungeoncaves.mob;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import wtf.init.BlockSets;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.dungeoncaves.DungeonAbstractSimple;

public class DungeonSimpleZombie extends DungeonAbstractSimple {

	public DungeonSimpleZombie(String name) {
		super(name);
	}

	@Override
	public void generateCenter(GeneratorMethods gen, Random rand, CavePosition pos, float depth) {
		gen.spawnVanillaSpawner(pos.getFloorPos().up(), new ResourceLocation("zombie"), 5);
	}
	
	IBlockState block = null;
	
	@Override
	public boolean canGenerateAt(GeneratorMethods gen, CaveListWrapper cave) {
		IBlockState temp = BlockSets.getTransformedState(gen.getWorld().getBlockState(cave.centerpos.getFloorPos()), Modifier.FRACTURED);
		block = BlockSets.getTransformedState(temp, Modifier.MOSS);
		
		if (block==null)
			block = Blocks.MOSSY_COBBLESTONE.getDefaultState();

		return true;
	}

	public void generate(GeneratorMethods gen, Random random, BlockPos pos) {
		gen.replaceBlock(pos, block);
	}
}
