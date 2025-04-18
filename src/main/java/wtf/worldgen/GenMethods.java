package wtf.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
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
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.BlockFluidBase;
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

    public static boolean isPosInSurfaceStructure(World world, BlockPos pos) {
        if(!world.isRemote) {
            ChunkProviderServer cps = ((WorldServer) world).getChunkProvider();
            boolean inVillage = cps.isInsideStructure(world, "Village", pos);
            boolean inMansion = cps.isInsideStructure(world, "Mansion", pos);
            boolean inMonument = cps.isInsideStructure(world, "Monument", pos);
            boolean inTemple = cps.isInsideStructure(world, "Temple", pos);
            boolean inCity = cps.isInsideStructure(world, "EndCity", pos);

            return inVillage || inMansion || inMonument || inTemple || inCity;
        }

        return false;
    }

    public static boolean isNonSolid(IBlockState state) {
        Material material = state.getMaterial();

        return !state.isFullBlock() || isFluid(state) || material == Material.WATER || material == Material.SNOW || material == Material.LAVA || material == Material.AIR;
    }

    public static boolean isTreeReplaceable(World world, BlockPos pos, IBlockState state) {
        return state.getBlock().isReplaceable(world, pos) || state.getMaterial() == Material.PLANTS;
    }

    public static boolean isMaterialReplaceable(IBlockState state) {
        return BlockSets.replaceableMaterial.contains(state.getMaterial()) || isFluid(state);
    }

    public static boolean isFluid(IBlockState state) {
        return (state.getBlock() instanceof BlockFluidBase || state.getBlock() instanceof BlockLiquid);
    }

    public static boolean override(World world, BlockPos pos, IBlockState state, boolean update) {
        return world.setBlockState(pos, state, update ? 19 : 18);
    }

    public static boolean override(World world, BlockPos pos, IBlockState state) {
        return override(world, pos, state, true);
    }

    public static boolean replace(World world, BlockPos pos, IBlockState state) {
        IBlockState prevState = world.getBlockState(pos);
        Block prevBlock = prevState.getBlock();

        if(!state.isFullBlock() && isFluid(prevState))
            return false;

        if(!prevBlock.hasTileEntity(state) && ((isMaterialReplaceable(prevState)) || prevBlock.isReplaceable(world, pos)) && !(prevBlock == Blocks.FARMLAND) && !(prevState.getBlockHardness(world, pos) == -1.0F))
            return override(world, pos, state, false);

        return false;
    }

    public static boolean modify(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.getTransformedState(world.getBlockState(pos), modifier);

        if(modState != null)
            return override(world, pos, modState, false);

        return false;
    }

    public static void setOre(World world, BlockPos pos, IBlockState oreState, int density) {
        if(oreState.getBlock() instanceof DummyModifierBlock) {
            modify(world, pos, ((DummyModifierBlock) oreState.getBlock()).modifier);
            return;
        }

        IBlockState denseOreState = BlockSets.stoneAndOre.get(new StoneAndOre(world.getBlockState(pos), oreState));

        if (denseOreState != null) {
            if (denseOreState.getBlock() instanceof BlockDenseOre)
                denseOreState = denseOreState.withProperty(BlockDenseOre.DENSITY, density);
            else if (denseOreState.getBlock() instanceof BlockDenseOreFalling)
                denseOreState = denseOreState.withProperty(BlockDenseOreFalling.DENSITY, density);

            override(world, pos, denseOreState);
        }
    }

    public static boolean setFloorAddon(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.blockTransformer.get(Pair.of(world.getBlockState(pos.down()), modifier));

        if (modState != null && world.isAirBlock(pos))
            return replace(world, pos, modState);

        return false;
    }

    public static boolean setCeilingAddon(World world, BlockPos pos, Modifier modifier) {
        IBlockState modState = BlockSets.blockTransformer.get(Pair.of(world.getBlockState(pos.up()), modifier));

        if (modState != null && world.isAirBlock(pos))
            return replace(world, pos, modState);

        return false;
    }

    public static void setPatch(World world, BlockPos pos, IBlockState patch) {
        IBlockState state = world.getBlockState(pos);
        Material material = state.getMaterial();

        if (world.isAirBlock(pos.up()) && state.isFullBlock() && !isFluid(state) && !(material == Material.SNOW || material == Material.ICE || material == Material.PACKED_ICE || material == Material.AIR || material == Material.GRASS))
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
            speleothem = BlockSets.speleothemMap.get(below);
        } else if (!world.isAirBlock(pos.up()) && world.isAirBlock(pos.down())){
            direction = -1;
            speleothem = BlockSets.speleothemMap.get(above);
        } else
            return false;

        if(isFluid(world.getBlockState(pos)))
            return false;

        if (speleothem == null) {
            if (direction == -1) {
                if (above.getMaterial() == Material.GROUND && depth > 0.7) {
                    genRoots(world, pos);
                    return true;
                } else if (frozen || above.getMaterial() == Material.ICE || above.getMaterial() == Material.PACKED_ICE) {
                    genIcicle(world, pos);
                    return true;
                } else if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.FOREST)) {
                    boolean aboveWoodAndSolid = above.getMaterial() == Material.WOOD && above.isSideSolid(world, pos.up(), EnumFacing.DOWN);

                    if (aboveWoodAndSolid)
                        return replace(world, pos, WTFContent.foxfire.getDefaultState().withProperty(BlockFoxfire.HANGING, true));
                }
            } else if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.FOREST)) {
                boolean belowWoodAndSolid = below.getMaterial() == Material.WOOD && below.isSideSolid(world, pos.down(), EnumFacing.UP);

                if (belowWoodAndSolid)
                    return replace(world, pos, WTFContent.foxfire.getDefaultState());
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
                else if (next == speleothem.parentBackground) {
                    set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalactite_base) : speleothem.getBlockState(SpeleothemType.stalagmite_base);
                    remaining = 0;
                } else {
                    set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
                    remaining = 0;
                }
            } else {
                set = direction == 1 ? speleothem.getBlockState(SpeleothemType.stalagmite_tip) : speleothem.getBlockState(SpeleothemType.stalactite_tip);
                remaining = 0;
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

    public static boolean genVines(World world, BlockPos pos, EnumFacing facing) {
        if (!world.isAirBlock(pos))
            return false;

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

       return replace(world, pos, block);
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
            nbt.setShort("MaxSpawnDelay", (short) 400);
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
}
