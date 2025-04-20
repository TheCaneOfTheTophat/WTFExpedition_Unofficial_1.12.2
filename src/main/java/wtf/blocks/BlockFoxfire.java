package wtf.blocks;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFoxfire extends BlockBush {

	protected static final AxisAlignedBB hangingBB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.8F, 0.8F);
	protected static final AxisAlignedBB sittingBB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.8F, 0.8F);

	public static final PropertyBool HANGING = PropertyBool.create("hanging");
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	private static final int lightCutoff = 7;

	public BlockFoxfire() {
		super(Material.PLANTS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(HANGING, false).withProperty(ACTIVE, false));
	}

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.scheduleBlockUpdate(pos, this, 0, 10);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		if (side == EnumFacing.DOWN)
			return canBlockStay(worldIn, pos, this.getDefaultState().withProperty(HANGING, true));

		return side == EnumFacing.UP && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing enumfacing : EnumFacing.values())
			return canPlaceBlockOnSide(worldIn, pos, enumfacing);

		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return facing == EnumFacing.DOWN ? this.getDefaultState().withProperty(HANGING, true) : this.getDefaultState();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		// Check
		if (!canBlockStay(world, pos, state)) {
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}

		// Glow
		else if (world.getLightFor(EnumSkyBlock.BLOCK, pos) < lightCutoff && (!(world.getSkylightSubtracted() < lightCutoff) || world.getLightFor(EnumSkyBlock.SKY, pos) < lightCutoff))
			if(!state.getValue(ACTIVE))
				world.setBlockState(pos, state.withProperty(ACTIVE, true));
			else
				world.scheduleBlockUpdate(pos, this, 40, 10);
		else if (state.getValue(ACTIVE))
			world.setBlockState(pos, state.withProperty(ACTIVE, false));

		// Spread
		if (random.nextInt(25) == 0) {
			int threshold = 6;

			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -2, -4), pos.add(4, 2, 4))) {
				if (world.getBlockState(blockpos).getBlock() == this) {
					--threshold;

					if (threshold <= 0)
						return;
				}
			}

			BlockPos blockpos1 = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 2, random.nextInt(3) - 1);

			for (int k = 0; k < 4; ++k) {
				if (world.isAirBlock(blockpos1) && this.canBlockStay(world, blockpos1, this.getDefaultState()) || this.canBlockStay(world, blockpos1, this.getDefaultState().withProperty(HANGING, true)))
					pos = blockpos1;

				blockpos1 = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 2, random.nextInt(3) - 1);
			}

			if (world.isAirBlock(pos)) {
				if (this.canBlockStay(world, pos, this.getDefaultState()) && this.canBlockStay(world, pos, this.getDefaultState().withProperty(HANGING, true)))
					world.setBlockState(pos, random.nextBoolean() ? this.getDefaultState() : this.getDefaultState().withProperty(HANGING, true), 2);
				else if (this.canBlockStay(world, pos, this.getDefaultState()))
					world.setBlockState(pos, this.getDefaultState(), 2);
				else if (this.canBlockStay(world, pos, this.getDefaultState().withProperty(HANGING, true)))
					world.setBlockState(pos, this.getDefaultState().withProperty(HANGING, true), 2);
			}
		}
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) {
			IBlockState stateDown = worldIn.getBlockState(pos.down());
			IBlockState stateUp = worldIn.getBlockState(pos.up());

			return (stateUp.getMaterial() == Material.WOOD && stateUp.isSideSolid(worldIn, pos.up(), EnumFacing.DOWN) && state.getValue(HANGING)) ||
					(stateDown.getMaterial() == Material.WOOD && stateDown.isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !state.getValue(HANGING));
		}

		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(HANGING) ? hangingBB : sittingBB;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HANGING, ACTIVE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		switch (meta) {
            case 1: return getDefaultState().withProperty(HANGING, true);
			case 2: return getDefaultState().withProperty(ACTIVE, true);
			case 3: return getDefaultState().withProperty(ACTIVE, true).withProperty(HANGING, true);
			default: return getDefaultState();
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HANGING) ? (state.getValue(ACTIVE) ? 3 : 1) : (state.getValue(ACTIVE) ? 2 : 0);
	}

	@Override
	public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Cave;
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.isFullBlock();
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entityIn) {
		world.scheduleBlockUpdate(pos, this, 0, 20);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState other = worldIn.getBlockState(pos);

		if (other.getBlock() != this)
			return other.getLightValue(worldIn, pos);

		if (state.getValue(ACTIVE))
			return 6;

		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (state.getValue(ACTIVE) && random.nextInt(15) == 0)
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + random.nextFloat(), pos.getY() + 0.5F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
}
