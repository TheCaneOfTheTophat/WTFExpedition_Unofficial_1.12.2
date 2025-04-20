package wtf.blocks;

import java.util.Random;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.WTFExpedition;
import wtf.enums.AnimatedDecoType;

public class BlockDecoAnim extends AbstractBlockDerivative implements IDeco {

	private final AnimatedDecoType type;

	public static final PropertyBool FAST = PropertyBool.create("fast");
	
	public BlockDecoAnim(IBlockState state, AnimatedDecoType type) {
		super(state, state);
		this.type = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FAST, false));
	}

    @Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
    	if (type == AnimatedDecoType.LAVA_CRUST)
    		world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
    }

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FAST);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FAST, meta != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FAST) ? 1 : 0;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

	@Override
	public int getLightValue(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState other = worldIn.getBlockState(pos);
		if (other.getBlock() != this)
			return other.getLightValue(worldIn, pos);
		if (type == AnimatedDecoType.LAVA_CRUST)
			return 3;
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		int chance = state.getValue(FAST) ? 5 : 20;

		if (random.nextInt(chance) == 0){

			double x = pos.getX() + random.nextFloat();
			double y = pos.getY() - 0.05D;
			double z = pos.getZ() + random.nextFloat();

			switch (type){
				case LAVA_CRUST:
					if (!world.isBlockNormalCube(pos.up(), false))
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y+1, z, 0.0D, 0.0D, 0.0D);
					// Fall through
				case LAVA_DRIPPING:
					if (!world.isBlockNormalCube(pos.down(), false))
						world.spawnParticle(EnumParticleTypes.DRIP_LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
					break;
				case WET:
					if (!world.isBlockNormalCube(pos.down(), false))
						world.spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack blockStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
		return I18n.format(WTFExpedition.modID + ":deco_type." + getType().getName() + ".name") + " " + blockStack.getDisplayName();
	}

	public AnimatedDecoType getType() {
		return type;
	}
}
