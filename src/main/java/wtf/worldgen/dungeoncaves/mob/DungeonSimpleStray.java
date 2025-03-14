package wtf.worldgen.dungeoncaves.mob;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.GeneratorMethods;

public class DungeonSimpleStray extends DungeonSimpleSkeleton{

	public DungeonSimpleStray(String name) {
		super(name);
	}
	
	@Override
	public void generateCenter(GeneratorMethods gen, Random rand, CavePosition pos, float depth) {
		gen.spawnVanillaSpawner(pos.getFloorPos().up(), new ResourceLocation("stray"), 5);
	}
}
