package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.WTFExpedition;
import wtf.blocks.*;
import wtf.enums.Modifier;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.fracturing.EntityFracture;
import wtf.init.BlockSets;
import wtf.network.WTFMessageBlockCrackEvent;

public class ListenerBreakFracture {

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		ItemStack tool = player.getHeldItemMainhand();
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;

		if (!player.capabilities.isCreativeMode && !silk) {
			IBlockState state = event.getState();
			Block block = state.getBlock();

			if(BlockSets.getTransformedState(state, Modifier.FRACTURED) == null)
				return;

			if(block instanceof IDeco) {
				state = ((AbstractBlockDerivative) event.getState().getBlock()).parentBackground;
				block = state.getBlock();
			}

			if (BlockSets.fractureWhenMinedBlocks.contains(state)) {
				event.setCanceled(true);
				EntityFracture.fractureBlock(event.getWorld(), event.getPos(), false, false);

				if(!event.getWorld().isRemote)
					for(EntityPlayer mplayer : event.getWorld().playerEntities)
						if(mplayer != player)
							WTFExpedition.CHANNEL_INSTANCE.sendTo(new WTFMessageBlockCrackEvent(Block.getStateId(state), event.getPos()), ((EntityPlayerMP) mplayer));

				if (tool.getItem().getRegistryName().toString().contains("tconstruct:hammer") && WTFExpeditionConfig.modifyHammerBehaviour) {
					int toolLevel = tool.getItem().getHarvestLevel(tool, "pickaxe", player, state);
					EntityFracture.fractureHammer(event.getWorld(), event.getPos(), toolLevel);
				}

				tool.damageItem(1, player);
				player.addStat(StatList.getBlockStats(block));
				player.addExhaustion(0.025F);
			}
		}
	}
}
