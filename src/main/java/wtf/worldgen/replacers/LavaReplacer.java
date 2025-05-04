package wtf.worldgen.replacers;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import wtf.config.WTFExpeditionConfig;
import wtf.enums.Modifier;
import wtf.utilities.simplex.SimplexHelper;

import static wtf.worldgen.GenMethods.*;

public class LavaReplacer extends Replacer {

	public LavaReplacer(boolean flowing) {
		super(flowing ? Blocks.FLOWING_LAVA : Blocks.LAVA);
	}

	private static SimplexHelper simplex = new SimplexHelper("LavaReplacer", true);
	
	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
		double n = simplex.get3DNoiseScaled(world, pos, 0.33);

		Biome biome = world.getBiomeForCoordsBody(pos);

		if(WTFExpeditionConfig.replaceLavaWithWater) {
			boolean surroundingBiomeOcean = false;

			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
				return override(world, pos, Blocks.WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, oldState.getValue(BlockLiquid.LEVEL)), false);

			for (EnumFacing facing : EnumFacing.HORIZONTALS) {
				surroundingBiomeOcean = BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos.offset(facing)), BiomeDictionary.Type.OCEAN);

				if (surroundingBiomeOcean)
					break;
			}

			if (surroundingBiomeOcean)
				return override(world, pos, Blocks.OBSIDIAN.getDefaultState(), false);
		}

		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) && oldState == Blocks.LAVA.getDefaultState().withProperty(BlockLiquid.LEVEL, 0) && WTFExpeditionConfig.replaceLavaWithObsidian) {
			if (n < 0.33)
				return false;
			else if (n < 0.66)
				return override(world, pos, Blocks.OBSIDIAN.getDefaultState(), false) && modify(world, pos, Modifier.LAVA_CRUST);
			else
				return override(world, pos, Blocks.OBSIDIAN.getDefaultState(), false);
		}

		return false;
	}
}
