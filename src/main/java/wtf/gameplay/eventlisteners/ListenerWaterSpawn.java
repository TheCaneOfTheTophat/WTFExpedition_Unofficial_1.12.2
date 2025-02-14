package wtf.gameplay.eventlisteners;

import net.minecraft.block.material.Material;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ListenerWaterSpawn {
	@SubscribeEvent
	public void waterSourceControl(CreateFluidSourceEvent event) {
		Biome biome = event.getWorld().getBiome(event.getPos());

		if (event.getWorld().getBlockState(event.getPos()).getMaterial() == Material.WATER && (!BiomeDictionary.hasType(biome, Type.WET) && !BiomeDictionary.hasType(biome, Type.WATER)))
			event.setResult(Result.DENY);
    }
}
