package wtf.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRoots extends AbstractBlockDerivative{

	// TODO Fix roots dropping oak logs in weird amounts.
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
	
	public BlockRoots() {
		super(Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState());
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!worldIn.isRemote && !canBlockStay(worldIn, pos, state)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) {
			IBlockState stateUp = worldIn.getBlockState(pos.up());
			return stateUp.isBlockNormalCube();
		}
		return false;
	}
	
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            java.util.List<ItemStack> items = this.getDrops(worldIn, pos, state, fortune);

            for (ItemStack item : items) {
                if (worldIn.rand.nextFloat() <= chance) {
                    spawnAsEntity(worldIn, pos, item);
                }
            }
        }
    }

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
    
    @Override
	public int quantityDropped(Random random) {
        return 1 + random.nextInt(2);
    }
    
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
        BlockPlanks.EnumType type = state.getValue(VARIANT);
		return type.getMetadata();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> items) {
		for (int loop = 0; loop < BlockPlanks.EnumType.values().length; loop++){
			items.add(new ItemStack(this, 1, loop));
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
    
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
