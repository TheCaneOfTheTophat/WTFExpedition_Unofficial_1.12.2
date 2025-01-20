package wtf.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.blocks.AbstractBlockDerivative;
import wtf.blocks.AbstractBlockDerivativeFalling;

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

    public static class ItemBlockDerivativeFalling extends ItemBlockState {
        AbstractBlockDerivativeFalling block;

        public ItemBlockDerivativeFalling(AbstractBlockDerivativeFalling block) {
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
}
