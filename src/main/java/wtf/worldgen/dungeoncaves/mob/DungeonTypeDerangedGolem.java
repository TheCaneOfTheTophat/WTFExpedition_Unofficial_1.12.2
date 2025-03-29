package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.BlockStoneBrick;
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

public class DungeonTypeDerangedGolem extends AbstractDungeonType {

	public DungeonTypeDerangedGolem(String name) {
		super(name, 0, 0);
	}

	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return isHeight(cave, 4);
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID, "deranged_iron_golem"), 1);
		replace(world, pos.getFloorPos().up(2), Blocks.ANVIL.getDefaultState());
		
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}
}
