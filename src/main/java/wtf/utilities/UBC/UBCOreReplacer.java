package wtf.utilities.UBC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static wtf.worldgen.GenMethods.*;

public class UBCOreReplacer extends ReplacerUBCAbstract{

	public UBCOreReplacer(Block block) {
		super(block);
	}

	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
		IBlockState state = getUBCStone(pos);
		//IBlockState newBlock = BlockSets.stoneAndOre.get(new StoneAndOre(state, oldState));
		override(world, pos, state);
		return false;
	}

}
