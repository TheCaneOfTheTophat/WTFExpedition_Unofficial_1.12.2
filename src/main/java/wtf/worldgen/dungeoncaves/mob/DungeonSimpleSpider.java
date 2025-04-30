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

public class DungeonSimpleSpider extends DungeonAbstractSimple {

	public DungeonSimpleSpider(String name) {
		super(name);
		this.genAir = true;
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		IBlockState temp = BlockSets.getTransformedState(getBlockStateCompatible(world, cave.centerPos.getFloorPos()), Modifier.FRACTURED);
		block = BlockSets.getTransformedState(temp, Modifier.MOSS);
		
		if (block == null)
			block = Blocks.MOSSY_COBBLESTONE.getDefaultState();

		return isHeight(cave, 4);
	}

	IBlockState block = null;
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		BlockPos midpos = pos.getCeilingPos().down();

		while (midpos.getY() > pos.floor) {
			replace(world, midpos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
			midpos = midpos.down();
		}

		spawnVanillaSpawner(world, midpos, new ResourceLocation("spider"), 3);
	}

	@Override
	public void generateAir(World world, Random rand, BlockPos floating, float depth) {
		double noise = simplex.get3DNoise(world, floating.getX(), floating.getY(), floating.getZ());

		if (noise < 0.2)
			replace(world, floating, Blocks.WEB.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos) {
		replace(world, pos, block);
	}
}

