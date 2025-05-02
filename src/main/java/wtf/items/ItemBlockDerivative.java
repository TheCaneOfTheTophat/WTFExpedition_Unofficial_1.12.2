package wtf.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.blocks.IDerivative;

public class ItemBlockDerivative extends ItemBlockState {
    Block block;

    public ItemBlockDerivative(IDerivative block) {
        super((Block) block);
        this.block = (Block) block;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        return ((IDerivative) block).getDisplayName(stack);
    }
}
