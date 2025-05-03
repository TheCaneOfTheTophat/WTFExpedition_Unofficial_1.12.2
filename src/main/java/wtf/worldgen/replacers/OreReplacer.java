package wtf.worldgen.replacers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.blocks.ores.BlockDenseOre;
import wtf.blocks.ores.BlockDenseOreFalling;
import wtf.init.BlockSets;
import wtf.utilities.wrappers.StoneAndOre;

import static wtf.worldgen.GenMethods.*;

public class OreReplacer extends Replacer {

    public OreReplacer(Block block) {
        super(block);
    }

    @Override
    public boolean replace(World world, BlockPos pos, IBlockState oldState) {
        // TODO Needs UBC compatibility here

        IBlockState stone = BlockSets.oresDefaultStone.get(oldState);
        IBlockState newOreState = BlockSets.stoneAndOre.get(new StoneAndOre((WTFExpedition.UBC && stone.getBlock() == Blocks.STONE) ? getUBCStone(world, pos) : stone, oldState));

        if (newOreState != null) {
            if (newOreState.getBlock() instanceof BlockDenseOre)
                newOreState = newOreState.withProperty(BlockDenseOre.DENSITY, 2);
            else if (newOreState.getBlock() instanceof BlockDenseOreFalling)
                newOreState = newOreState.withProperty(BlockDenseOreFalling.DENSITY, 2);

            return override(world, pos, newOreState, false);
        }

        return false;
    }
}
