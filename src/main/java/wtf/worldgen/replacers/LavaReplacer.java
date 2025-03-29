package wtf.worldgen.replacers;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.enums.Modifier;
import wtf.utilities.simplex.SimplexHelper;
import wtf.worldgen.caves.CaveTypeRegister;
import wtf.worldgen.caves.types.CaveTypeHell.hellBiome;

import static wtf.worldgen.GenMethods.*;

public class LavaReplacer extends Replacer {

	public LavaReplacer() {
		super(Blocks.LAVA);
	}

	private static SimplexHelper simplex = new SimplexHelper("LavaReplacer");
	
	@Override
	public boolean replace(World world, BlockPos pos, IBlockState oldState) {
			double n = simplex.get3DNoiseScaled(world, pos, 0.33);

			Biome biome = world.getBiomeForCoordsBody(pos);
			
			if (BiomeDictionary.hasType(biome,BiomeDictionary.Type.OCEAN)) {
				override(world, pos, Blocks.WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, oldState.getValue(BlockLiquid.LEVEL)), false);
				return true;
			} else if (BiomeDictionary.hasType(biome,BiomeDictionary.Type.SNOWY)) {
				if (n < 0.33)
					return true;
				else if (n < 0.66) {
					override(world, pos, Blocks.OBSIDIAN.getDefaultState(), false);
					modify(world, pos, Modifier.LAVA_CRUST);
				}
				else
					override(world, pos, Blocks.OBSIDIAN.getDefaultState(), false);
			} else if (BiomeDictionary.hasType(biome, Type.NETHER)){
				hellBiome netherbiome = CaveTypeRegister.nether.getSubType(pos);

				switch (netherbiome) {
				case DEADFOREST:
					break;
				case FIREFOREST:
					break;
				case FROZEN:
					break;
				case MUSHROOM:
					break;
				case NORMAL:
					break;
				case SOULDESERT:
					
				default:
					break;
				}
			}
			return false;
	}
}
