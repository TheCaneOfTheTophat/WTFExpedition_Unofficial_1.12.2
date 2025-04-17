package wtf.worldgen;

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

import static wtf.worldgen.GenMethods.*;

public class DungeonGenerator {
	
	public static void generate(World world, Random rand, int surfaceAverage, UnsortedChunkCaves caves) {
		if (world.provider.getDimensionType() == DimensionType.OVERWORLD && rand.nextFloat() < WTFExpeditionConfig.subtypeChance) {
			CaveListWrapper cave = getLargestCave(world, surfaceAverage, caves);

			if (cave == null)
				return;
			
			CaveProfile profile = CaveTypeRegister.getCaveProfile(cave.getBiome(world));
			AbstractDungeonType dungeon = profile.getDungeonForCave(world, rand, cave, surfaceAverage);

			if (dungeon != null) {
				BlockPos ceilPos = cave.getCenter().getCeilingPos().down();

				while (ceilPos.getY() > cave.getCenter().floor) {
					if(isFluid(world.getBlockState(ceilPos)))
						return;

					ceilPos = ceilPos.down();
				}

				System.out.println("Generating dungeon " + dungeon.name + " @ " + cave.getCenter().x + " " + (cave.getCenter().floor + 2) + " " + cave.getCenter().z);
				
				dungeon.setupForGen(cave);
				float depth = (float) (cave.getAvgFloor() / surfaceAverage);

				for (CavePosition position : cave.getCaveSet())
					generateDungeon(world, rand, dungeon, position, depth);

				dungeon.generateCenter(world, rand, cave.getCenter(), depth);
			}
		}
	}

	public static void generateDungeon(World world, Random rand, AbstractDungeonType dungeon, CavePosition pos, float depth) {
		if (dungeon.shouldPosGen(world, pos.getFloorPos(), rand))
			dungeon.generateFloor(world, rand, pos.getFloorPos(), depth);
		
		if (dungeon.shouldPosGen(world, pos.getCeilingPos(), rand))
			dungeon.generateCeiling(world, rand, pos.getCeilingPos(), depth);

		for (BlockPos wallPos : pos.wall)
			if (dungeon.shouldPosGen(world, wallPos, rand))
				dungeon.generateWall(world, rand, wallPos, depth, wallPos.getY() - pos.floor);

		for (AdjPos adjPos : pos.adj)
			if (dungeon.shouldPosGen(world, adjPos, rand))
				dungeon.generateAdjacentWall(world, rand, adjPos, depth, adjPos.getY() - pos.floor);

		if (rand.nextInt(100) < dungeon.ceilingAddonChance + (1 - depth) * 5 && dungeon.shouldPosGen(world, pos.getCeilingPos().down(), rand))
			dungeon.generateCeilingAddons(world, rand, pos.getCeilingPos().down(), depth);

		if (rand.nextInt(100) < dungeon.floorAddonChance + ( 1 - depth) * 5 && dungeon.shouldPosGen(world,  pos.getFloorPos().up(), rand))
			dungeon.generateFloorAddons(world, rand, pos.getFloorPos().up(), depth);

		if (dungeon.genAir)
			for (BlockPos airPos : pos.getAirPos())
				if (dungeon.shouldPosGen(world, airPos, rand))
					dungeon.generateAir(world, rand, airPos, depth);
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
