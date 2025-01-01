package wtf.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStainedTerracottaPatch extends BlockPatch {
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

	public BlockStainedTerracottaPatch() {
		super(Blocks.HARDENED_CLAY.getDefaultState());
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
	}

	public int damageDropped(IBlockState state) {
		return state.getValue(COLOR).getMetadata();
	}

	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
			items.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
		}
	}

	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.getBlockColor(state.getValue(COLOR));
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(COLOR).getMetadata();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR);
	}
}
