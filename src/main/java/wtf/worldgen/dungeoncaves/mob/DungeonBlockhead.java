package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.replace;
import static wtf.worldgen.GenMethods.spawnVanillaSpawner;

public class DungeonBlockhead extends AbstractDungeonType {

    public DungeonBlockhead(String name) {
        super(name, 0, 30);
    }

    @Override
    public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

    @Override
    public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
        replace(world, pos, Blocks.GRASS.getDefaultState());
    }

    @Override
    public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
        spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID,"blockhead"), 2);
    }

    @Override
    public boolean canGenerateAt(World world, CaveListWrapper cave) {
        return true;
    }

    @Override
    public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {}

    @Override
    public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
        if(world.getBlockState(pos.down()) == Blocks.GRASS.getDefaultState())
            replace(world, pos, rand.nextFloat() > 0.8 ? Blocks.RED_FLOWER.getDefaultState() : Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), true);
    }

    @Override
    public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}
}
