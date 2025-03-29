package wtf.worldgen;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.lang3.tuple.Pair;
import wtf.blocks.*;
import wtf.config.WTFExpeditionConfig;
import wtf.enums.IcicleType;
import wtf.enums.Modifier;
import wtf.enums.SpeleothemType;
import wtf.init.BlockSets;
import wtf.init.WTFContent;
import wtf.utilities.wrappers.StoneAndOre;

public class GenMethods {

    public static boolean override(World world, BlockPos pos, IBlockState state, boolean update) {
        return world.setBlockState(pos, state, update ? 19 : 18);
    }

    public static boolean override(World world, BlockPos pos, IBlockState state) {
        return override(world, pos, state, true);
    }

    public static boolean replace(World world, BlockPos pos, IBlockState state) {
        IBlockState prevState = world.getBlockState(pos);
        Material material = prevState.getMaterial();

        if(!prevState.getBlock().hasTileEntity(state) &&
          ((material == Material.ROCK || material == Material.GROUND || material == Material.SAND || material == Material.PLANTS || material == Material.AIR)
          || prevState.getBlock().isReplaceable(world, pos)) && !(state.getBlock() == Blocks.FARMLAND) && !(prevState.getBlockHardness(world, pos) == -1.0F))
            return override(world, pos, state, false);

        return false;
    }

    public static boolean modify(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.getTransformedState(world.getBlockState(pos), modifier);

        if(modState != null)
            return override(world, pos, modState, false);

        return false;
    }

    public static boolean setOre(World world, BlockPos pos, IBlockState oreState, int density) {
        IBlockState denseOreState = BlockSets.stoneAndOre.get(new StoneAndOre(world.getBlockState(pos), oreState));

        if (denseOreState != null) {
            if (denseOreState.getBlock() instanceof BlockDenseOre)
                denseOreState = denseOreState.withProperty(BlockDenseOre.DENSITY, density);
            else if (denseOreState.getBlock() instanceof BlockDenseOreFalling)
                denseOreState = denseOreState.withProperty(BlockDenseOreFalling.DENSITY, density);

            return override(world, pos, denseOreState);
        }

        return false;
    }

    public static boolean setFloorAddon(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.blockTransformer.get(Pair.of(world.getBlockState(pos.down()), modifier));

        if (modState != null)
            return replace(world, pos, modState);

        return false;
    }

    public static boolean setCeilingAddon(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.blockTransformer.get(Pair.of(world.getBlockState(pos.up()), modifier));

        if (modState != null)
            return replace(world, pos, modState);

        return false;
    }

    public static void setPatch(World world, BlockPos pos, IBlockState patch) {
        Material material = world.getBlockState(pos).getMaterial();

        if (world.isAirBlock(pos.up()) && !(material == Material.SNOW || material == Material.ICE || material == Material.PACKED_ICE || material == Material.LAVA || material == Material.WATER || material == Material.AIR || material == Material.GRASS))
            replace(world, pos.up(), patch);
    }

    public static void setWaterPatch(World world, BlockPos pos) {
        if (WTFExpeditionConfig.enablePuddles)
            setPatch(world, pos, WTFContent.puddle.getDefaultState());
    }

    public static void genFloatingStone(World world, BlockPos pos) {
        // TODO UBC compat
        replace(world, pos, Blocks.STONE.getDefaultState());
    }

    public static boolean genSpeleothem(World world, BlockPos pos, int size, float depth, boolean frozen) {
        if (frozen && depth > 0.9) {
            genIcicle(world, pos);
            return true;
        }

        IBlockState above = world.getBlockState(pos.up());
        IBlockState below = world.getBlockState(pos.down());

        int remaining = size;
        int direction;
        BlockSpeleothem speleothem;

        if (world.isAirBlock(pos.up()) && !world.isAirBlock(pos.down())){
            direction = 1;
            speleothem = WTFContent.speleothemMap.get(below);
        } else if (!world.isAirBlock(pos.up()) && world.isAirBlock(pos.down())){
            direction = -1;
            speleothem = WTFContent.speleothemMap.get(above);
        } else
            return false;

        if (speleothem == null) {
            if (direction == -1) {
                if (above.getMaterial() == Material.GROUND && depth > 0.7) {
                    genRoots(world, pos);
                    return true;
                } else if (frozen || above.getMaterial() == Material.ICE || above.getMaterial() == Material.PACKED_ICE) {
                    genIcicle(world, pos);
                    return true;
                } else if (above.getMaterial() == Material.WOOD && BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.FOREST))
                    return true;
                //hanging glow shrooms
            }

            return false;
        }

        if (depth > 1)
            return false;

        if (frozen)
            speleothem = speleothem.frozen;

        while (remaining > 0) {
            IBlockState next = world.getBlockState(pos.up(direction));
            IBlockState set;

            if (remaining == size) {
                if (world.isAirBlock(pos.up(direction))) {
                    if (size > 1)
                        set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_base) : speleothem.getBlockState(SpeleothemType.stalactite_base);
                    else
                        set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_small) : speleothem.getBlockState(SpeleothemType.stalactite_small);
                } else
                    return false;
            } else if (remaining > 1) {
                if (world.isAirBlock(pos.up(direction)))
                    set = speleothem.getBlockState(SpeleothemType.speleothem_column);
                else if (next.hashCode() == speleothem.parentBackground.hashCode()) {
                    set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalactite_base) : speleothem.getBlockState(SpeleothemType.stalagmite_base);
                    remaining = 0;
                } else
                    set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
            } else {
                set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
            }

            replace(world, pos, set);
            pos = pos.up(direction);
            remaining--;
        }

        return true;
    }

    public static void genIcicle(World world, BlockPos pos) {
        if (world.rand.nextBoolean() && world.isAirBlock(pos.down())) {
            replace(world, pos, WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_base));
            replace(world, pos.down(), WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_tip));
        } else
            replace(world, pos, WTFContent.icicle.getDefaultState().withProperty(BlockIcicle.TYPE, IcicleType.icicle_small));
    }

    public static void genVines(World world, BlockPos pos, EnumFacing facing) {
        if (!world.isAirBlock(pos))
            return;

        IBlockState block = null;

        switch(facing) {
            case EAST:
                block = Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST,  true);
                break;
            case NORTH:
                block = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH,  true);
                break;
            case SOUTH:
                block = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH,  true);
                break;
            case WEST:
                block = Blocks.VINE.getDefaultState().withProperty(BlockVine.WEST,  true);
                break;
        }

        replace(world, pos, block);
    }

    public static void genRoots(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS))
            replace(world, pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.SPRUCE));
        else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA))
            replace(world, pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.ACACIA));
        else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE))
            replace(world, pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.JUNGLE));
        else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPOOKY))
            replace(world, pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.DARK_OAK));
        else if (biome.getBiomeName().contains("birch"))
            replace(world, pos, WTFContent.roots.getDefaultState().withProperty(BlockRoots.VARIANT, BlockPlanks.EnumType.BIRCH));
        else
            replace(world, pos, WTFContent.roots.getDefaultState());
    }

    public static void spawnVanillaSpawner(World world, BlockPos pos, ResourceLocation entityName, int count) {
        override(world, pos, Blocks.MOB_SPAWNER.getDefaultState(), false);

        TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);

        if (spawner != null) {
            spawner.getSpawnerBaseLogic().setEntityId(entityName);
            NBTTagCompound nbt = new NBTTagCompound();
            spawner.writeToNBT(nbt);

            nbt.setShort("Delay", (short) 20);
            nbt.setShort("MinSpawnDelay", (short) 200);
            nbt.setShort("MaxSpawnDelay", (short) 800);
            nbt.setShort("SpawnCount",(short) count);
            nbt.setShort("MaxNearbyEntities", (short) 6);
            nbt.setShort("RequiredPlayerRange", (short) 16);
            nbt.setShort("SpawnRange", (short) 2);

            spawner.readFromNBT(nbt);
        }
    }

    public static int getMaxY(Chunk chunk) {
        int index = 0;
        int maxY = 0;

        for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
            index++;

            if (extendedblockstorage != null)
                maxY = index;
        }

        return maxY * 16;
    }

    public static boolean setTreeBlock(BlockPos pos, IBlockState state) {
        return false; //blockmap.add(pos, new QTreeReplace(pos, state));
    }
}
