package wtf.worldgen.replacers;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.init.WTFContent;

import static wtf.worldgen.GenMethods.*;

public class TorchReplacer extends Replacer {

    public TorchReplacer() {
        super(Blocks.TORCH);
    }

    @Override
    public boolean replace(World world, BlockPos pos, IBlockState oldState) {
        return override(world, pos, WTFContent.lit_torch.getDefaultState().withProperty(BlockTorch.FACING, oldState.getValue(BlockTorch.FACING)), true);
    }
}
