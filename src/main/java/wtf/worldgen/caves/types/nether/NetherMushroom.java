package wtf.worldgen.caves.types.nether;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class NetherMushroom extends AbstractCaveType {

	public NetherMushroom() {
		super("NetherMushroom", 0, 1);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		replace(world, pos, WTFContent.mycorrack.getDefaultState());
    }

	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		generateMushroom(world, rand, pos);
    }

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

	
	public boolean generateMushroom(World world, Random rand, BlockPos position) {
        Block block = rand.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
        
        int i = rand.nextInt(3) + 4;

        if (rand.nextInt(12) == 0)
            i *= 2;

        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 < 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 3;

                if (j <= position.getY() + 3)
                    k = 0;

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            IBlockState state = world.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1));

                            if (state.getBlock().hashCode() != Blocks.AIR.hashCode())
                                flag = false;
                        } else
                            flag = false;
                    }
                }
            }

            if (!flag)
                return false;
            else {
                Block block1 = world.getBlockState(position.down()).getBlock();

                if (block1.hashCode() != WTFContent.mycorrack.hashCode())
                    return false;
                else {
                    int k2 = position.getY() + i;

                    if (block == Blocks.RED_MUSHROOM_BLOCK)
                        k2 = position.getY() + i - 3;

                    for (int l2 = k2; l2 <= position.getY() + i; ++l2) {
                        int j3 = 1;

                        if (l2 < position.getY() + i)
                            ++j3;

                        if (block == Blocks.BROWN_MUSHROOM_BLOCK)
                            j3 = 3;

                        int k3 = position.getX() - j3;
                        int l3 = position.getX() + j3;
                        int j1 = position.getZ() - j3;
                        int k1 = position.getZ() + j3;

                        for (int l1 = k3; l1 <= l3; ++l1) {
                            for (int i2 = j1; i2 <= k1; ++i2) {
                                int j2 = 5;

                                if (l1 == k3)
                                    --j2;
                                else if (l1 == l3)
                                    ++j2;

                                if (i2 == j1)
                                    j2 -= 3;
                                else if (i2 == k1)
                                    j2 += 3;

                                BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);

                                if (block == Blocks.BROWN_MUSHROOM_BLOCK || l2 < position.getY() + i) {
                                    if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1))
                                        continue;

                                    if (l1 == position.getX() - (j3 - 1) && i2 == j1)
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;

                                    if (l1 == k3 && i2 == position.getZ() - (j3 - 1))
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;

                                    if (l1 == position.getX() + (j3 - 1) && i2 == j1)
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;

                                    if (l1 == l3 && i2 == position.getZ() - (j3 - 1))
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;

                                    if (l1 == position.getX() - (j3 - 1) && i2 == k1)
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;

                                    if (l1 == k3 && i2 == position.getZ() + (j3 - 1))
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;

                                    if (l1 == position.getX() + (j3 - 1) && i2 == k1)
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;

                                    if (l1 == l3 && i2 == position.getZ() + (j3 - 1))
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                }

                                if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + i)
                                    blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;

                                if (position.getY() >= position.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                                    BlockPos blockpos = new BlockPos(l1, l2, i2);
                                    IBlockState state = world.getBlockState(blockpos);
                                    replace(world, blockpos, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
                                }
                            }
                        }
                    }

                    for (int i3 = 0; i3 < i; ++i3) {
                        IBlockState iblockstate = world.getBlockState(position.up(i3));

                        if (iblockstate.getBlock().canBeReplacedByLeaves(iblockstate, world, position.up(i3)))
                            replace(world, position.up(i3), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
                    }

                    return true;
                }
            }
        }
        else
            return false;
    }
}
