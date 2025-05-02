package wtf.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IDerivative {

    IBlockState getParentBackground();

    IBlockState getParentForeground();

    String getDisplayName(ItemStack stack);
}
