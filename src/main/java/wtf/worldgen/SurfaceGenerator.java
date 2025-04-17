package wtf.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import wtf.config.WTFExpeditionConfig;
import wtf.enums.Modifier;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.SurfacePos;

import static wtf.worldgen.GenMethods.modify;

public class SurfaceGenerator {

    private static final SimplexHelper simplex = new SimplexHelper("SurfaceGenerator", true);

    public static void generate(World world, SurfacePos[][] surfacePos) {
        for (SurfacePos[] posArray : surfacePos) {
            for (SurfacePos pos : posArray) {
                Biome biome = world.getBiome(pos);
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
                    if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.riverFracturedChunkPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.riverFracturedBlockPercent)
                        modify(world, pos, Modifier.FRACTURED);
                } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN)) {
                    if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.mountainFracturedChunkPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.mountainFracturedBlockPercent)
                        modify(world, pos, Modifier.FRACTURED);
                } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST )) {
                    if (simplex.get2DNoise(world, pos.getX() * 16, pos.getZ() * 16) > WTFExpeditionConfig.forestMossyChunkPercent && simplex.get3DNoise(world, pos) > WTFExpeditionConfig.forestMossyBlockPercent)
                        modify(world, pos, Modifier.MOSS);
                }
            }
        }
    }
}
