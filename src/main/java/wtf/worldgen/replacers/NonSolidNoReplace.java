package wtf.worldgen.replacers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import wtf.worldgen.GeneratorMethods;

public class NonSolidNoReplace extends Replacer {

	public NonSolidNoReplace(Block block) {
		super(block);
	}

	@Override
	public boolean isNonSolidAndReplacement(Chunk chunk, BlockPos pos, GeneratorMethods gen,IBlockState oldState) {
		return true;
	}

}
