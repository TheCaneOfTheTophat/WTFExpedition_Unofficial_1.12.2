package wtf.blocks;


import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;

public class BlockDenseOreFalling extends AbstractBlockDerivativeFalling {

    public static final PropertyInteger DENSITY = PropertyInteger.create("density", 0, 2);

    public BlockDenseOreFalling(IBlockState backState, IBlockState foreState) {
        super(backState, foreState);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DENSITY, 0));
        this.setHarvestLevel(backState.getBlock().getHarvestTool(backState), foreState.getBlock().getHarvestLevel(foreState));
//		BlockSets.stoneAndOre.put(new StoneAndOre(backState, foreState), this.getDefaultState());
//		BlockSets.oreAndFractures.add(this);
//		BlockSets.surfaceBlocks.add(this);
        this.disableStats();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
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
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        this.onBlockHarvested(world, pos, state, player);

        if(state.getValue(DENSITY) < 2 && !player.capabilities.isCreativeMode)
            state = state.withProperty(DENSITY, state.getValue(DENSITY)+1);
        else {
            if (!player.capabilities.isCreativeMode) {
                this.parentBackground.getBlock().dropBlockAsItem(world, pos, this.parentBackground, 0);
            }
            state = Blocks.AIR.getDefaultState();
        }

        player.addStat(StatList.getBlockStats(this.parentForeground.getBlock()));
        return world.setBlockState(pos, state, world.isRemote ? 11 : 3);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DENSITY);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.parentForeground.getBlock().canSilkHarvest(world, pos, state, player);
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        ItemStack backStack = new ItemStack(parentBackground.getBlock(), 1, parentBackground.getBlock().getMetaFromState(parentBackground));
        ItemStack foreStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
        return I18n.format(WTFExpedition.modID + ":prefix.dense.name") + " " + backStack.getDisplayName() + " " + foreStack.getDisplayName();
    }
}
