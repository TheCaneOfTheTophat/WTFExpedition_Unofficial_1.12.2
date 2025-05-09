package wtf.blocks.ores;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.init.BlockSets;

import java.util.Random;

public class BlockRedstoneOreDerivative extends BlockOreDerivative implements IOreRedstone {

    private final boolean isOn;
    private BlockRedstoneOreDerivative toggledBlock;

    public BlockRedstoneOreDerivative(IBlockState backState, boolean isOn) {
        super(backState, Blocks.REDSTONE_ORE.getDefaultState());

        if (isOn) {
            this.setLightLevel(0.625F);
            this.setTickRandomly(true);
            BlockSets.explosiveBlocks.put(this, 2F);
        } else
            BlockSets.explosiveBlocks.put(this, 3F);

        this.isOn = isOn;
    }

    @Override
    public int tickRate(World worldIn) {
        return 30;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.activate(worldIn, pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        this.activate(worldIn, pos);
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        this.activate(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (isOn)
            worldIn.setBlockState(pos, getToggled(worldIn.getBlockState(pos)));
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isOn)
            this.spawnParticles(worldIn, pos);
    }

    private void spawnParticles(World worldIn, BlockPos pos) {
        Random random = worldIn.rand;

        for (int i = 0; i < 6; ++i) {
            double d1 = (float)pos.getX() + random.nextFloat();
            double d2 = (float)pos.getY() + random.nextFloat();
            double d3 = (float)pos.getZ() + random.nextFloat();

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube())
                d2 = (double)pos.getY() + 0.0625D + 1.0D;

            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube())
                d2 = (double)pos.getY() - 0.0625D;

            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube())
                d3 = (double)pos.getZ() + 0.0625D + 1.0D;

            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube())
                d3 = (double)pos.getZ() - 0.0625D;

            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube())
                d1 = (double)pos.getX() + 0.0625D + 1.0D;

            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube())
                d1 = (double)pos.getX() - 0.0625D;

            if (d1 < (double)pos.getX() || d1 > (double)(pos.getX() + 1) || d2 < 0.0D || d2 > (double)(pos.getY() + 1) || d3 < (double)pos.getZ() || d3 > (double)(pos.getZ() + 1))
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    private void activate(World worldIn, BlockPos pos) {
        this.spawnParticles(worldIn, pos);

        if (!isOn)
            worldIn.setBlockState(pos, getToggled(worldIn.getBlockState(pos)));
    }

    private IBlockState getToggled(IBlockState state) {
        return toggledBlock.getDefaultState();
    }

    public void setToggled(Block block) {
        toggledBlock = (BlockRedstoneOreDerivative) block;
    }
}

