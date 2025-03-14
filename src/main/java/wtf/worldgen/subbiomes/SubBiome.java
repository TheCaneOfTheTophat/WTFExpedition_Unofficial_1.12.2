package wtf.worldgen.subbiomes;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import wtf.utilities.wrappers.ChunkScan;

public interface SubBiome {

	void resetTopBlock(World world, BlockPos pos);
	
	double scale();

	double freq();

	Biome getBiome();

	byte getID();

	Biome getParentBiome();

	WorldGenerator getTree(ChunkScan chunkscan, Random random);
}
