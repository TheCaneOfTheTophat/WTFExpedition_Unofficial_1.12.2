package wtf.client.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.Core;
import wtf.blocks.BlockDecoAnim;
import wtf.client.DerivativeResourceLocation;
import wtf.config.BlockEntry;
import wtf.init.JSONLoader;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelDecoAnim implements IModel {
    private final ResourceLocation cubeTexture;
    private final ResourceLocation overlayTexture;
    private final String overlayName;

    public ModelDecoAnim(DerivativeResourceLocation location) {
        IBlockState state = location.block.parentBackground;
        BlockDecoAnim block = (BlockDecoAnim) location.block;

        BlockEntry entry = JSONLoader.identifierToBlockEntry.get(state.getBlock().getRegistryName().toString() + "@" + state.getBlock().getMetaFromState(state));
        cubeTexture = new ResourceLocation(entry.getTexture());

        overlayName = block.getType().getOverlayName();
        overlayTexture = new ResourceLocation(Core.coreID + ":overlays/" + overlayName);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel model;

        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(overlayName == null ? "minecraft:block/cube_all" : Core.coreID + ":block/cube_overlay"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IModel finalModel = model.retexture(ImmutableMap.of("cube", cubeTexture.toString(), "all", cubeTexture.toString(), "mask", overlayTexture.toString()));
        return finalModel.bake(state, format, bakedTextureGetter);
    }

    /* ===========================================
                         LOADER
       =========================================== */
    @SideOnly(Side.CLIENT)
    public static class Loader implements ICustomModelLoader {
        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Core.coreID) && modelLocation.getResourcePath().contains("deco_animated") && modelLocation.getResourcePath().contains("hardcoded");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            if (this.accepts(modelLocation)) {
                Core.coreLog.fatal(modelLocation);
                return new ModelDecoAnim((DerivativeResourceLocation) modelLocation);
            }
            throw new RuntimeException();
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }
}
