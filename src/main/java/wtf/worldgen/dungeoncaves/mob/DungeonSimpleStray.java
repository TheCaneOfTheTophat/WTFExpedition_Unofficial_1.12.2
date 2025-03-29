package wtf.worldgen.dungeoncaves.mob;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import wtf.utilities.wrappers.CavePosition;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class DungeonSimpleStray extends DungeonSimpleSkeleton {

	public DungeonSimpleStray(String name) {
		super(name);
	}
	
	@Override
	public void generateCenter(World world, Random rand, CavePosition pos, float depth) {
		spawnVanillaSpawner(world, pos.getFloorPos().up(), new ResourceLocation("stray"), 5);
	}
}
