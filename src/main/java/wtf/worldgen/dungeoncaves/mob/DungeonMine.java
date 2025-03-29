package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonMine extends AbstractDungeonType {

	public DungeonMine(String name) {
		super(name, 0, 0);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return this.isHeight(cave, 5);
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID, "zombie_miner"), 5);
		replace(world, pos.getFloorPos().up(2), Blocks.TNT.getDefaultState());
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		int x = pos.getX() % 10;
		int z = pos.getZ() % 10;
		
		if (x == 1 || x == 5 || x == 8 || z == 1 || z == 5 || z == 8)
			replace(world, pos.down(), Blocks.SPRUCE_FENCE.getDefaultState());
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
		int x = pos.getX() % 10;
		int z = pos.getZ() % 10;
		if (x == 1 || x == 5 || x == 8 || z == 1 || z == 5 || z == 8)
			replace(world, pos.down(), Blocks.SPRUCE_FENCE.getDefaultState());
	}
}
