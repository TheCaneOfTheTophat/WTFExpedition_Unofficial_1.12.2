package wtf.worldgen;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.utilities.wrappers.ChunkCoords;

public class SandstoneNaturaliser {

	int sandstoneID = Blocks.SANDSTONE.getDefaultState().hashCode();
	int redsandstonestoneID = Blocks.RED_SANDSTONE.getDefaultState().hashCode();


	
	//this isn't world specific, and it should be
	ChunkCoords current = null;
	
	@SubscribeEvent
	public void chunkGen(InitMapGenEvent event){
		if (event.getType() == EventType.CAVE){
			event.setNewGen(new MapGenWTFCave());
		}
	}



}
