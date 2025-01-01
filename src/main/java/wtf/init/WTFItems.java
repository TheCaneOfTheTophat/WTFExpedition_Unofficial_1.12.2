package wtf.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.*;
import net.minecraftforge.registries.IForgeRegistry;
import wtf.Core;
import wtf.client.WTFModels;
import wtf.config.GameplayConfig;
import wtf.config.MasterConfig;
import wtf.items.HomeScroll;
import wtf.items.ItemBlockState;

import java.util.ArrayList;

@Mod.EventBusSubscriber
@ObjectHolder(Core.coreID)
public class WTFItems {
	public static final Item sulfur = null;
	public static final Item nitre = null;
	public static final Item home_scroll = null;

	public static ArrayList<Item> items = new ArrayList<>();

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registerItem(registry, new Item(), "nitre");
		registerItem(registry, new Item(), "sulfur");

		if(MasterConfig.gameplaytweaks && GameplayConfig.homescroll)
			registerItem(registry, new HomeScroll(), "home_scroll");

		for(Block block : WTFBlocks.blocks) {
			if(WTFModels.metaMap.get(block.getRegistryName()) != null)
				registerItemBlock(registry, new ItemBlockState(block));
			else
				registerItemBlock(registry, new ItemBlock(block));
		}
	}

	public static <T extends Item> Item registerItem(IForgeRegistry<Item> registry, T item, String name) {
		item.setCreativeTab(Core.wtfTab);
		item.setRegistryName(Core.coreID, name);
		item.setUnlocalizedName(item.getRegistryName().toString());

		items.add(item);

		registry.register(item);
		return item;
	}

	public static <T extends ItemBlock> ItemBlock registerItemBlock(IForgeRegistry<Item> registry, T itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
		itemBlock.setHasSubtypes(true);
		itemBlock.setMaxDamage(0);

		items.add(itemBlock);

		registry.register(itemBlock);
		return itemBlock;
	}
}
