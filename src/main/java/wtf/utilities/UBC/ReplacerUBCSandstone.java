package wtf.utilities.UBC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReplacerUBCSandstone extends ReplacerUBCAbstract{

	public ReplacerUBCSandstone(Block block) {
		super(block);

	}

	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
		/*
		IBlockState state = getUBCStone(pos);
		if (state.getBlock().hashCode() == sedHash){
			replace(world, pos, state);
		}*/
		double noise =getSimplexSand(world, pos);
		if (noise < 8){
			// replace(world, pos, sands[(int)noise]);
		}
		return false;
	}

}
