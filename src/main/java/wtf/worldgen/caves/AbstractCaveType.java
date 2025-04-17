package wtf.worldgen.caves;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.AdjPos;

import java.util.Random;

public abstract class AbstractCaveType {

	public final String name;
	public final int ceilingAddonChance;
	public final int floorAddonChance;
	public boolean genAir = false;
	protected static SimplexHelper simplex = new SimplexHelper("CaveGenerator", true);

	public AbstractCaveType(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		this.name = name;
		this.ceilingAddonChance = ceilingAddonPercentChance;
		this.floorAddonChance = floorAddonPercentChance;
	}

	public abstract void generateCeiling(World world, Random rand, BlockPos pos, float depth);

	public abstract void generateFloor(World world, Random rand, BlockPos pos, float depth);
	
	public abstract void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth);
	
	public abstract void generateFloorAddons(World world, Random rand, BlockPos pos, float depth);
	
	public abstract void generateWall(World world, Random rand, BlockPos pos, float depth, int height);
	
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {}

	public void generateAir(World world, Random rand, BlockPos pos, float depth) {}
	
	protected int getSpeleothemSize(Random rand, float depth) {
		return (int)(Math.sqrt(rand.nextInt(9)) * (1 - depth)) + rand.nextInt(2) + rand.nextInt(2);
	}
}
