package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.fracturing.EntityFracture;
import wtf.init.BlockSets;

public class ListenerOreFracture {

	@SubscribeEvent
	public void rightClick(RightClickBlock event) {
		if (!event.getEntityPlayer().capabilities.isCreativeMode && WTFExpeditionConfig.preventOrePlacement) {
			Item item = event.getItemStack().getItem();

			if(item instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(event.getItemStack().getItem());
				if (BlockSets.adjacentFracturingBlocks.contains(block.getStateFromMeta(item.getMetadata(event.getItemStack()))))
					event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event) {
		ItemStack tool = event.getPlayer().getHeldItemMainhand();
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
		
		if (!event.getPlayer().capabilities.isCreativeMode && !silk) {
			if (BlockSets.adjacentFracturingBlocks.contains(event.getState())) {
				if (WTFExpeditionConfig.simpleFracturing)
					EntityFracture.fractureAdjacentSimple(event.getWorld(), event.getPos());
				else {
					int toolLevel = tool.getItem().getHarvestLevel(tool, "pickaxe", event.getPlayer(), event.getState());
					EntityFracture.fractureOre(event.getWorld(), event.getPos(), toolLevel);
				}
			}
		}
	}
}
