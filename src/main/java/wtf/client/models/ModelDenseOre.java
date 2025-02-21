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
import wtf.WTFExpedition;
import wtf.client.DerivativeFallingResourceLocation;
import wtf.client.DerivativeResourceLocation;
import wtf.init.JSONLoader;
import wtf.init.WTFContent;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelDenseOre implements IModel {

    private final ResourceLocation cubeTexture;
    private final ResourceLocation overlayTexture;
    private final int meta;

    public ModelDenseOre(DerivativeResourceLocation location) {
        IBlockState parentState = location.block.parentBackground;

        cubeTexture = new ResourceLocation(JSONLoader.getEntryFromState(parentState).getTexture());

        overlayTexture = new ResourceLocation(WTFContent.oreEntryMap.get(location.block).getRawOverlayPath());

        meta = location.meta;
    }

    public ModelDenseOre(DerivativeFallingResourceLocation location) {
        IBlockState parentState = location.block.parentBackground;

        cubeTexture = new ResourceLocation(JSONLoader.getEntryFromState(parentState).getTexture());

        overlayTexture = new ResourceLocation(WTFContent.oreEntryMap.get(location.block).getRawOverlayPath());

        meta = location.meta;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel model;

        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(WTFExpedition.modID + ":block/cube_overlay"));
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
    public static class Loader implements ICustomModelLoader {

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(WTFExpedition.modID) && modelLocation.getResourcePath().contains("dense") && modelLocation.getResourcePath().contains("hardcoded");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            if (this.accepts(modelLocation))
                if(modelLocation instanceof DerivativeFallingResourceLocation)
                    return new ModelDenseOre((DerivativeFallingResourceLocation) modelLocation);
                else
                    return new ModelDenseOre((DerivativeResourceLocation) modelLocation);
            throw new RuntimeException();
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }
}
