package wtf.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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
import wtf.enums.IcicleType;
import wtf.init.BlockSets;

public class BlockIcicle extends AbstractBlockDerivative {

	public static final IProperty<IcicleType> TYPE = PropertyEnum.create("type", IcicleType.class);
	
	public BlockIcicle() {
		super(Blocks.ICE.getDefaultState(), Blocks.ICE.getDefaultState());
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!canBlockStay(world, pos, state))
			world.destroyBlock(pos, true);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	if(!canBlockStay(worldIn, pos, state))
			worldIn.destroyBlock(pos, true);
    }

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!canBlockStay(worldIn, pos, state))
			worldIn.destroyBlock(pos, true);
	}
	
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		for(BlockPos relPos : BlockPos.getAllInBoxMutable(pos.add(-1, -2, -1), pos.add(1, 1, 1)))
			if(BlockSets.meltBlocks.contains(world.getBlockState(relPos).getBlock()))
				return false;

		if (state.getBlock() == this)
			switch (state.getValue(TYPE)) {
				case icicle_base:
				case icicle_small: return (world.getBlockState(pos.up()).isBlockNormalCube());
				case icicle_tip: return (world.getBlockState(pos.up()) == this.getDefaultState().withProperty(TYPE, IcicleType.icicle_base));
				default: return false;
			}

		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(TYPE).boundingBox;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getValue(TYPE).boundingBox;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@Override
	public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> items) {
		for (int loop = 0; loop < IcicleType.values().length; loop++)
			items.add(new ItemStack(this, 1, loop));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, IcicleType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getID();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	public IBlockState getBlockState(IcicleType type) {
		return this.getDefaultState().withProperty(TYPE, type);
	}
}
