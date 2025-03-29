package wtf.worldgen.replacers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.BlockSets;

public abstract class Replacer {

	public Replacer(Block block) {
		BlockSets.replacementMap.put(block,  this);
	}
	
	public abstract boolean replace(World world, BlockPos pos, IBlockState oldState);
}
