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
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;
import wtf.blocks.IDeco;
import wtf.blocks.IDerivative;
import wtf.client.DerivativeResourceLocation;
import wtf.client.WTFModelRegistry;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelDecoration implements IModel {

    private final ResourceLocation cubeTexture;
    private final ResourceLocation overlayTexture;
    private final String overlayName;

    public ModelDecoration(DerivativeResourceLocation location) {
        IBlockState state = ((IDerivative) location.block).getParentBackground();
        BlockDecoAnim decoAnim = null;
        BlockDecoStatic decoStatic = null;

        if(location.block instanceof BlockDecoAnim)
            decoAnim = (BlockDecoAnim) location.block;
        else
            decoStatic = (BlockDecoStatic) location.block;

        cubeTexture = new ResourceLocation(WTFModelRegistry.textureMap.get(state));

        if(location.block instanceof BlockDecoAnim)
            overlayName = decoAnim.getType().getOverlayName();
        else
            overlayName = decoStatic.getType().getOverlayName();

        overlayTexture = new ResourceLocation(WTFExpedition.modID + ":overlays/" + overlayName);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel model;

        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(overlayName == null ? "minecraft:block/cube_all" : WTFExpedition.modID + ":block/cube_overlay"));
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
            return modelLocation instanceof DerivativeResourceLocation && modelLocation.getResourceDomain().equals(WTFExpedition.modID) && (((DerivativeResourceLocation) modelLocation).block instanceof IDeco);
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            if (this.accepts(modelLocation))
                return new ModelDecoration((DerivativeResourceLocation) modelLocation);
            throw new RuntimeException();
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }
}
