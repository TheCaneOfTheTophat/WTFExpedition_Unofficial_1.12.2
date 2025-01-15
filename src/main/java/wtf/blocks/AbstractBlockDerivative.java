package wtf.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBlockDerivative extends Block{
	
	public final IBlockState parentBackground;
	public final IBlockState parentForeground;

	/**
	 * This class is used to create blocks whose properties are derivative of other blocks
	 * backState is used to determine the properties of the block
	 * foreState is used to determine a block's harvesting and drops
	 * they can be the same block, for simple blocks, or more different for things like ores, with an ore and a background stone
	 * @param backState
	 * @param foreState
	 */
	public AbstractBlockDerivative(IBlockState backState, IBlockState foreState) {
		super(backState.getMaterial());
		this.parentBackground = backState;
		this.parentForeground = foreState;
		this.setHarvestLevel(foreState.getBlock().getHarvestTool(foreState), foreState.getBlock().getHarvestLevel(foreState));
	}

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return parentForeground.getBlock().getItemDropped(parentForeground, rand, fortune);
    }
	
    @Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return parentBackground.getBlockHardness(worldIn, pos);
    }
	
    @Override
	public float getExplosionResistance(Entity exploder) {
        return parentBackground.getBlock().getExplosionResistance(exploder)/2.5F;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return parentBackground.getBlock().getBlockLayer();
    }
    
    @Override
	public SoundType getSoundType() {
        return parentBackground.getBlock().getSoundType();
    }
    
    @Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return parentBackground.getBlock().getFlammability(world, pos, face);
    }
    
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return this.parentBackground.getBlock().canDropFromExplosion(explosionIn);
    }
}
