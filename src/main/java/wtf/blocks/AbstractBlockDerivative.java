package wtf.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class AbstractBlockDerivative extends Block {
	
	public final IBlockState parentBackground;
	public final IBlockState parentForeground;

    /**
	 * This class is used to create blocks whose properties are derivative of other blocks
	 * backState is used to determine the properties of the block
	 * foreState is used to determine a block's harvesting and drops
	 * they can be the same block, for simple blocks, or more different for things like ores, with an ore and a background stone
	 * @param backState
	 * @param foreState
	 */

	public AbstractBlockDerivative(IBlockState backState, IBlockState foreState) {
		super(backState.getMaterial());
		this.parentBackground = backState;
		this.parentForeground = foreState;
		this.setHarvestLevel(foreState.getBlock().getHarvestTool(foreState), foreState.getBlock().getHarvestLevel(foreState));
	}

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return parentForeground.getBlock().getItemDropped(parentForeground, rand, fortune);
    }

    @Override
    public int quantityDropped(Random random) {
        return parentForeground.getBlock().quantityDropped(random);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return parentForeground.getBlock().quantityDroppedWithBonus(fortune, random);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return parentForeground.getBlock().canSilkHarvest(world, pos, parentForeground, player);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            List<ItemStack> items = new ArrayList<ItemStack>();
            int meta = this.parentForeground.getBlock().getMetaFromState(this.parentForeground);
            ItemStack itemstack = new ItemStack(this.parentForeground.getBlock(),1, meta);

            if (itemstack != null)
                items.add(itemstack);

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack item : items)
                spawnAsEntity(worldIn, pos, item);

        }
        else {
            harvesters.set(player);
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItem(worldIn, pos, parentForeground, i);
            harvesters.set(null);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;

        int count = quantityDropped(parentForeground, fortune, rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(parentForeground, rand, fortune);
            if (item != Items.AIR)
                drops.add(new ItemStack(item, 1, parentForeground.getBlock().damageDropped(parentForeground)));
        }
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        return this.parentForeground.getBlock().getExpDrop(state, world, pos, fortune);
    }
	
    @Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return parentBackground.getBlockHardness(worldIn, pos);
    }
	
    @Override
	public float getExplosionResistance(Entity exploder) {
        return parentBackground.getBlock().getExplosionResistance(exploder) / 2.5F;
    }

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return this.parentBackground.getBlock().canDropFromExplosion(explosionIn);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return parentBackground.getBlock().getFlammability(world, pos, face);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
        return parentBackground.getBlock().canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    public SoundType getSoundType() {
        return parentBackground.getBlock().getSoundType();
    }

    public String getDisplayName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return parentBackground.getBlock().getBlockLayer();
    }
}
