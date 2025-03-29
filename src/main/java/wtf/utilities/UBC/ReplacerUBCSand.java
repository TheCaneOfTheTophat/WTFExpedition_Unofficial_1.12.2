package wtf.utilities.UBC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.worldgen.GenMethods;

public class ReplacerUBCSand extends ReplacerUBCAbstract{


	public ReplacerUBCSand(Block block) {
		super(block);

	}

	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
		if (!BiomeDictionary.hasType(world.getBiome(pos), Type.SANDY)){

			double noise =getSimplexSand(world, pos);
			if (noise < 8){
				 GenMethods.replace(world, pos, sands[(int)noise]);
			}
		}
		return false;
	}




}
