package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeCaveIn extends AbstractDungeonType {

	public DungeonTypeCaveIn(String name) {
		super(name, 0, 0);
		this.genAir = true;
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return true;
	}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateAir(World world, Random rand, BlockPos floating, float depth) {
		double noise = simplex.get3DNoise(world, floating.getX(), floating.getY(), floating.getZ());

		if (noise < 0.5) {
			genFloatingStone(world, floating);
			modify(world, floating, Modifier.FRACTURED, true);
		}
	}
}
