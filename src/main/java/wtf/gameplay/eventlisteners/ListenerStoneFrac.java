package wtf.gameplay.eventlisteners;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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
import wtf.gameplay.GravityMethods;
import wtf.gameplay.fracturing.EntityStoneCrack;
import wtf.init.BlockSets;
import wtf.init.BlockSets.Modifier;
import wtf.init.JSONLoader;

public class ListenerStoneFrac {

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event)
	{
		ItemStack tool = event.getPlayer().getHeldItemMainhand();
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;

		
		if (!event.getPlayer().capabilities.isCreativeMode && !silk){

			

			BlockEntry entry = JSONLoader.identifierToBlockEntry.get(event.getState().getBlock().getRegistryName() + "@" + event.getState().getBlock().getMetaFromState(event.getState()));
			if (entry != null && entry.fracturesFirstWhenMined())	{
				
				event.setCanceled(true);
				doFrac(event.getWorld(), event.getPos(), event.getState());

				if (ListenerHelper.isHammer(event.getPlayer().getHeldItemMainhand()) && WTFExpeditionConfig.modifyHammerBehaviour){
					int toolLevel = tool == null ? 0 : tool.getItem().getHarvestLevel(tool, "pickaxe", event.getPlayer(), event.getState());
					EntityStoneCrack.cracHammer(event.getWorld(), event.getPos(), toolLevel);
				}

				if (tool != null){
					// Unsure about this
					tool.attemptDamageItem(1, event.getWorld().rand, null);
					event.getPlayer().addStat(StatList.getBlockStats(event.getState().getBlock()));
					event.getPlayer().addExhaustion(0.025F);
				}
			}
		}
	}

	public static void doFrac(World world, BlockPos pos, IBlockState state){

		IBlockState cobble = BlockSets.getTransformedState(state, Modifier.COBBLE); //if it can be fractured

		if (state.getBlock() instanceof BlockDecoAnim && ((BlockDecoAnim) state.getBlock()).getType() == BlockDecoAnim.AnimatedDecoType.LAVA_CRUST){
			//if lava crust, frac to lava
			world.setBlockState(pos, Blocks.LAVA.getDefaultState());
		}
		else {
			//if has cobble, then frac and drop the block
			world.setBlockState(pos, cobble);
			if (WTFExpeditionConfig.additionalBlockGravity){
				GravityMethods.dropBlock(world, pos, true);
			}
		}
	}

}
