package wtf.client.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
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
import wtf.blocks.BlockSpeleothem;
import wtf.blocks.BlockSpeleothemFrozen;
import wtf.client.DerivativeResourceLocation;
import wtf.init.JSONLoader;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelSpeleothem implements IModel {
    private final ResourceLocation blockTexture;
    private final String typeName;

    public ModelSpeleothem(DerivativeResourceLocation location) {
        IBlockState parentState;

        if(location.block instanceof BlockSpeleothemFrozen)
            parentState = ((BlockSpeleothem) location.block.parentForeground.getBlock()).parentBackground;
        else
            parentState = location.block.parentBackground;

        Block parentBlock = parentState.getBlock();

        blockTexture = new ResourceLocation(JSONLoader.identifierToBlockEntry.get(parentBlock.getRegistryName().toString() + "@" + parentBlock.getMetaFromState(parentState)).getTexture());

        typeName = location.block.getStateFromMeta(location.meta).getValue(BlockSpeleothem.TYPE).getName() + (location.block instanceof BlockSpeleothemFrozen ? "_frozen" : "");
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel model;

        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(Core.coreID + ":block/" + typeName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IModel finalModel = model.retexture(ImmutableMap.of("texture", blockTexture.toString(), "ice", "minecraft:blocks/ice"));
        return finalModel.bake(state, format, bakedTextureGetter);
    }

    /* ===========================================
                         LOADER
       =========================================== */
    @SideOnly(Side.CLIENT)
    public static class Loader implements ICustomModelLoader {
        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Core.coreID) && modelLocation.getResourcePath().contains("speleothem") && modelLocation.getResourcePath().contains("hardcoded");
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

}
