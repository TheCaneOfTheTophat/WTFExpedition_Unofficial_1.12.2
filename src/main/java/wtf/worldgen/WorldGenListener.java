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
    public static ArrayList<OreGenAbstract> oreGenRegister = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void newPopulate(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        int x = event.getChunkX();
        int z = event.getChunkZ();
        Random rand = event.getRand();

        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        ChunkPos chunkPos = new ChunkPos(x, z);

        SurfacePos[][] surfacePositions = WorldGenListener.surfacePosMap.get(Pair.of(new ChunkPos(x, z), world.provider.getDimension()));
        SurfacePos[][] surfacePositionsX = WorldGenListener.surfacePosMap.get(Pair.of(new ChunkPos(x + 1, z), world.provider.getDimension()));
        SurfacePos[][] surfacePositionsZ = WorldGenListener.surfacePosMap.get(Pair.of(new ChunkPos(x, z + 1), world.provider.getDimension()));
        SurfacePos[][] surfacePositionsXZ = WorldGenListener.surfacePosMap.get(Pair.of(new ChunkPos(x + 1, z + 1), world.provider.getDimension()));

        SurfacePos[][] surfacePositionsOffset = new SurfacePos[16][16];

        for(int xOffset = 8; xOffset < 24; xOffset++) {
            for (int zOffset = 8; zOffset < 24; zOffset++) {
                int actualX = xOffset % 16;
                int actualZ = zOffset % 16;

                boolean xOver = actualX != xOffset;
                boolean zOver = actualZ != zOffset;
                SurfacePos surfacePos = surfacePositions[actualX][actualZ];

                if(xOver && zOver)
                    surfacePos = surfacePositionsXZ[actualX][actualZ];
                else if (xOver)
                    surfacePos = surfacePositionsX[actualX][actualZ];
                else if (zOver)
                    surfacePos = surfacePositionsZ[actualX][actualZ];

                surfacePositionsOffset[xOffset - 8][zOffset - 8] = surfacePos;
            }
        }

        int surfaceAvg = 0;

        UnsortedChunkCaves unsortedCavePos = new UnsortedChunkCaves(new ChunkPos(x, z));
        ArrayList<BlockPos> water = new ArrayList<>();

        int maxY = Math.min(255, getMaxY(chunk));

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            int worldX = (x << 4) + chunkX + 8;

            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                int worldZ = (z << 4) + chunkZ + 8;

                int Y = maxY;

                SurfacePos columnSurfacePos = surfacePositionsOffset[chunkX][chunkZ];

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
                                    surfacePositionsOffset[chunkX][chunkZ].setGenerated();
                            }
                        } else if (adjustSurface && fullBlock) {
                            adjustSurface = false;
                            surfaceAvg += Y;
                            surfacePositionsOffset[chunkX][chunkZ] = new SurfacePos(worldX, Y, worldZ);

                            if (world.getBlockState(columnPos.up()).isFullBlock())
                                surfacePositionsOffset[chunkX][chunkZ].setGenerated();
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

        if (WTFExpeditionConfig.oreGenEnabled)
            for (OreGenAbstract generators : WorldGenListener.oreGenRegister)
                generators.generate(world, rand, chunkPos, surfaceAvg, unsortedCavePos, water);

        if (WTFExpeditionConfig.dungeonGenerationEnabled)
            DungeonGenerator.generate(world, rand, surfaceAvg, unsortedCavePos);

        if (WTFExpeditionConfig.caveGenerationEnabled)
            CaveGenerator.generate(world, rand, surfaceAvg, unsortedCavePos);

        if (WTFExpeditionConfig.overworldGenerationEnabled && WTFExpeditionConfig.bigTreesEnabled)
            TreeGenerator.generate(world, rand, surfacePositionsOffset);

        // TODO I don't have this under a config at the moment
        // TODO Probably shouldn't change biomes on post-populate methinks
        if (WTFExpeditionConfig.enableSurfaceModification)
            SubBiomeGenerator.generate(world, chunkPos);

        if (WTFExpeditionConfig.enableSurfaceModification && WTFExpeditionConfig.overworldGenerationEnabled)
            SurfaceGenerator.generate(world, surfacePositionsOffset);

        if(!world.isRemote) {
            for (int x1 = -1; x1 < 2; x1++) {
                for (int z1 = -1; z1 < 2; z1++) {
                    ChunkPos chunkPos0 = new ChunkPos(x + x1, z + z1);

                    if (eligibleForUnload(chunkPos0, world)) {
                        Pair<ChunkPos, Integer> key = Pair.of(chunkPos0, world.provider.getDimension());
                        WorldGenListener.surfacePosMap.remove(key);
                    }
                }
            }
        }
    }

    public static boolean eligibleForUnload(ChunkPos pos, World world) {
        ChunkProviderServer cps = (ChunkProviderServer) world.getChunkProvider();

        boolean unload = true;

        for (int x1 = -1; x1 < 2; x1++) {

            if (!unload)
                break;

            for (int z1 = -1; z1 < 2; z1++) {
                if ((!(x1 == 1 && z1 == -1) && !(x1 == -1 && z1 == 1))) {
                    ChunkPos chunkPos1 = new ChunkPos(pos.x + x1, pos.z + z1);

                    if (!cps.chunkExists(chunkPos1.x, chunkPos1.z) || !world.getChunkFromChunkCoords(chunkPos1.x, chunkPos1.z).isPopulated()) {
                        unload = false;
                        break;
                    }
                }
            }
        }

        return unload;
    }
}
