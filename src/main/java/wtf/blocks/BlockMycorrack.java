package wtf.blocks;

import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

public class BlockMycorrack extends BlockNetherrack{

	public BlockMycorrack(){
		this.setHardness(0.4F);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == layer.TRANSLUCENT || layer == layer.SOLID;
    }
}
