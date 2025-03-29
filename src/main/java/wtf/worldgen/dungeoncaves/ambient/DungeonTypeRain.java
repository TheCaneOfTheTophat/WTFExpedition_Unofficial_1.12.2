package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.blocks.BlockDecoAnim;
import wtf.init.BlockSets;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeRain extends AbstractDungeonType {

	public DungeonTypeRain(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return true;
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		IBlockState state = BlockSets.getTransformedState(world.getBlockState(pos), Modifier.WET);
		
		if (state != null && rand.nextBoolean())
			replace(world, pos, state.withProperty(BlockDecoAnim.FAST, true));
		else {
			int var = rand.nextInt(3);

			switch (var) {
			case 0:
				modify(world, pos, Modifier.FRACTURED);
				break;
			case 1:
				modify(world, pos, Modifier.MOSS);
				break;
			case 2:
				modify(world, pos, Modifier.FRACTURED);
				modify(world, pos, Modifier.MOSS);
				break;
			}
		}
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		int var = rand.nextInt(3);
		boolean water = rand.nextBoolean();

		switch (var) {
		case 0:
			if (water) {
				setWaterPatch(world, pos);
				modify(world, pos, Modifier.FRACTURED);
			} else
				modify(world, pos, Modifier.FRACTURED);
			break;
		case 1:
			if (water) {
				setWaterPatch(world, pos);
				modify(world, pos, Modifier.MOSS);
			} else
				modify(world, pos, Modifier.MOSS);
			break;
		case 2:
			if (water) {
				setWaterPatch(world, pos);
				modify(world, pos, Modifier.FRACTURED);
				modify(world, pos, Modifier.MOSS);
			} else {
				modify(world, pos, Modifier.FRACTURED);
				modify(world, pos.down(), Modifier.MOSS);
			}
			break;
		}
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		int var = rand.nextInt(3);

		switch (var) {
		case 0:
			modify(world, pos, Modifier.FRACTURED);
			break;
		case 1:
			modify(world, pos, Modifier.MOSS);
			break;
		case 2:
			modify(world, pos, Modifier.FRACTURED);
			modify(world, pos, Modifier.MOSS);
			break;
		}
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}
}
