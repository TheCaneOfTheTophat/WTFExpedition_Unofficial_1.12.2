package wtf.worldgen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import wtf.utilities.wrappers.SurfacePos;

import java.util.ArrayList;
import java.util.HashSet;

public class ListenerChunkScanStorage {

    @SubscribeEvent
    public void load(ChunkDataEvent.Load event) {
        if(!event.getWorld().isRemote) {
            int x = event.getChunk().x;
            int z = event.getChunk().z;
            Pair<ChunkPos, Integer> key = Pair.of(new ChunkPos(x, z), event.getWorld().provider.getDimension());

            if(event.getData().hasKey("WTFExpedition_Data")) {
                NBTTagCompound expeditionNbt = event.getData().getCompoundTag("WTFExpedition_Data");

                if(expeditionNbt.hasKey("surface")) {
                    NBTTagCompound surfacePositionsNbt = expeditionNbt.getCompoundTag("surface");
                    SurfacePos[][] surfacePoses = new SurfacePos[16][16];

                    for(int posX = 0; posX < 16; posX++) {
                        for (int posZ = 0; posZ < 16; posZ++) {
                            if(surfacePositionsNbt.hasKey("x" + posX + "z" + posZ)) {
                                NBTTagCompound individualPosNbt = surfacePositionsNbt.getCompoundTag("x" + posX + "z" + posZ);

                                surfacePoses[posX][posZ] = new SurfacePos((x << 4) + posX, individualPosNbt.getInteger("y"), (z << 4) + posZ);
                            }
                        }
                    }

                    WorldGenListener.surfacePosMap.put(key, surfacePoses);
                }
            }
        }
    }

    @SubscribeEvent
    public void save(ChunkDataEvent.Save event) {
        if(!event.getWorld().isRemote) {
            int x = event.getChunk().x;
            int z = event.getChunk().z;
            Pair<ChunkPos, Integer> key = Pair.of(new ChunkPos(x, z), event.getWorld().provider.getDimension());

            NBTTagCompound expeditionNbt = new NBTTagCompound();

            NBTTagCompound surfacePositionsNbt = new NBTTagCompound();
            SurfacePos[][] surfacePoses = WorldGenListener.surfacePosMap.get(key);

            if(surfacePoses != null) {
                for (int posX = 0; posX < 16; posX++) {
                    for (int posZ = 0; posZ < 16; posZ++) {
                        SurfacePos surfacePos = surfacePoses[posX][posZ];

                        NBTTagCompound individualPosNbt = new NBTTagCompound();
                        individualPosNbt.setInteger("y", surfacePos.getY());

                        surfacePositionsNbt.setTag("x" + posX + "z" + posZ, individualPosNbt);
                    }
                }

                expeditionNbt.setTag("surface", surfacePositionsNbt);
            }

            event.getData().setTag("WTFExpedition_Data", expeditionNbt);
        }
    }

    @SubscribeEvent
    public void unload(ChunkEvent.Unload event) {
        World world = event.getWorld();

        if(!world.isRemote) {
            ChunkProviderServer cps = (ChunkProviderServer) world.getChunkProvider();

            for (int x1 = -1; x1 < 2; x1++) {
                for (int z1 = -1; z1 < 2; z1++) {
                    ChunkPos chunkPos0 = new ChunkPos(event.getChunk().x + x1, event.getChunk().z + z1);

                    if (cps.chunkExists(chunkPos0.x, chunkPos0.z) && world.getChunkFromChunkCoords(chunkPos0.x, chunkPos0.z).isPopulated()) {
                        Pair<ChunkPos, Integer> key = Pair.of(chunkPos0, world.provider.getDimension());
                        WorldGenListener.surfacePosMap.remove(key);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event) {
        if(!event.getWorld().isRemote)
            for(Pair<ChunkPos, Integer> key : new ArrayList<>(new HashSet<>(WorldGenListener.surfacePosMap.keySet())))
                if (key.getValue() == event.getWorld().provider.getDimension())
                    WorldGenListener.surfacePosMap.remove(key);
    }
}
