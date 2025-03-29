package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonTypeNetherPortal extends AbstractDungeonType {

	public DungeonTypeNetherPortal(String name) {
		super(name, 0, 0);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return isHeight(cave, 6);
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos(), new ResourceLocation("zombie_pigman"), 5);
		
		replace(world, pos.getFloorPos().up(), Blocks.OBSIDIAN.getDefaultState());
		replace(world, pos.getFloorPos().up().east(), Blocks.OBSIDIAN.getDefaultState());
		replace(world, pos.getFloorPos().up().west(), Blocks.OBSIDIAN.getDefaultState());

		for (int loop = 1; loop < 7; loop++) {
			replace(world, pos.getFloorPos().up(loop).east(2), Blocks.OBSIDIAN.getDefaultState());
			replace(world, pos.getFloorPos().up(loop).west(2), Blocks.OBSIDIAN.getDefaultState());
		}

		replace(world, pos.getFloorPos().up(6), Blocks.OBSIDIAN.getDefaultState());
		replace(world, pos.getFloorPos().up(6).east(), Blocks.OBSIDIAN.getDefaultState());
		replace(world, pos.getFloorPos().up(6).west(), Blocks.OBSIDIAN.getDefaultState());

		for (int xloop = -1; xloop < 2; xloop++) {
			for (int yloop = 2; yloop < 6; yloop++) {
				replace(world, pos.getFloorPos().up(yloop).east(xloop), Blocks.PORTAL.getDefaultState());
			}
		}
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.NETHERRACK.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}
}
