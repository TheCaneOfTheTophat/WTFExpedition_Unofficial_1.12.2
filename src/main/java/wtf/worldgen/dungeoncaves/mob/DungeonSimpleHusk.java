package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.BlockSandStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.DungeonAbstractSimple;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonSimpleHusk extends DungeonAbstractSimple {

	public DungeonSimpleHusk(String name) {
		super(name);
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("husk"), 5);
	}

	IBlockState sandstone = Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH);
	
	public void generate(World world, BlockPos pos) {
		replace(world, pos, sandstone);
	}
}
