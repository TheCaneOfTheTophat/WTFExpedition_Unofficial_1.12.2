package wtf.worldgen.caves.types;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypePaintedDesert extends AbstractCaveType{

	IBlockState sand = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
	IBlockState sandstone = Blocks.RED_SANDSTONE.getDefaultState();

	//Red, orange, yellow, brown, white, light gray and unstained hardened c

	IBlockState[] slabs = {
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE),
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN),
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE),
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE),
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW),
			WTFContent.terracotta_patch.getDefaultState(),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE),
			WTFContent.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PINK),
			//WTFBlocks.terracotta_patch_stained.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE),
	};
	
	IBlockState[] clay = {
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE),
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN),
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE),
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE),
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW),
			Blocks.HARDENED_CLAY.getDefaultState(),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE),
			Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PINK),
			//Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE),
	};
	


	public CaveTypePaintedDesert(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.2);

		if (noise < 0.3)
			setPatch(world, pos, slabs[(int)simplex.get3DNoiseScaled(world, pos, 0.33) * slabs.length]);
		else if (noise > 0.66)
			if (rand.nextBoolean())
				replace(world, pos, Blocks.GRASS_PATH.getDefaultState());
			else
				setPatch(world, pos, WTFContent.red_sand_patch.getDefaultState());
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (height < 3) {
			double noise = simplex.get3DNoiseScaled(world, pos, 0.2);

			if (noise < 0.3)
				replace(world, pos.up(), clay[(int)simplex.get3DNoiseScaled(world, pos, 0.33) * clay.length]);
		}
	}
}
