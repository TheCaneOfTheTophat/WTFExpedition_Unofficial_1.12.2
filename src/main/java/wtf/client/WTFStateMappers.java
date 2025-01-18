package wtf.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import wtf.blocks.*;

public final class WTFStateMappers {

    /*
        REMINDER: A state mapper gets every block state for a block and makes new ModelResourceLocations for them.
        Each MRL holds the ResourceLocation, variants, and any other necessary data for model rendering.
        A model loader will volunteer to load the MRL, and models are made per variant.
    */

    public static final IStateMapper DENSE_ORE_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockDenseOre ore = (BlockDenseOre) state.getBlock();
            return new DerivativeResourceLocation(ore, "density", ore.getMetaFromState(state));
        }
    };

    public static final IStateMapper SPELEOTHEM_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockSpeleothem block = (BlockSpeleothem) state.getBlock();
            return new DerivativeResourceLocation(block, "type", block.getMetaFromState(state));
        }
    };

    public static final IStateMapper STATIC_DECORATION_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockDecoStatic block = (BlockDecoStatic) state.getBlock();
            return new DerivativeResourceLocation(block, "normal");
        }
    };

    public static final IStateMapper ANIMATED_DECORATION_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockDecoAnim block = (BlockDecoAnim) state.getBlock();
            return new DerivativeResourceLocation(block, "normal");
        }
    };
}
