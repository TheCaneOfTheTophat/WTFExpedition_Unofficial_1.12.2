package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeBatCave extends AbstractDungeonType {

	public DungeonTypeBatCave(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return isHeight(cave, 4);
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		setCeilingAddon(world, pos, Modifier.FRACTURED);
		spawnVanillaSpawner(world, pos.up(), new ResourceLocation("bat"), 6);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}
