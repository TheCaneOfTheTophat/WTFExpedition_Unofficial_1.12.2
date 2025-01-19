package wtf.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.BlockIce;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.Core;
import wtf.init.BlockSets;

public class BlockSpeleothemFrozen extends BlockSpeleothem {

	BlockSpeleothem speleothem;

	public BlockSpeleothemFrozen(BlockSpeleothem block) {
		super(Blocks.ICE.getDefaultState(), block.getDefaultState());

		speleothem = block;

		this.setHarvestLevel(parentBackground.getBlock().getHarvestTool(parentBackground), parentBackground.getBlock().getHarvestLevel(parentBackground));
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		switch (side) {
		case DOWN: return isFrozen(blockAccess.getBlockState(pos.down()));
		case EAST: return isFrozen(blockAccess.getBlockState(pos.east()));
		case NORTH: return isFrozen(blockAccess.getBlockState(pos.north()));
		case SOUTH: return isFrozen(blockAccess.getBlockState(pos.south()));
		case UP: return isFrozen(blockAccess.getBlockState(pos.up()));
		case WEST: return isFrozen(blockAccess.getBlockState(pos.west()));
		}
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	private Boolean isFrozen(IBlockState state) {
        return !(state.getBlock() instanceof BlockSpeleothemFrozen) && !(state.getBlock() instanceof BlockIce);
    }

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack stoneStack = new ItemStack(speleothem.parentForeground.getBlock(), 1, speleothem.parentForeground.getBlock().getMetaFromState(speleothem.parentForeground));
		return I18n.format(Core.coreID + ":prefix.frozen.name") + " " + stoneStack.getDisplayName() + " " + I18n.format("tile." + Core.coreID + ":speleothem." + stack.getItemDamage() + ".name");
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, this.speleothem.getDefaultState().withProperty(TYPE, state.getValue(TYPE)));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	@Deprecated
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return blockState.getBoundingBox(worldIn, pos);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {

		for(BlockPos relpos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
			if(BlockSets.meltBlocks.contains(world.getBlockState(relpos).getBlock())) {
				int meta = getMetaFromState(world.getBlockState(pos));
				IBlockState newState = this.parentForeground.getBlock().getStateFromMeta(meta);

				world.setBlockState(pos, newState);
			}

		return super.canBlockStay(world, pos, state);
	}
}
