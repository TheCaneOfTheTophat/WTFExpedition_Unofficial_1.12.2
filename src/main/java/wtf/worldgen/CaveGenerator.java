package wtf.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.wrappers.AdjPos;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.utilities.wrappers.UnsortedChunkCaves;
import wtf.worldgen.caves.AbstractCaveType;
import wtf.worldgen.caves.CaveProfile;
import wtf.worldgen.caves.CaveTypeRegister;

import java.util.Random;

public class CaveGenerator {

	public static void generate(World world, Random rand, int surfaceAverage, UnsortedChunkCaves caves) {
		for (CaveListWrapper cave : caves.getCaves()) {
			CaveProfile profile = CaveTypeRegister.getCaveProfile(cave.getBiome(world));
			float depth = (float) (1 - cave.getAvgFloor() / surfaceAverage);

			AbstractCaveType caveType = profile.getCave(cave.getAvgFloor() / surfaceAverage);
			
			for (CavePosition position : cave.getCaveSet() ) {
				caveType.generateFloor(world, rand, position.getFloorPos(), depth);
				caveType.generateCeiling(world, rand, position.getCeilingPos(), depth);

				for (BlockPos wallPos : position.wall)
					caveType.generateWall(world, rand, wallPos, depth, wallPos.getY() - position.floor);

				for (AdjPos adjPos : position.adj)
					caveType.generateAdjacentWall(world, rand, adjPos, depth, adjPos.getY() - position.floor);

				if (rand.nextInt(100) < caveType.ceilingAddonChance + (depth) * 5)
					caveType.generateCeilingAddons(world, rand, position.getCeilingPos().down(), depth);

				if (rand.nextInt(100) < caveType.floorAddonChance + (depth) * 5)
					caveType.generateFloorAddons(world, rand, position.getFloorPos().up(), depth);

				if (caveType.genAir)
					for (BlockPos airPos : position.getAirPos())
						caveType.generateAir(world, rand, airPos, depth);
			}
		}
	}	
}
