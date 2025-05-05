package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeSoulsand extends AbstractDungeonType {

	public DungeonTypeSoulsand(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return true;
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		if(simplex.get3DNoise(world, pos) > 0.5)
			modify(world, pos, Modifier.SOUL);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.SOUL_SAND.getDefaultState());}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if(simplex.get3DNoise(world, pos) > 0.5)
			modify(world, pos, Modifier.SOUL);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.NETHER_WART.getDefaultState());
	}
}
