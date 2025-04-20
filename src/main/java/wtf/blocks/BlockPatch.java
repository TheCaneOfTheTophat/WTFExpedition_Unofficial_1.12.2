package wtf.blocks;

import java.util.Random;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.WTFExpedition;

import javax.annotation.Nullable;

public class BlockPatch extends BlockFalling {

	protected static final AxisAlignedBB height1 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

	private final IBlockState state;

	public BlockPatch(IBlockState state) {
		super(state.getMaterial());
		this.state = state;
		this.setCreativeTab(WTFExpedition.wtfTab);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
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
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return state.getBlockHardness(worldIn, pos);
    }

    @Override
	public float getExplosionResistance(Entity exploder) {
        return state.getBlock().getExplosionResistance(exploder) / 2.5F;
    }
    
    @Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return state.getBlock().getFlammability(world, pos, face);
    }

	@Override
	public SoundType getSoundType() {
		return state.getBlock().getSoundType();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return state.getBlock().getBlockLayer();
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		if (layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID)
			return true;

		return super.canRenderInLayer(state, layer);
	}
}
