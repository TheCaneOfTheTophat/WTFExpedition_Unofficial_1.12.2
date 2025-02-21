package wtf.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
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
import wtf.WTFExpedition;
import wtf.blocks.enums.SpeleothemType;
import wtf.init.BlockSets;

public class BlockSpeleothemFrozen extends BlockSpeleothem {

	BlockSpeleothem speleothem;

	public BlockSpeleothemFrozen(BlockSpeleothem block) {
		super(Blocks.ICE.getDefaultState(), block.getDefaultState());

		speleothem = block;

		this.setHarvestLevel(parentBackground.getBlock().getHarvestTool(parentBackground), parentBackground.getBlock().getHarvestLevel(parentBackground));
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, this.speleothem.getDefaultState().withProperty(TYPE, state.getValue(TYPE)));
		worldIn.scheduleUpdate(pos, this.speleothem, 0);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if(!canBlockStay(worldIn, pos, state)) {
			worldIn.setBlockState(pos, this.speleothem.getDefaultState().withProperty(TYPE, state.getValue(TYPE)));
			worldIn.scheduleUpdate(pos, this.speleothem, 0);
		}
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		for(BlockPos relpos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
			if(BlockSets.meltBlocks.contains(world.getBlockState(relpos).getBlock()))
				return false;

		if(state.getBlock() == this) {
			switch (state.getValue(TYPE)) {
				case speleothem_column: return traverseColumn(world, pos);
				case stalactite_base:
				case stalactite_small: return (world.getBlockState(pos.up()) == speleothem.parentForeground);
				case stalactite_tip: return (hasProperty(world.getBlockState(pos.up()), SpeleothemType.stalactite_base) || hasProperty(world.getBlockState(pos.up()), SpeleothemType.speleothem_column));
				case stalagmite_base:
				case stalagmite_small: return (world.getBlockState(pos.down()) == speleothem.parentForeground);
				case stalagmite_tip: return (hasProperty(world.getBlockState(pos.down()), SpeleothemType.stalagmite_base) || hasProperty(world.getBlockState(pos.down()), SpeleothemType.speleothem_column));
				default: return false;
			}
		}

		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
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
	public String getDisplayName(ItemStack stack) {
		ItemStack stoneStack = new ItemStack(speleothem.parentForeground.getBlock(), 1, speleothem.parentForeground.getBlock().getMetaFromState(speleothem.parentForeground));
		return I18n.format(WTFExpedition.modID + ":prefix.frozen.name") + " " + stoneStack.getDisplayName() + " " + I18n.format("tile." + WTFExpedition.modID + ":speleothem." + stack.getItemDamage() + ".name");
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();

		if (block instanceof BlockSpeleothemFrozen || block instanceof BlockIce)
			return false;

		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();

		if (block instanceof BlockSpeleothemFrozen || block instanceof BlockIce)
			return true;

		return super.doesSideBlockRendering(blockState, blockAccess, pos, side);
	}
}
