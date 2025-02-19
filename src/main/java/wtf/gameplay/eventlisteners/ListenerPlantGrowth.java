package wtf.gameplay.eventlisteners;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import wtf.config.WTFExpeditionConfig;

public class ListenerPlantGrowth {

	static ImmutableMap<Block, Type> plantmods = new ImmutableMap.Builder<Block, Type>()
		.put(Blocks.BEETROOTS, Type.COLD)
		.put(Blocks.CACTUS, Type.DRY)
		.put(Blocks.CARROTS, Type.HILLS)
		.put(Blocks.COCOA, Type.JUNGLE)
		.put(Blocks.MELON_STEM, Type.LUSH)
		.put(Blocks.BROWN_MUSHROOM, Type.SWAMP)
		.put(Blocks.RED_MUSHROOM, Type.CONIFEROUS)
		.put(Blocks.NETHER_WART, Type.NETHER)
		.put(Blocks.POTATOES, Type.PLAINS)
		.put(Blocks.PUMPKIN_STEM, Type.FOREST)
		.put(Blocks.REEDS, Type.RIVER)
		.put(Blocks.WHEAT, Type.SAVANNA).build();
	
	static Random random = new Random();
	
	@SubscribeEvent
	public void cropGrowEvent(CropGrowEvent event) {
		float chance = random.nextFloat();
		Type type = plantmods.get(event.getState().getBlock());

		if (type != null) {
			Biome biome = event.getWorld().getBiome(event.getPos());
			double growthPercent = WTFExpeditionConfig.cropGrowthPercentModifier * (BiomeDictionary.hasType(biome, type) ? 2 : 1);

			if (chance > growthPercent) {
				event.setResult(Result.DENY);
				return;
			}
		}
		event.setResult(Result.DEFAULT);
	}

	@SubscribeEvent
	public void harvestDropsEvent(HarvestDropsEvent event) {
		Block plant = event.getState().getBlock();
		if (plantmods.containsKey(plant)) {
			Biome biome = event.getWorld().getBiome(event.getPos());
			if (!BiomeDictionary.hasType(biome, plantmods.get(plant))) {
				List<ItemStack> drops = event.getDrops();
				for (int loop = 0; loop < drops.size() && drops.size() > 1; loop++)
					if (random.nextFloat() < 0.33)
						drops.remove(loop);
			}
		}
	}
}
