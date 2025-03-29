package wtf.worldgen;

import java.util.HashMap;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import wtf.utilities.simplex.SimplexHelper;
import wtf.worldgen.subbiomes.SubBiome;

public class SubBiomeGenerator {

	public static HashMap <Byte, SubBiome> subBiomeRegistry = new HashMap<>();
	private static SimplexHelper simplex = new SimplexHelper("SubBiome");
	
	public static void generate(World world, ChunkPos chunkPos) {
		byte[] newBiomes = world.getChunkFromChunkCoords(chunkPos.x, chunkPos.z).getBiomeArray();

		for (int xloop = 0; xloop < 16 ; xloop++) {
			for (int zloop = 0; zloop < 16; zloop++) {
				int loop = xloop + zloop * 16;

				SubBiome sub = subBiomeRegistry.get(newBiomes[loop]);

				if (sub != null) {
					double x = (xloop + chunkPos.getXStart()) / sub.scale();
					double z = (zloop + chunkPos.getZStart()) / sub.scale();

					if (simplex.get2DNoise(world, x, z) < sub.freq())
						newBiomes[loop] = sub.getID();
				}
			}
		}
	}
}
