package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.enums.Modifier;
import wtf.init.BlockSets;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonCursedArmor extends AbstractDungeonType {

    public DungeonCursedArmor(String name) {
        super(name, 0, 3);
    }

    IBlockState block = null;

    @Override
    public boolean canGenerateAt(World world, CaveListWrapper cave) {
        block = BlockSets.getTransformedState(getBlockStateCompatible(world, cave.centerPos.getFloorPos()), Modifier.BRICK);

        if (block == null)
            block = Blocks.STONEBRICK.getDefaultState();

        return true;
    }

    @Override
    public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
        spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID, "cursed_armor"), 5);
        override(world, pos.getFloorPos().up(2), Blocks.STANDING_BANNER.getDefaultState(), false);
    }

    @Override
    public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
        generate(world, pos);
    }

    @Override
    public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
        generate(world, pos);
    }

    @Override
    public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {}

    @Override
    public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
        EntityArmorStand stand = new EntityArmorStand(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

        stand.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        stand.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        stand.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
        stand.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));

        stand.rotationYaw = -180 + (rand.nextFloat() * 360);

        world.spawnEntity(stand);
    }

    @Override
    public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {}

    public void generate(World world, BlockPos pos) {
        replace(world, pos, block);
    }
}

