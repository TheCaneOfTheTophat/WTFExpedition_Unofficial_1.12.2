package wtf.blocks;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.WTFExpedition;
import wtf.gameplay.fracturing.EntityFracture;
import wtf.init.BlockSets;

public class BlockDecoStatic extends AbstractBlockDerivative {
	private final StaticDecoType type;

	public static ArrayList<BlockDecoStatic> crackedList = new ArrayList<BlockDecoStatic>();

	public BlockDecoStatic(IBlockState state, StaticDecoType type) {
		super(state, state);
		this.type = type;
//		if (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GROUND){
//			BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.MOSSY), this.getDefaultState());
//			IBlockState cobble = BlockSets.getTransformedState(state, Modifier.COBBLE);
//			if (cobble != null){
//				IBlockState mossyCobble = BlockSets.getTransformedState(cobble, Modifier.MOSSY);
//				if (mossyCobble != null){
//					BlockSets.blockTransformer.put(new StateAndModifier(this.getDefaultState(), Modifier.COBBLE), mossyCobble);
//				}
//			}
//		}
//			BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.SOUL), this.getDefaultState().withProperty(TYPE, StaticDecoType.SOUL));
//		if (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.ICE){
//			BlockSets.blockTransformer.put(new StateAndModifier(state, BlockSets.Modifier.CRACKED), this.getDefaultState().withProperty(TYPE, StaticDecoType.CRACKED));
//		}
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
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		if(layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT)
			return true;
		return false;
	}

	public StaticDecoType getType() {
		return type;
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		ItemStack blockStack = new ItemStack(parentForeground.getBlock(), 1, parentForeground.getBlock().getMetaFromState(parentForeground));
		return I18n.format(WTFExpedition.modID + ":deco_type." + getType().getName() + ".name") + " " + blockStack.getDisplayName();
	}

	public enum StaticDecoType implements IStringSerializable {
		MOSS("mossy", BlockSets.Modifier.MOSSY, "moss_overlay"),
		SOUL("soul", BlockSets.Modifier.SOUL, "soul_overlay"),
		CRACKED("cracked", BlockSets.Modifier.CRACKED, "cracked_overlay");


		private final String name;
		public final BlockSets.Modifier modifier;
        private final String overlayName;

        StaticDecoType(String name, BlockSets.Modifier mod, String overlayName) {
			this.name = name;
			this.modifier = mod;
            this.overlayName = overlayName;
        }

		@Override
		public String getName() {
			return name;
		}

		public String getOverlayName() {
			return overlayName;
		}

		@Override
		public String toString() {
			return getName();
		}
    }
}
