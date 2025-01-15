package wtf.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import wtf.blocks.BlockDenseOre;

public final class WTFStateMappers {
    public static final IStateMapper DENSE_ORE_STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockDenseOre ore = (BlockDenseOre) state.getBlock();
            return new DenseOreResourceLocation(ore, ore.getMetaFromState(state));
        }
    };
}
