package wtf.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.blocks.BlockDenseOre;

public class ItemBlockDenseOre extends ItemBlockState {
    public ItemBlockDenseOre(BlockDenseOre block) {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        return block.getLocalizedName();
    }
}
