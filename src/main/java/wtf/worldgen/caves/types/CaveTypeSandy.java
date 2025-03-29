package wtf.worldgen.caves.types;

import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeSandy extends AbstractCaveType {

	private final IBlockState sand;
	private final IBlockState sandstone;
	private final IBlockState slab;
	
	public CaveTypeSandy(String name, int ceilingAddonPercentChance, int floorAddonPercentChance, boolean redSand) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
		this.sand = redSand ? Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND) : Blocks.SAND.getDefaultState();
		this.sandstone = redSand ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
		this.slab = redSand ? WTFContent.red_sand_patch.getDefaultState() : WTFContent.sand_patch.getDefaultState();
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoiseScaled(world, pos, 0.2) < 0.3)
			setPatch(world, pos, slab);
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
		if (height < 3 && simplex.get3DNoiseScaled(world, pos, 0.2) < 0.3)
			replace(world, pos, sandstone);
	}
}
