package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.blocks.BlockFoxfire;
import wtf.init.WTFContent;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeFoxfire extends AbstractDungeonType {

	public DungeonTypeFoxfire(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return isHeight(cave, 4);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos.up(2), Blocks.LOG.getDefaultState());
		replace(world, pos.up(), Blocks.LOG.getDefaultState());
		replace(world, pos, Blocks.LOG.getDefaultState());
		replace(world, pos.down(), Blocks.LOG.getDefaultState());
		replace(world, pos.down(2), WTFContent.foxfire.getDefaultState().withProperty(BlockFoxfire.HANGING, true));
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos.down(2), Blocks.LOG.getDefaultState());
		replace(world, pos.down(), Blocks.LOG.getDefaultState());
		replace(world, pos, Blocks.LOG.getDefaultState());
		replace(world, pos.up(), Blocks.LOG.getDefaultState());
		replace(world, pos.up(2), WTFContent.foxfire.getDefaultState());
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}