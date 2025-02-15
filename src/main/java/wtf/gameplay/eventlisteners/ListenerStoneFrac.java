package wtf.gameplay.eventlisteners;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.blocks.BlockDecoAnim;
import wtf.config.BlockEntry;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.fracturing.EntityStoneCrack;
import wtf.init.JSONLoader;

public class ListenerStoneFrac {

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		ItemStack tool = player.getHeldItemMainhand();
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;

		if (!player.capabilities.isCreativeMode && !silk) {
			IBlockState state = event.getState();
			BlockEntry entry = JSONLoader.getEntryFromState(state);

			if (entry != null && !entry.getFracturedBlockId().isEmpty() && entry.fracturesFirstWhenMined()) {
				event.setCanceled(true);
				fracture(event.getWorld(), event.getPos(), state);

				if (ListenerHelper.isHammer(player.getHeldItemMainhand()) && WTFExpeditionConfig.modifyHammerBehaviour) {
					int toolLevel = tool == null ? 0 : tool.getItem().getHarvestLevel(tool, "pickaxe", event.getPlayer(), state);
					EntityStoneCrack.cracHammer(event.getWorld(), event.getPos(), toolLevel);
				}

				if (tool != null) {
					tool.damageItem(1, player);
					player.addStat(StatList.getBlockStats(state.getBlock()));
					player.addExhaustion(0.025F);
				}
			}
		}
	}

	public static void fracture(World world, BlockPos pos, IBlockState state) {
		BlockEntry entry = JSONLoader.getEntryFromState(state);

		if (state.getBlock() instanceof BlockDecoAnim && ((BlockDecoAnim) state.getBlock()).getType() == BlockDecoAnim.AnimatedDecoType.LAVA_CRUST) {
			world.setBlockState(pos, Blocks.LAVA.getDefaultState());
		}
		else {
			world.setBlockState(pos, JSONLoader.getStateFromId(entry.getFracturedBlockId()));
			if (WTFExpeditionConfig.additionalBlockGravity) {
				// GravityMethods.dropBlock(world, pos, true);
			}
		}
	}
}
