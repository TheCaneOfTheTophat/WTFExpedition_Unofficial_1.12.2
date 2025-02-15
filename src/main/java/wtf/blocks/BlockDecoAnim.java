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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.WTFExpedition;
import wtf.init.BlockSets;

public class BlockDecoAnim extends AbstractBlockDerivative {
	private final AnimatedDecoType type;

	public static final PropertyBool FAST = PropertyBool.create("fast");
	
	public BlockDecoAnim(IBlockState state, AnimatedDecoType type) {
		super(state, state);
		this.type = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FAST, false));
		
//		BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.LAVA_CRUST), this.getDefaultState().withProperty(TYPE, AnimatedDecoType.LAVA_CRUST));
//		BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.WATER_DRIP), this.getDefaultState().withProperty(TYPE, AnimatedDecoType.DRIP_WATER));
//		BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.LAVA_DRIP), this.getDefaultState().withProperty(TYPE, AnimatedDecoType.DRIP_LAVA));
//
//		BlockSpeleothem block = WTFContent.speleothemMap.get(state);
//		WTFContent.speleothemMap.put(this.getDefaultState().withProperty(TYPE, AnimatedDecoType.DRIP_WATER), block);
//		IBlockState fractured =  BlockSets.blockTransformer.get(new StateAndModifier(state, BlockSets.Modifier.COBBLE));
//		BlockSets.blockTransformer.put(new StateAndModifier(this.getDefaultState().withProperty(TYPE, AnimatedDecoType.DRIP_WATER), BlockSets.Modifier.COBBLE), fractured);
//		BlockSets.blockTransformer.put(new StateAndModifier(this.getDefaultState().withProperty(TYPE, AnimatedDecoType.DRIP_LAVA), BlockSets.Modifier.COBBLE), fractured);
	}
	
	public void setFast(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos); 
		if (blockstate.getBlock() instanceof BlockDecoAnim)
			world.setBlockState(pos, getStateFromMeta(this.getMetaFromState(blockstate)+8));
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
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
    	if (type == AnimatedDecoType.LAVA_CRUST)
    		world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
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
			case DRIP_LAVA:
				if (!world.isBlockNormalCube(pos.down(), false))
					world.spawnParticle(EnumParticleTypes.DRIP_LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
				break;
			case DRIP_WATER:
				if (!world.isBlockNormalCube(pos.down(), false))
					world.spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState().withProperty(FAST, meta == 0 ? false : true);
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FAST) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FAST);
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

	public AnimatedDecoType getType() {
		return type;
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack blockStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
		return I18n.format(WTFExpedition.modID + ":deco_type." + getType().getName() + ".name") + " " + blockStack.getDisplayName();
	}

	public enum AnimatedDecoType implements IStringSerializable {
		LAVA_CRUST("lava_crust", BlockSets.Modifier.LAVA_CRUST, "lava_overlay"),
		DRIP_WATER("wet", BlockSets.Modifier.WATER_DRIP, null),
		DRIP_LAVA("lava_dripping", BlockSets.Modifier.LAVA_DRIP, null);

		private final String name;
		public final BlockSets.Modifier modifier;
        private final String overlayName;

        AnimatedDecoType(String name, BlockSets.Modifier mod, String overlayName) {
			this.name = name;
			this.modifier = mod;
            this.overlayName = overlayName;
        }

		@Override
		public String getName() {
			return name;
		}

		public String getOverlayName() {
			return overlayName;
		}

		@Override
		public String toString() {
			return getName();
		}
    }
}
