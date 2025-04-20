package wtf.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPuddle extends Block {

	protected static final AxisAlignedBB height1 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

	public static SoundType puddle = new SoundType(1.0F, 1.0F, SoundEvents.ENTITY_GENERIC_SPLASH, SoundEvents.BLOCK_SLIME_STEP, SoundEvents.ENTITY_GENERIC_SPLASH, SoundEvents.ENTITY_PLAYER_SPLASH, SoundEvents.ENTITY_GENERIC_SPLASH);
	
	public BlockPuddle() {
		super(Material.SPONGE);
		this.setSoundType(puddle);
		this.setTickRandomly(false);
		this.setHardness(0.01F);
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
	}

	@Override
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
	}

	public boolean canBlockStay(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isBlockNormalCube();
    }

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
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
		return height1;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
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

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getMaterial() != this.blockMaterial && (side == EnumFacing.UP || super.shouldSideBeRendered(blockState, blockAccess, pos, side));
    }

    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return motion.addVector(-0.1, -0.1, -0.1);
    }
}
