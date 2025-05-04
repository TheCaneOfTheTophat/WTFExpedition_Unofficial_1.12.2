package wtf.worldgen.caves.types;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;
import static wtf.worldgen.GenMethods.modify;

public class CaveTypeHypervolcanic extends AbstractCaveType {

    public CaveTypeHypervolcanic(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
        super(name, ceilingAddonPercentChance, floorAddonPercentChance);
    }

    @Override
    public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.5)
            modify(world, pos, Modifier.LAVA_CRUST);
        else if (noise > 0.6)
            modify(world, pos, Modifier.LAVA_DRIPPING);
        else
            modify(world, pos, Modifier.FRACTURED);
    }

    @Override
    public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);
        boolean lava = true;

        if(isPosInMineshaft(world, pos.up()))
            lava = false;

        if(world.getBlockState(pos.up()).getMaterial() == Material.LAVA)
            lava = false;

        if(world.getBlockState(pos).getMaterial() != Material.ROCK && world.getBlockState(pos).getMaterial() != Material.GROUND)
            lava = false;

        for(BlockPos boxPos : BlockPos.getAllInBoxMutable(pos.add(2, 1, 2), pos.add(-2, 1, -2)))
            if(world.getBlockState(boxPos).getBlock().hasTileEntity(world.getBlockState(boxPos)))
                lava = false;

        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState state = world.getBlockState(pos.offset(facing));

            if(!state.isFullBlock() && state.getMaterial() != Material.LAVA)
                lava = false;
        }

        BlockPos modPos = pos;

        if(lava) {
            replace(world, pos, Blocks.LAVA.getDefaultState(), true);
            modPos = pos.down();
        }

        if (noise < 0.5)
            modify(world, modPos, Modifier.LAVA_CRUST);
        else if (noise < 0.6 && noise > 0.5)
            modify(world, modPos, Modifier.FRACTURED);
    }

    @Override
    public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

    @Override
    public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {}

    @Override
    public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.5)
            modify(world, pos, Modifier.LAVA_CRUST);
        else if (noise < 0.6 && noise > 0.5)
            modify(world, pos, Modifier.FRACTURED);
    }
}
