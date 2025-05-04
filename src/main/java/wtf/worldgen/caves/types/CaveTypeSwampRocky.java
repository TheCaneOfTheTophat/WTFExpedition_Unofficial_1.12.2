package wtf.worldgen.caves.types;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.AdjPos;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeSwampRocky extends AbstractCaveType {

    public CaveTypeSwampRocky(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
        super(name, ceilingAddonPercentChance, floorAddonPercentChance);
    }

    @Override
    public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);

        if (rand.nextFloat() < 0.15)
            modify(world, pos, Modifier.WET);
        else {
            if (noise < 0.05)
                modify(world, pos, Modifier.CRACKED);
            else if (noise > 0.75)
                modify(world, pos, Modifier.FRACTURED);

            if (noise - 0.5 > depth)
                modify(world, pos, Modifier.MOSS);
        }
    }

    @Override
    public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
        double noise = simplex.get3DNoise(world, pos);

        if (simplex.get3DNoiseScaled(world, pos, 0.5) < 0.2)
            setWaterPatch(world, pos);

        if (noise < 0.05)
            modify(world, pos, Modifier.CRACKED);
        else if (noise > 0.75)
            modify(world, pos, Modifier.FRACTURED);

        if (noise - 0.5 > depth)
            modify(world, pos, Modifier.MOSS);
    }

    @Override
    public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
        if (rand.nextBoolean() && setCeilingAddon(world, pos, Modifier.FRACTURED)) {
            for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                boolean cancel;

                for (int loop = rand.nextInt(3) + 1; loop > -1; loop--) {
                    cancel = !genVines(world, pos.offset(facing.getOpposite()).down(loop), facing);

                    if (cancel)
                        break;
                }
            }
        } else
            genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
    }

    @Override
    public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
        if (rand.nextBoolean() && setFloorAddon(world, pos, Modifier.FRACTURED)) {
            for(EnumFacing facing : EnumFacing.HORIZONTALS)
                genVines(world, pos.offset(facing.getOpposite()), facing);
        } else
            genSpeleothem(world, pos, getSpeleothemSize(rand, depth), depth, false);
    }


    @Override
    public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
        double noise = simplex.get3DNoise(world, pos);

        if (noise < 0.05)
            modify(world, pos, Modifier.CRACKED);
        else if (noise > 0.75)
            modify(world, pos, Modifier.FRACTURED);

        if (noise - 0.5 > depth)
            modify(world, pos, Modifier.MOSS);

    }

    @Override
    public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
        if (simplex.get3DNoise(world, pos) > depth * 2)
            genVines(world, pos, pos.getFace(rand));
    }
}
