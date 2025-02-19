package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.blocks.*;
import wtf.blocks.enums.AnimatedDecoType;
import wtf.blocks.enums.StaticDecoType;
import wtf.config.BlockEntry;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.fracturing.EntityFracture;
import wtf.init.JSONLoader;

public class ListenerBreakFracture {

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		ItemStack tool = player.getHeldItemMainhand();
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;

		if (!player.capabilities.isCreativeMode && !silk) {
			IBlockState state = event.getState();
			Block block = state.getBlock();

			if((block instanceof IDeco && (((IDeco) block).getType() != StaticDecoType.CRACKED && ((IDeco) block).getType() != AnimatedDecoType.LAVA_CRUST))) {
				state = ((AbstractBlockDerivative) event.getState().getBlock()).parentBackground;
				block = state.getBlock();
			}

			BlockEntry entry = JSONLoader.getEntryFromState(state);

			if (entry != null && !entry.getFracturedBlockId().isEmpty() && entry.fracturesFirstWhenMined()) {
				event.setCanceled(true);
				// TODO Use packets to send block break effect to every other player but the harvester.
				EntityFracture.fractureBlock(event.getWorld(), event.getPos(), false, true);

				if (ListenerHelper.isHammer(player.getHeldItemMainhand()) && WTFExpeditionConfig.modifyHammerBehaviour) {
					int toolLevel = tool == null ? 0 : tool.getItem().getHarvestLevel(tool, "pickaxe", player, state);
					EntityFracture.fractureHammer(event.getWorld(), event.getPos(), toolLevel);
				}

				if (tool != null) {
					tool.damageItem(1, player);
					player.addStat(StatList.getBlockStats(block));
					player.addExhaustion(0.025F);
				}
			}
		}
	}
}
