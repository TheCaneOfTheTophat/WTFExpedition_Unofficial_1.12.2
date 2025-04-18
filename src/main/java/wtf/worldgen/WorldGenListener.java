package wtf.worldgen;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.config.WTFExpeditionConfig;
import wtf.init.BlockSets;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CavePosition;
import wtf.utilities.wrappers.SurfacePos;
import wtf.utilities.wrappers.UnsortedChunkCaves;
import wtf.worldgen.replacers.Replacer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class WorldGenListener {
    public static ArrayList<OreGenAbstract> oreGenRegister = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void newPopulate(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        int x = event.getChunkX();
        int z = event.getChunkZ();
        Random rand = event.getRand();

        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        ChunkPos chunkPos = new ChunkPos(x, z);

        SurfacePos[][] surfacePositions = new SurfacePos[16][16];
        int surfaceAvg = 0;

        UnsortedChunkCaves unsortedCavePos = new UnsortedChunkCaves(new ChunkPos(x, z));
        ArrayList<BlockPos> water = new ArrayList<>();

        int maxY = Math.min(255, getMaxY(chunk));

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            int worldX = (x << 4) + chunkX + 8;

            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                int worldZ = (z << 4) + chunkZ + 8;

                int Y = maxY;

                boolean surfaceFound = false;
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

                    if (!surfaceFound) {
                        surfaceFound = (BlockSets.surfaceMaterial.contains(state.getMaterial()) && state.isFullBlock() && !state.getBlock().hasTileEntity(state) && !isPosInSurfaceStructure(world, columnPos)) || Y == 1;

                        if (surfaceFound) {
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
    }
}
