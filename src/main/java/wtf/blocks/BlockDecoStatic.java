package wtf.blocks;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.enums.StaticDecoType;
import wtf.gameplay.fracturing.EntityFracture;

public class BlockDecoStatic extends AbstractBlockDerivative implements IDeco {
	private final StaticDecoType type;

	public static ArrayList<BlockDecoStatic> crackedList = new ArrayList<>();

	public BlockDecoStatic(IBlockState state, StaticDecoType type) {
		super(state, state);
		this.type = type;
//		if (BlockSets.fallingBlocks.containsKey(this.parentBackground)){
//			BlockSets.fallingBlocks.put(this, BlockSets.fallingBlocks.get(this.parentBackground));
//		}
//
//		//BlockSets.blockMiningSpeed
//		if (BlockSets.fallingBlocks.containsKey(state.getBlock())){
//			BlockSets.fallingBlocks.put(this, BlockSets.fallingBlocks.get(state.getBlock()));
//		}
//		BlockSets.ReplaceHashset.add(this);
	//	OreDictionary.registerOre("moss", new ItemStack(this, 1, 0));
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote && type == StaticDecoType.CRACKED)
			EntityFracture.fractureAdjacent(world, pos);
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack blockStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
		return I18n.format(WTFExpedition.modID + ":deco_type." + getType().getName() + ".name") + " " + blockStack.getDisplayName();
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

	public StaticDecoType getType() {
		return type;
	}
}
