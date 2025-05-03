package wtf.blocks.ores;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.blocks.AbstractBlockDerivative;
import wtf.config.WTFExpeditionConfig;

public class BlockOreDerivative extends AbstractBlockDerivative implements IOre {

    public BlockOreDerivative(IBlockState backState, IBlockState foreState) {
        super(backState, foreState);
        this.setHarvestLevel(backState.getBlock().getHarvestTool(backState), foreState.getBlock().getHarvestLevel(foreState));
        this.disableStats();
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        this.onBlockHarvested(world, pos, state, player);

        if(WTFExpeditionConfig.oresDropStone) {
            if (!player.capabilities.isCreativeMode)
                this.parentBackground.getBlock().dropBlockAsItem(world, pos, this.parentBackground, 0);

            player.addStat(StatList.getBlockStats(this.parentBackground.getBlock()));
        }

        player.addStat(StatList.getBlockStats(this.parentForeground.getBlock()));

        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return Math.max(parentBackground.getBlockHardness(worldIn, pos), parentForeground.getBlockHardness(worldIn, pos));
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return Math.max(parentBackground.getBlock().getExplosionResistance(exploder) / 2.5F, parentForeground.getBlock().getExplosionResistance(exploder) / 2.5F);
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        ItemStack backStack = new ItemStack(parentBackground.getBlock(), 1, parentBackground.getBlock().getMetaFromState(parentBackground));
        ItemStack foreStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
        return I18n.format(backStack.getDisplayName() + " " + foreStack.getDisplayName());
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }
}
