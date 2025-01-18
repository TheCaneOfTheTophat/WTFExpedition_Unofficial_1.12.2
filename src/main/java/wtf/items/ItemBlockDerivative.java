package wtf.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.blocks.AbstractBlockDerivative;

public class ItemBlockDerivative extends ItemBlockState {
    AbstractBlockDerivative block;
    public ItemBlockDerivative(AbstractBlockDerivative block) {
        super(block);
        this.block = block;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        return block.getDisplayName(stack);
    }
}
