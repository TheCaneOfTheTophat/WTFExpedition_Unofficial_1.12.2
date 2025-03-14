package wtf.worldgen;

import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SandstoneNaturaliser {

	@SubscribeEvent
	public void chunkGen(InitMapGenEvent event) {
		if (event.getType() == EventType.CAVE)
			event.setNewGen(new MapGenWTFCave());
	}
}
