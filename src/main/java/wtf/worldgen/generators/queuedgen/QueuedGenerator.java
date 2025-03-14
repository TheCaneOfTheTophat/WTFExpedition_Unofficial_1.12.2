package wtf.worldgen.generators.queuedgen;

import net.minecraft.block.state.IBlockState;

public interface QueuedGenerator {

	IBlockState getBlockState(IBlockState oldstate);
	
}
