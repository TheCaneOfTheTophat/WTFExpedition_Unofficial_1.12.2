package wtf.worldgen.caves.types;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.init.WTFContent;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;
import static wtf.worldgen.GenMethods.modify;

public class CaveTypeIcePermafrost extends AbstractCaveType {

    public CaveTypeIcePermafrost(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
        super(name, ceilingAddonPercentChance, floorAddonPercentChance);
    }

    @Override
    public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.1)
            replace(world, pos, Blocks.OBSIDIAN.getDefaultState());
        else if (noise > 0.9)
            replace(world, pos, Blocks.PACKED_ICE.getDefaultState());
        else if (noise > 0.75)
            modify(world, pos, Modifier.FRACTURED);
    }

    @Override
    public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.1)
            replace(world, pos, Blocks.OBSIDIAN.getDefaultState());
        else if (noise > 0.9)
            replace(world, pos, Blocks.PACKED_ICE.getDefaultState());
        else if (noise > 0.75)
            modify(world, pos, Modifier.FRACTURED);

        if (simplex.get3DNoiseScaled(world, pos,0.2) < 0.5)
            setPatch(world, pos, WTFContent.ice_patch.getDefaultState());
    }

    @Override
    public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos.up());

        if (noise < 0.2)
            setCeilingAddon(world, pos, Modifier.FRACTURED);
        else
            genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, true);
    }

    @Override
    public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos.down());

        if (noise < 0.2)
            setFloorAddon(world, pos, Modifier.FRACTURED);
        else
            genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, true);
    }

    @Override
    public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.1)
            replace(world, pos, Blocks.OBSIDIAN.getDefaultState());
        else if (noise > 0.9)
            replace(world, pos, Blocks.PACKED_ICE.getDefaultState());
        else if (noise < 0.4)
            modify(world, pos, Modifier.FRACTURED);
    }
}
