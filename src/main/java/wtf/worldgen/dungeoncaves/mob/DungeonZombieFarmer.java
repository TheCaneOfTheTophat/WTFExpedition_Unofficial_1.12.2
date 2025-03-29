package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.blocks.BlockFoxfire;
import wtf.init.WTFContent;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonZombieFarmer extends AbstractDungeonType {

	public DungeonZombieFarmer(String name) {
		super(name, 5, 15);
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
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.MYCELIUM.getDefaultState());
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID,"zombie_farmer"), 2);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return false;
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.BROWN_MUSHROOM.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}
}
