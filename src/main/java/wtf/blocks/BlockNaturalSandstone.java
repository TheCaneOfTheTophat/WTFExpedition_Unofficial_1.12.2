package wtf.blocks;

import net.minecraft.block.state.IBlockState;
import org.apache.commons.lang3.tuple.Pair;
import wtf.init.BlockSets;
import wtf.enums.Modifier;

public class BlockNaturalSandstone extends AbstractBlockDerivative {

	public BlockNaturalSandstone(IBlockState state) {
		 super(state, state);
		 BlockSets.blockTransformer.put(Pair.of(this.getDefaultState(), Modifier.BRICK), state);
	}
}

