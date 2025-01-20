package wtf.blocks;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.init.WTFContent;

public class BlockNitreOre extends BlockOre {
	public BlockNitreOre() {
		super();
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return WTFContent.nitre;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return par1Random.nextInt(4) + 1;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
