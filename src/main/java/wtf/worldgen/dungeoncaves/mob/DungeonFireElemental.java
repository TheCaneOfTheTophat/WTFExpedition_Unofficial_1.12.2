package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonFireElemental extends AbstractDungeonType {

	public DungeonFireElemental(String name) {
		super(name, 0,0);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return true;
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID, "fire_elemental"), 2);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoise(world, pos) < 0.5) {
			replace(world, pos, Blocks.NETHERRACK.getDefaultState());
			replace(world, pos.up(), Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

}
