package wtf.worldgen;

import java.util.HashSet;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.caves.CaveProfile;
import wtf.worldgen.caves.CaveTypeRegister;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;
import wtf.utilities.wrappers.UnsortedChunkCaves;

public class DungeonGenerator {
	
	public static void generate(World world, Random rand, int surfaceAverage, UnsortedChunkCaves caves) {
		if (world.provider.getDimensionType() == DimensionType.OVERWORLD && rand.nextFloat() < WTFExpeditionConfig.subtypeChance) {
//			for (ChunkCoords adj : coords.getChunksInRadius(1)) {
//				if (spawned.contains(adj)) 
//					return;
//			}

			CaveListWrapper cave = getLargestCave(world, surfaceAverage, caves);
			if (cave == null) {
				//if the cave is null
				return;
			}
			
			CaveProfile profile = CaveTypeRegister.getCaveProfile(cave.getBiome(world));
			AbstractDungeonType dungeon = profile.getDungeonForCave(world, rand, cave, surfaceAverage);

			if (dungeon != null) {
				System.out.println("Generating dungeon " + dungeon.name + " @ " + cave.getCenter().x + " " + (cave.getCenter().floor + 2) + " " + cave.getCenter().z);
				// spawned.add(coords);
				
				dungeon.setupForGen(cave);
				float depth = (float) (cave.getAvgFloor() / surfaceAverage);

				for (CavePosition position : cave.getCaveSet())
					generateDungeon(world, rand, dungeon, position, depth);

				dungeon.generateCenter(world, rand, cave.getCenter(), depth);
			}
		}
	}

	public static void generateDungeon(World world, Random rand, AbstractDungeonType dungeon, CavePosition pos, float depth) {
		if (dungeon.shouldPosGen(world, pos.getFloorPos()))
			dungeon.generateFloor(world, rand, pos.getFloorPos(), depth);
		
		if (dungeon.shouldPosGen(world, pos.getCeilingPos()))
			dungeon.generateCeiling(world, rand, pos.getCeilingPos(), depth);

		for (BlockPos wallpos : pos.wall)
			if (dungeon.shouldPosGen(world, wallpos))
				dungeon.generateWall(world, rand, wallpos, depth, wallpos.getY() - pos.floor);

		for (AdjPos adjpos : pos.adj)
			if (dungeon.shouldPosGen(world, adjpos))
				dungeon.generateAdjacentWall(world, rand, adjpos, depth, adjpos.getY() - pos.floor);


		if (rand.nextInt(100) < dungeon.ceilingAddonChance + (1 - depth) * 5 && dungeon.shouldPosGen(world, pos.getCeilingPos().down()))
			dungeon.generateCeilingAddons(world, rand, pos.getCeilingPos().down(), depth);

		if (rand.nextInt(100) < dungeon.floorAddonChance + ( 1 - depth) * 5 && dungeon.shouldPosGen(world,  pos.getFloorPos().up()))
			dungeon.generateFloorAddons(world, rand, pos.getFloorPos().up(), depth);

		if (dungeon.genAir)
			for (BlockPos airpos : pos.getAirPos())
				if (dungeon.shouldPosGen(world, airpos))
					dungeon.generateAir(world, rand, airpos, depth);
	}

	public static CaveListWrapper getLargestCave(World world, int surfaceAverage, UnsortedChunkCaves caves) {
		CaveListWrapper best = null;
		double bestvalue = 1;

		for (CaveListWrapper wrappedcave : caves.getCaves()) {
			double score = wrappedcave.dungeonScore(world, surfaceAverage);

			if (score > bestvalue) {
				best = wrappedcave;
				bestvalue = score;
			}
		}

		return best;
	}
}
