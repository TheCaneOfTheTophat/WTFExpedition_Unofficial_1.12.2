package wtf.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import wtf.config.WTFExpeditionConfig;

import java.util.List;

public class LootEventListener {

	Pair<ResourceLocation, String> dungeonMain = new ImmutablePair<>(LootTableList.CHESTS_SIMPLE_DUNGEON, "main");
	Pair<ResourceLocation, String> pyramid = new ImmutablePair<>(LootTableList.CHESTS_DESERT_PYRAMID, "main");
	Pair<ResourceLocation, String> library = new ImmutablePair<>(LootTableList.CHESTS_STRONGHOLD_LIBRARY, "main");
	Pair<ResourceLocation, String> blacksmith = new ImmutablePair<>(LootTableList.CHESTS_VILLAGE_BLACKSMITH, "main");

	List<Pair<ResourceLocation, String>> teleportScrollList = new ImmutableList.Builder<Pair<ResourceLocation, String>>().add(dungeonMain).add(pyramid).add(library).build();
	LootEntry teleportScroll = new LootEntryItem(WTFContent.home_scroll, 15, 0, new LootFunction[] {new SetCount(new LootCondition[0],
			new RandomValueRange(1, 3))}, new LootCondition[0], "loottable:teleportscroll");
	
	// LootEntry wcicTable = new LootEntryItem(Item.getItemFromBlock(WTFContent.wcicTable), 20, 0, new LootFunction[0], new LootCondition[0], "loottable:wcicTable");
	
	@SubscribeEvent
	public void lootTableLoad(LootTableLoadEvent event) {
		if (WTFExpeditionConfig.homeScrollsEnabled)
			for (Pair<ResourceLocation, String> entry : teleportScrollList)
				if (event.getName() == entry.getLeft() && event.getTable().getPool(entry.getRight()) != null)
					event.getTable().getPool(entry.getRight()).addEntry(teleportScroll);

		if (WTFExpeditionConfig.wcicTableEnabled)
			if (event.getName() == blacksmith.getLeft() && event.getTable().getPool(blacksmith.getRight()) != null) {
//				event.getTable().getPool(blacksmith.getRight()).addEntry(wcicTable);
			}
	}
}
