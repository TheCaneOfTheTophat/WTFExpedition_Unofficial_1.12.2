package wtf.client.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import wtf.WTFExpedition;
import wtf.blocks.BlockSpeleothem;
import wtf.blocks.BlockSpeleothemFrozen;
import wtf.client.DerivativeResourceLocation;
import wtf.client.WTFModelRegistry;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelSpeleothem implements IModel {

    private final ResourceLocation blockTexture;
    private final String typeName;
    private final boolean frozen;

    public ModelSpeleothem(DerivativeResourceLocation location) {
        IBlockState parentState;

        if(location.block instanceof BlockSpeleothemFrozen) {
            parentState = ((BlockSpeleothem) location.block.parentForeground.getBlock()).parentBackground;
            frozen = true;
        }
        else {
            parentState = location.block.parentBackground;
            frozen = false;
        }

        blockTexture = new ResourceLocation(WTFModelRegistry.textureMap.get(parentState));

        typeName = location.block.getStateFromMeta(location.meta).getValue(BlockSpeleothem.TYPE).getName();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel modelSpeleothem;
        IModel modelIce;

        try {
            modelSpeleothem = ModelLoaderRegistry.getModel(new ResourceLocation(WTFExpedition.modID + ":block/" + typeName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IModel finalModelSpeleothem = modelSpeleothem.retexture(ImmutableMap.of("texture", blockTexture.toString()));
        if(frozen) {
            try {
                modelIce = ModelLoaderRegistry.getModel(new ResourceLocation(WTFExpedition.modID + ":block/overlay"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            IModel finalModelIce = modelIce.retexture(ImmutableMap.of("mask", new ResourceLocation("blocks/ice").toString()));

            return new FrozenSpeleothemBakedModel(finalModelIce.bake(state, format, bakedTextureGetter), finalModelSpeleothem.bake(state, format, bakedTextureGetter));
        } else
            return finalModelSpeleothem.bake(state, format, bakedTextureGetter);
    }

    /* ===========================================
                         LOADER
       =========================================== */
    @SideOnly(Side.CLIENT)
    public static class Loader implements ICustomModelLoader {

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(WTFExpedition.modID) && modelLocation.getResourcePath().contains("speleothem") && modelLocation.getResourcePath().contains("hardcoded");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            if (this.accepts(modelLocation))
                return new ModelSpeleothem((DerivativeResourceLocation) modelLocation);
            throw new RuntimeException();
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }

    /* ===========================================
                      BAKED MODEL
       =========================================== */
    private static final class FrozenSpeleothemBakedModel implements IBakedModel {

        private final IBakedModel ice;
        private final IBakedModel speleothem;

        public FrozenSpeleothemBakedModel(IBakedModel ice, IBakedModel speleothem) {
            this.ice = ice;
            this.speleothem = speleothem;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
            List<BakedQuad> quads = new ArrayList<>();

            if (layer == BlockRenderLayer.TRANSLUCENT || layer == null)
                quads.addAll(ice.getQuads(state, side, rand));
            if (layer == BlockRenderLayer.SOLID || layer == null)
                quads.addAll(speleothem.getQuads(state, side, rand));

            return quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isAmbientOcclusion(IBlockState state) {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return ice.getParticleTexture();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return new ImmutablePair<IBakedModel, Matrix4f>(this, speleothem.handlePerspective(cameraTransformType).getRight());
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }
    }
}
