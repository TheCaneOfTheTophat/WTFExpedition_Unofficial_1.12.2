package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.blocks.AbstractBlockDerivative;
import wtf.config.BlockEntry;
import wtf.init.JSONLoader;

public class ListenerMiningSpeed {

	@SubscribeEvent
	public void breakSpeedEvent(BreakSpeed event) {
		IBlockState state = event.getState();
		Block block = state.getBlock();

		if(block instanceof AbstractBlockDerivative) {
			state = ((AbstractBlockDerivative) block).parentForeground;
		}

		BlockEntry entry = JSONLoader.getEntryFromState(state);

		if (!event.getEntityPlayer().capabilities.isCreativeMode && entry != null)
			event.setNewSpeed(entry.getPercentageMineSpeedModifier() * event.getOriginalSpeed());
	}
}
