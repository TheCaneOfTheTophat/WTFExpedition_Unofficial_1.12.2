package wtf.utilities.UBC;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.BlockSets;
import wtf.enums.Modifier;

import static wtf.worldgen.GenMethods.*;

public class ReplacerUBCMossyCobble extends ReplacerUBCAbstract{

	public ReplacerUBCMossyCobble() {
		super(Blocks.MOSSY_COBBLESTONE);
	}

	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
		
		IBlockState cobble = BlockSets.getTransformedState(getUBCStone(pos), Modifier.FRACTURED);
		IBlockState mossy = BlockSets.getTransformedState(cobble, Modifier.MOSS);
		
		//Running it off simplex sand, because it's easy	
		//double noise =getSimplexSand(chunk.getWorld(), pos);
			//if (noise < 8){
		if (mossy != null){		
			override(world, pos, mossy);
		}
		//else spawn something striking to test
		

		//}
		return false;
	//}
	}

}
