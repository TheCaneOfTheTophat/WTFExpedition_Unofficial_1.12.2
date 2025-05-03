package wtf.worldgen.trees.types;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static wtf.worldgen.GenMethods.*;

public class Shrub {
    private final IBlockState leaves;
    private final IBlockState wood;
	
	public Shrub(WorldGenShrub oldshrub) {
		this.leaves = ObfuscationReflectionHelper.getPrivateValue(WorldGenShrub.class, oldshrub, "field_150528_a");
		this.wood = ObfuscationReflectionHelper.getPrivateValue(WorldGenShrub.class, oldshrub, "field_150527_b");
	}
	
	public boolean generate(World worldIn, Random rand, BlockPos position) {
       if (position == null)
    	   return false;
		
       IBlockState state = worldIn.getBlockState(position);

        if (state.getBlock().canSustainPlant(state, worldIn, position, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING))) {
            position = position.up();
            replace(worldIn, position, wood);
            //this.setBlockAndNotifyAdequately(worldIn, position, this.woodMetadata);

            for (int i = position.getY(); i <= position.getY() + 2; ++i) {
                int j = i - position.getY();
                int k = 2 - j;

                for (int l = position.getX() - k; l <= position.getX() + k; ++l) {
                    int i1 = l - position.getX();

                    for (int j1 = position.getZ() - k; j1 <= position.getZ() + k; ++j1) {
                        int k1 = j1 - position.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0) {
                            BlockPos blockpos = new BlockPos(l, i, j1);
                           
                            // this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                            replace(worldIn, blockpos, leaves);
                        }
                    }
                }
            }
        }
        return true;
    }
}
