package wtf.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import wtf.init.BlockSets;
import wtf.enums.Modifier;

import javax.annotation.Nullable;

public class BlockIcePatch extends BlockBreakable {

	protected static final AxisAlignedBB height1 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

	public BlockIcePatch() {
		super(Material.ICE, false);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);

		BlockSets.blockTransformer.put(Pair.of(Blocks.AIR.getDefaultState(), Modifier.FROZEN), this.getDefaultState());
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
	}

	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!canBlockStay(worldIn, pos))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!canBlockStay(world, pos))
			world.setBlockToAir(pos);

		if (world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getDefaultState().getLightOpacity())
			world.setBlockToAir(pos);
	}

	public boolean canBlockStay(World world, BlockPos pos) {
		if (!world.getBlockState(pos.down()).isBlockNormalCube())
			return false;

		for(BlockPos relpos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
			if(BlockSets.meltBlocks.contains(world.getBlockState(relpos).getBlock()))
				return false;

		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return height1;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return height1;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
}
