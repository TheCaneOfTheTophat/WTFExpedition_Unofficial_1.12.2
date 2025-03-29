package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonSpeleothemGrotto extends AbstractDungeonType {

	public DungeonSpeleothemGrotto(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return  isHeight(cave, 4);
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get2DRandom(world, pos) < 0.5)
			genSpeleothem(world, pos, rand.nextInt(5), depth, BiomeDictionary.hasType(world.getBiome(pos), Type.SNOWY));
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get2DRandom(world, pos) < 0.5)
			genSpeleothem(world, pos, rand.nextInt(3), depth, BiomeDictionary.hasType(world.getBiome(pos), Type.SNOWY));
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
