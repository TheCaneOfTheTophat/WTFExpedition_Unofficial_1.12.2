package wtf.utilities.UBC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.worldgen.GeneratorMethods;

public class ReplacerUBCSand extends ReplacerUBCAbstract{


	public ReplacerUBCSand(Block block) {
		super(block);

	}

	@Override
	public boolean isNonSolidAndReplacement(Chunk chunk, BlockPos pos, GeneratorMethods gen, IBlockState oldState) {
		if (!BiomeDictionary.hasType(chunk.getWorld().getBiome(pos), Type.SANDY)){

			double noise =getSimplexSand(chunk.getWorld(), pos);
			if (noise < 8){
				gen.replaceBlock(pos, sands[(int)noise]);
			}
		}
		return false;
	}




}
