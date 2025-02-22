package wtf.blocks;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.WTFExpedition;
import wtf.config.WTFExpeditionConfig;

public class BlockWTFTorch extends BlockTorch {

	public boolean lit;
	private BlockWTFTorch toggleBlock;
	
	public BlockWTFTorch(boolean lit) {
		super();
		this.setHardness(0.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(WTFExpedition.wtfTab);
		this.setLightLevel((lit ? 14 : 0) / 15F);
		this.lit = lit;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(lit ? Blocks.TORCH : this);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (lit && rand.nextInt(100) < WTFExpeditionConfig.torchLifespan) {
			if (!world.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), WTFExpeditionConfig.torchRange))
				world.setBlockState(pos, toggleBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)));
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!lit && (WTFExpeditionConfig.relightTorchByHand || playerIn.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL)) {
			worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return worldIn.setBlockState(pos, toggleBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)));
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
		if (lit)
			super.randomDisplayTick(state, worldIn, pos, rand);
	}

	public void setToggleBlock(BlockWTFTorch toggleBlock) {
		this.toggleBlock = toggleBlock;
	}
}
