package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.blocks.AbstractBlockDerivative;
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;
import wtf.enums.AnimatedDecoType;
import wtf.enums.StaticDecoType;
import wtf.init.BlockSets;

public class ListenerMiningSpeed {

	@SubscribeEvent
	public void breakSpeedEvent(BreakSpeed event) {
		IBlockState state = event.getState();
		Block block = state.getBlock();

		if(block instanceof AbstractBlockDerivative) {
			state = ((AbstractBlockDerivative) block).parentForeground;

			if(block instanceof BlockDecoStatic && ((BlockDecoStatic) block).getType() == StaticDecoType.CRACKED)
				return;

			if(block instanceof BlockDecoAnim && ((BlockDecoAnim) block).getType() == AnimatedDecoType.LAVA_CRUST)
				return;
		}

		float modifier = BlockSets.miningSpeedModifierMap.get(state) == null ? -1 : BlockSets.miningSpeedModifierMap.get(state);

		if (!event.getEntityPlayer().capabilities.isCreativeMode && modifier != -1)
			event.setNewSpeed(modifier * event.getOriginalSpeed());
	}
}
