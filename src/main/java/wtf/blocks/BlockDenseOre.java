package wtf.blocks;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wtf.WTFExpedition;

import javax.annotation.Nullable;

public class BlockDenseOre extends AbstractBlockDerivative {

	public static final PropertyInteger DENSITY = PropertyInteger.create("density", 0, 2);
	
	public BlockDenseOre(IBlockState backState, IBlockState foreState) {
		super(backState, foreState);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DENSITY, 0));
		this.setHarvestLevel(backState.getBlock().getHarvestTool(backState), foreState.getBlock().getHarvestLevel(foreState));
//		BlockSets.stoneAndOre.put(new StoneAndOre(backState, foreState), this.getDefaultState());
//		BlockSets.surfaceBlocks.add(this);
		this.disableStats();
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return this.parentForeground.getBlock().canSilkHarvest(world, pos, state, player);
	}
	
    @Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    	this.onBlockHarvested(world, pos, state, player);

        if(state.getValue(DENSITY) < 2 && !player.capabilities.isCreativeMode)
        	state = state.withProperty(DENSITY, state.getValue(DENSITY)+1);
        else {
			if (!player.capabilities.isCreativeMode)
				this.parentBackground.getBlock().dropBlockAsItem(world, pos, this.parentBackground, 0);

        	state = Blocks.AIR.getDefaultState();
        }

        player.addStat(StatList.getBlockStats(this.parentForeground.getBlock()));
        return world.setBlockState(pos, state, world.isRemote ? 11 : 3);
    }

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		if(state.getValue(DENSITY) < 2) {
			Vec3d vec3d = player.getPositionEyes(1.0F);
			Vec3d vec3d1 = player.getLook(1.0F);
			double blockReachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
			Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
			RayTraceResult result = worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);

			if (result != null)
				pos = pos.offset(result.sideHit);
		}

		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}

    @Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DENSITY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState().withProperty(DENSITY, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DENSITY);
	}

    @Override
    public String getDisplayName(ItemStack stack) {
        ItemStack backStack = new ItemStack(parentBackground.getBlock(), 1, parentBackground.getBlock().getMetaFromState(parentBackground));
        ItemStack foreStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
        return I18n.format(WTFExpedition.modID + ":prefix.dense.name") + " " + backStack.getDisplayName() + " " + foreStack.getDisplayName();
    }

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
}
