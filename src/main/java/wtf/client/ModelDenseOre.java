package wtf.client;

import com.google.common.collect.ImmutableMap;
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
import wtf.init.WTFContent;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelDenseOre implements IModel {
    private final ResourceLocation cubeTexture;
    private final ResourceLocation overlayTexture;
    private final int meta;

    public ModelDenseOre(DenseOreResourceLocation location) {

        cubeTexture = new ResourceLocation("minecraft:blocks/stone");
        overlayTexture = new ResourceLocation(WTFContent.oreEntryMap.get(location.ore).getRawOverlayPath());

        meta = location.meta;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel model;

        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(Core.coreID + ":block/cube_overlay_hardcode"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IModel finalModel = model.retexture(ImmutableMap.of("cube", cubeTexture.toString(), "mask", overlayTexture.toString() + meta));
        return finalModel.bake(state, format, bakedTextureGetter);
    }

    /* ===========================================
                         LOADER
       =========================================== */
    @SideOnly(Side.CLIENT)
    public static class DenseOreLoader implements ICustomModelLoader {
        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Core.coreID) && modelLocation.getResourcePath().contains("dense");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {

            if (this.accepts(modelLocation))
                return new ModelDenseOre((DenseOreResourceLocation) modelLocation);
            throw new RuntimeException();
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }
}
