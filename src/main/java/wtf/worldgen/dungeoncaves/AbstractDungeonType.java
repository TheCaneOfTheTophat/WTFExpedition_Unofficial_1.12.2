package wtf.worldgen.dungeoncaves;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wtf.utilities.simplex.SimplexHelper;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

public abstract class AbstractDungeonType extends AbstractCaveType {
	
	protected SimplexHelper simplex;
	protected BlockPos midPos;
	protected double wallDist;
	protected CaveListWrapper cave = null;
	
	public AbstractDungeonType(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}
	
	//core idea- that the dungeon wrapper calls the cave method if it doens't choose to generate something
	//issue- the generators need to be seperate, so nevermind

	public abstract boolean canGenerateAt(World world, CaveListWrapper cave);
	
	protected boolean isHeight(CaveListWrapper cave, int n) {
		return cave.getAvgCeiling() - cave.getAvgFloor() >= n;
	}
	
	public void setupForGen(CaveListWrapper cave) {
		simplex = new SimplexHelper(name + cave.centerPos.toString(), false);
		this.cave = cave;
		midPos = cave.centerPos.getMidPos();
		wallDist = cave.getWallDist(cave.centerPos);
	}
	
	public boolean shouldPosGen(World world, BlockPos pos, Random random) {
		double rad = cave.distFromCenter(pos);

		//for some reason, the center pos wasn't generating, so I've just stuck this check in
		if (pos.getX() == midPos.getX() && pos.getZ() == midPos.getZ())
			return true;
		
		double vecX = (midPos.getX() - pos.getX()) / rad;
		double vecY = (midPos.getY() - pos.getY()) / rad;
	
		double pitchY = Math.acos(vecY);
		double sinY = Math.sin(pitchY);
		
		double n = MathHelper.clamp(vecX/sinY, -1, 1);
		double pitchX = Math.acos(n);
		double pitchZ = Math.asin(n);
		 
		double noise = simplex.get3DNoise(world, pitchX * 2, pitchY * 3, pitchZ * 2);
        double variable = 5;
        double maxDist = noise * variable + wallDist + 1;
		
		//y is left out in order to generate a cylinder, rather than a sphere
		int x = pos.getX() - midPos.getX();
		int z = pos.getZ() - midPos.getZ();

		int chunkPosX = pos.getX() - cave.xStart;
		int chunkPosZ = pos.getZ() - cave.zStart;

		return (x * x + z * z < maxDist * maxDist) && (!(chunkPosX == 0 || chunkPosZ == 0 || chunkPosX == 15 || chunkPosZ == 15) || random.nextBoolean());
	}

	public abstract void generateCenter(World world, Random rand, CavePosition pos, float depth);
}
