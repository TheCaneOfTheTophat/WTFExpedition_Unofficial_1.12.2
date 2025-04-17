package wtf.worldgen.dungeoncaves.ambient;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonJungleTemple extends AbstractDungeonType {

	public DungeonJungleTemple(String name) {
		super(name, 5, 0);
	}
	
	@Override
	public boolean canGenerateAt(World world, CaveListWrapper cave) {
		return isHeight(cave, 4) && cave.getAvgFloor() > 36;
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		replace(world, pos.getFloorPos(), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		replace(world, pos.getFloorPos().down(), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		replace(world, pos.getFloorPos().up(), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		replace(world, pos.getFloorPos().up(2), Blocks.TORCH.getDefaultState());
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoise(world, pos) < 0.25)
			replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		else
			replace(world, pos, Blocks.STONEBRICK.getDefaultState());
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		if (simplex.get3DNoise(world, pos) < 0.25)
			replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		else
			replace(world, pos, Blocks.STONEBRICK.getDefaultState());
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		if (simplex.get3DNoise(world, pos) < 0.25)
			replace(world, pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		else
			replace(world, pos, Blocks.STONEBRICK.getDefaultState());
	}

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		if (rand.nextFloat() < depth && setCeilingAddon(world, pos, Modifier.FRACTURED)) {

			for(EnumFacing facing : EnumFacing.HORIZONTALS) {
				boolean cancel;

				for (int loop = rand.nextInt(3) + 1; loop > -1; loop--) {
					cancel = !genVines(world, pos.offset(facing.getOpposite()).down(loop), facing);

					if (cancel)
						break;
				}
			}
		}
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}
	
	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
		if (simplex.get3DNoise(world, pos) < 0.15)
			genVines(world, pos, pos.getFace(rand));
	}
}
