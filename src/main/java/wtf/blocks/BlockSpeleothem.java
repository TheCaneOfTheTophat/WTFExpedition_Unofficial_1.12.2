package wtf.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.init.WTFContent;

import java.util.Random;


public class BlockSpeleothem extends AbstractBlockDerivative {

	public static final IProperty<SpeleothemType> TYPE = PropertyEnum.create("type", SpeleothemType.class);

	public BlockSpeleothem frozen;

	public BlockSpeleothem(IBlockState state) {
		super(state, state);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, SpeleothemType.stalactite_small));
		WTFContent.speleothemMap.put(state, this);
	}

	protected BlockSpeleothem(IBlockState backState, IBlockState foreState) {
		super(backState, foreState);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, SpeleothemType.stalactite_small));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if(!canBlockStay(worldIn, pos, state))
			worldIn.destroyBlock(pos, true);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!canBlockStay(world, pos, state)) {
			world.destroyBlock(pos, true);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!canBlockStay(worldIn, pos, state)) {
			worldIn.destroyBlock(pos, true);
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(TYPE).boundingBox;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getValue(TYPE).boundingBox;
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		if(state.getBlock() == this) {
			switch (state.getValue(TYPE)) {
				// TODO Make columns not be able to float by just having two columns in the air
				case speleothem_column:
					return (hasProperty(world.getBlockState(pos.down()), SpeleothemType.stalagmite_base) ||
							hasProperty(world.getBlockState(pos.down()), SpeleothemType.speleothem_column) ||
							hasProperty(world.getBlockState(pos.up()), SpeleothemType.stalactite_base) ||
							hasProperty(world.getBlockState(pos.up()), SpeleothemType.speleothem_column));
				case stalactite_base:
				case stalactite_small:
					return (world.getBlockState(pos.up()) == this.parentForeground);
				case stalactite_tip:
					return (hasProperty(world.getBlockState(pos.up()), SpeleothemType.stalactite_base) ||
							hasProperty(world.getBlockState(pos.up()), SpeleothemType.speleothem_column));
				case stalagmite_base:
				case stalagmite_small:
					return (world.getBlockState(pos.down()) == this.parentForeground);
				case stalagmite_tip:
					return (hasProperty(world.getBlockState(pos.down()), SpeleothemType.stalagmite_base) ||
							hasProperty(world.getBlockState(pos.down()), SpeleothemType.speleothem_column));
			}
		}
		return false;
	}

	protected boolean hasProperty(IBlockState state, SpeleothemType type) {
		if (state.getBlock() instanceof BlockSpeleothem) {
			return state.getValue(TYPE) == type;
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {		
		return getDefaultState().withProperty(TYPE, SpeleothemType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		SpeleothemType type = state.getValue(TYPE);
		return type.getID();
	}

	@Override
	public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> items) {
		for (int loop = 0; loop < SpeleothemType.values().length; loop++){
			items.add(new ItemStack(this, 1, loop));
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack stoneStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
		return stoneStack.getDisplayName() + " " + I18n.format("tile." + WTFExpedition.modID + ":speleothem." + stack.getItemDamage() + ".name");
	}

	public IBlockState getBlockState(SpeleothemType type){
		return this.getDefaultState().withProperty(TYPE, type);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public enum SpeleothemType implements IStringSerializable {
		stalactite_small(0, "stalactite_small", new AxisAlignedBB(0.2F, 0.2F, 0.2F, 0.8F, 1F, 0.8F)),
		stalactite_base(1, "stalactite_base", new AxisAlignedBB(0.2F, 0F, 0.2F, .8F, 1F, .8F)),
		stalactite_tip(2, "stalactite_tip", new AxisAlignedBB(0.3F, 0.4F, 0.3F, 0.7F, 1F, 0.7F)),
		speleothem_column(3, "speleothem_column", new AxisAlignedBB(0.3F, 0F, 0.3F, 0.7F, 1F, 0.7F)),
		stalagmite_small(4, "stalagmite_small", new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.8F, 0.8F)),
		stalagmite_base(5, "stalagmite_base", new AxisAlignedBB(0.2F, 0F, 0.2F, .8F, 1F, .8F)),
		stalagmite_tip(6, "stalagmite_tip", new AxisAlignedBB(0.3F, 0F, 0.3F, 0.7F, 0.7F, 0.7F));

		private final int ID;
		private final String name;
		public final AxisAlignedBB boundingBox;

		SpeleothemType(int ID, String name, AxisAlignedBB box) {
			this.ID = ID;
			this.name = name;
			this.boundingBox = box;
		}

		@Override
		public String getName() {
			return name;
		}

		public int getID() {
			return ID;
		}

		@Override
		public String toString() {
			return getName();
		}
	}
}
