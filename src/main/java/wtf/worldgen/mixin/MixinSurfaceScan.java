package wtf.worldgen.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import wtf.worldgen.GenMethods;
import wtf.worldgen.WorldGenListener;
import wtf.utilities.wrappers.SurfacePos;

@Mixin(ChunkProviderServer.class)
public abstract class MixinSurfaceScan {

    @Shadow @Final public WorldServer world;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;populate(Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/gen/IChunkGenerator;)V"), method = "provideChunk(II)Lnet/minecraft/world/chunk/Chunk;", locals = LocalCapture.CAPTURE_FAILHARD)
    protected void getSurface(int x, int z, CallbackInfoReturnable<Chunk> cir, Chunk chunk) {
        SurfacePos[][] surfacePositions = new SurfacePos[16][16];
        int maxY = Math.min(255, GenMethods.getMaxY(chunk));

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            int worldX = (chunk.x << 4) + chunkX;

            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                int worldZ = (chunk.z << 4) + chunkZ;

                int Y = maxY;

                while (Y > 0) {
                    IBlockState state = chunk.getBlockState(chunkX, Y, chunkZ);

                    if (!state.isFullBlock())
                        Y--;
                    else
                        break;
                }

                surfacePositions[chunkX][chunkZ] = new SurfacePos(worldX, Y, worldZ);
            }
        }

        WorldGenListener.surfacePosMap.put(Pair.of(new ChunkPos(x, z), world.provider.getDimension()), surfacePositions);
    }
}
