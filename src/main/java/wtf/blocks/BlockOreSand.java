package wtf.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOreSand extends BlockFalling {

	public BlockOreSand() {
		this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
        this.setHarvestLevel("shovel", 0);
	}
	
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.GOLD_NUGGET;
    }

    public int quantityDropped(Random random) {
        return 1 + random.nextInt(2);
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
