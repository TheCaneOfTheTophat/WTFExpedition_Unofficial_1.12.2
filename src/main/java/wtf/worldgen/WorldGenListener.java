package wtf.worldgen;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import wtf.config.WTFExpeditionConfig;
import wtf.init.BlockSets;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CavePosition;
import wtf.utilities.wrappers.SurfacePos;
import wtf.utilities.wrappers.UnsortedChunkCaves;
import wtf.worldgen.replacers.Replacer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class WorldGenListener {

    public static HashMap<Pair<ChunkPos, Integer>, SurfacePos[][]> surfacePosMap = new HashMap<>();

    // TODO Save scan results and chunk status as NBT
    public static HashSet<Pair<ChunkPos, Integer>> chunkGenMap = new HashSet<>();
    public static ArrayList<OreGenAbstract> oreGenRegister = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void populate(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        ChunkProviderServer cps = (ChunkProviderServer) world.getChunkProvider();

        // Get chunks surrounding the chunk being vanilla populated
        for (int x0 = -2; x0 < 3; x0++) {
            for (int z0 = -2; z0 < 3; z0++) {

                boolean pop = true;
                ChunkPos chunkPos0 = new ChunkPos(event.getChunkX() + x0, event.getChunkZ() + z0);

                // Get chunks surrounding the chunks surrounding the chunk being vanilla populated
                for (int x1 = -1; x1 < 2; x1++) {

                    if(!pop)
                        break;

                    for (int z1 = -1; z1 < 2; z1++) {

                        // Discount that chunk
                        if (x1 == 0 && z1 == 0)
                            continue;

                        ChunkPos chunkPos1 = new ChunkPos(chunkPos0.x + x1, chunkPos0.z + z1);

                        // If one of the chunks surrounding that chunk do not exist or aren't populated, break and set pop to false
                        if (cps.chunkExists(chunkPos1.x, chunkPos1.z)) {
                            if (!world.getChunkFromChunkCoords(chunkPos1.x, chunkPos1.z).isTerrainPopulated()) {
                                pop = false;
                                break;
                            }
                        } else {
                            pop = false;
                            break;
                        }
                    }
                }

                // TODO scan only, then generate with Expedition features when a chunk and it's +X, +Z and +XZ have been scanned
                if (cps.chunkExists(chunkPos0.x, chunkPos0.z)) {
                    if (pop && !chunkGenMap.contains(Pair.of(chunkPos0, world.provider.getDimension()))) {
                        wtfPopulate(world, chunkPos0.x, chunkPos0.z);
                        chunkGenMap.add(Pair.of(chunkPos0, world.provider.getDimension()));
                    }
                }
            }
        }
    }

    // TODO Offset all generation by 8 on X and Z
    public static void wtfPopulate(World world, int x, int z) {
        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        ChunkPos chunkPos = new ChunkPos(x, z);

        SurfacePos[][] surfacePositions = WorldGenListener.surfacePosMap.get(Pair.of(new ChunkPos(chunk.x, chunk.z), world.provider.getDimension()));
        int surfaceAvg = 0;
        UnsortedChunkCaves unsortedCavePos = new UnsortedChunkCaves(new ChunkPos(x, z));
        ArrayList<BlockPos> water = new ArrayList<>();

        int maxY = Math.min(255, getMaxY(chunk));

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            int worldX = (chunk.x << 4) + chunkX;

            for (int chunkZ0 = 0; chunkZ0 < 16; chunkZ0++) {
                int chunkZ;

                if ((chunkX & 1) == 0)
                    chunkZ = chunkZ0;
                else
                    chunkZ = 15 - chunkZ0;

                int worldZ = (chunk.z << 4) + chunkZ;

                int Y = maxY;

                SurfacePos columnSurfacePos = surfacePositions[chunkX][chunkZ];

                boolean surfaceFound = false;
                boolean adjustSurface = false;
                int ceiling = -1;

                HashSet<AdjPos> caveAdj = new HashSet<>();
                HashSet<BlockPos> caveWall = new HashSet<>();

                while (Y > 0) {
                    BlockPos columnPos = new BlockPos(worldX, Y, worldZ);
                    IBlockState state = world.getBlockState(columnPos);
                    Replacer replacer = BlockSets.replacementMap.get(state.getBlock());
                    boolean fullBlock = state.isFullBlock();

                    if (replacer != null)
                        replacer.replace(world, columnPos, state);

                    if(fullBlock && (world.getBlockState(columnPos.up()).getBlock() instanceof BlockFluidBase || world.getBlockState(columnPos.up()).getBlock() instanceof BlockLiquid))
                        water.add(columnPos);

                    if (Y >= columnSurfacePos.getY() || adjustSurface) {
                        if (!surfaceFound) {
                            surfaceFound = Y == columnSurfacePos.getY();
                            adjustSurface = surfaceFound && state.getMaterial() == Material.AIR;

                            if (surfaceFound && !adjustSurface) {
                                surfaceAvg += Y;

                                if (world.getBlockState(columnPos.up()).isFullBlock())
                                    surfacePositions[chunkX][chunkZ].setGenerated();
                            }
                        } else if (adjustSurface && fullBlock) {
                            adjustSurface = false;
                            surfaceAvg += Y;
                            surfacePositions[chunkX][chunkZ] = new SurfacePos(worldX, Y, worldZ);

                            if (world.getBlockState(columnPos.up()).isFullBlock())
                                surfacePositions[chunkX][chunkZ].setGenerated();
                        }
                    } else {
                        if (!fullBlock) {
                            if (ceiling == -1) {
                                ceiling = Y + 1;
                            }

                            for (EnumFacing face : EnumFacing.HORIZONTALS) {
                                BlockPos adjPos = columnPos.offset(face);

                                if (world.getBlockState(adjPos.up()).isFullBlock() && world.getBlockState(adjPos.down()).isFullBlock() && world.getBlockState(adjPos).isFullBlock()) {
                                    caveWall.add(adjPos);
                                    caveAdj.add(new AdjPos(columnPos, face));
                                }
                            }

                        } else if (ceiling > -1) {
                            CavePosition pos = new CavePosition(worldX, ceiling, Y, worldZ);
                            pos.adj.addAll(caveAdj);
                            pos.wall.addAll(caveWall);
                            unsortedCavePos.add(pos);

                            caveAdj.clear();
                            caveWall.clear();
                            ceiling = -1;
                        }
                    }

                    Y--;
                }
            }
        }

        surfaceAvg = Math.round(surfaceAvg / 256F);
        unsortedCavePos.getSortedCaves();

        Random rand = new Random(world.getSeed());
        long k = rand.nextLong() / 2L * 2L + 1L;
        long l = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed((long) x * k + (long) z * l ^ world.getSeed());

        if (WTFExpeditionConfig.oreGenEnabled)
            for (OreGenAbstract generators : WorldGenListener.oreGenRegister)
                generators.generate(world, rand, chunkPos, surfaceAvg, unsortedCavePos, water);

        if (WTFExpeditionConfig.dungeonGenerationEnabled)
            DungeonGenerator.generate(world, rand, surfaceAvg, unsortedCavePos);

        if (WTFExpeditionConfig.caveGenerationEnabled)
            CaveGenerator.generate(world, rand, surfaceAvg, unsortedCavePos);

        if (WTFExpeditionConfig.overworldGenerationEnabled && WTFExpeditionConfig.bigTreesEnabled)
            TreeGenerator.generate(world, rand, surfacePositions);

        // TODO I don't have this under a config at the moment
        // TODO Probably shouldn't change biomes on post-populate methinks
        if (WTFExpeditionConfig.enableSurfaceModification)
            SubBiomeGenerator.generate(world, chunkPos);

        if (WTFExpeditionConfig.enableSurfaceModification && WTFExpeditionConfig.overworldGenerationEnabled)
            SurfaceGenerator.generate(world, surfacePositions);

        WorldGenListener.surfacePosMap.remove(Pair.of(new ChunkPos(chunk.x, chunk.z), world.provider.getDimension()));
    }
}
