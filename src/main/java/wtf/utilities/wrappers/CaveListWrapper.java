package wtf.utilities.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import wtf.WTFExpedition;
import wtf.init.BlockSets;

public class CaveListWrapper {

	private final HashMap<XZ, CavePosition> cave;
	private final ArrayList<CavePosition> caveList = new ArrayList<>();
	private final ArrayList<BlockPos> wallPos = new ArrayList<>();
	public final int xStart;
	private final int xEnd;
	public final int zStart;
	private final int zEnd;

	private double totalFloor = 0;
	private double totalCeiling = 0;
	private double totalX = 0;
	private double totalZ = 0;

	int minFloor;
	int minCeiling;
	int minX;
	int minZ;

	int maxFloor;
	int maxCeiling;
	int maxX;
	int maxZ;

	public CaveListWrapper(CavePosition pos, ChunkPos chunkPos) {
		xStart = chunkPos.getXStart() + 8;
		xEnd = chunkPos.getXEnd() + 8;
		zStart = chunkPos.getZStart() + 8;
		zEnd = chunkPos.getZEnd() + 8;
		cave = new HashMap<>();
		addPos(pos);
		maxFloor = pos.floor;
		minFloor = pos.floor;
		maxCeiling = pos.ceiling;
		minCeiling = pos.ceiling;
		minX = pos.x;
		maxX = pos.x;
		minZ = pos.z;
		maxZ = pos.z;
	}

	public boolean contains(XZ xz) {
		return this.cave.containsKey(xz);
	}

	public Collection<CavePosition> getCaveSet() {
		return cave.values();
	}

	public void addPos(CavePosition pos) {
		wallPos.addAll(pos.wall);
		cave.put(pos.xz(), pos);

		totalFloor += pos.floor;
		totalCeiling += pos.ceiling;
		totalX += pos.x;
		totalZ += pos.z;

		maxFloor = Math.max(maxFloor, pos.floor);
		minFloor = Math.min(minFloor, pos.floor);
		maxCeiling = Math.max(maxCeiling, pos.ceiling);
		minCeiling = Math.min(minCeiling, pos.ceiling);
		minX = Math.min(minX, pos.x);
		maxX = Math.max(maxX, pos.x);
		minZ = Math.min(minZ, pos.z);
		maxZ = Math.max(maxZ, pos.z);
	}

	public boolean isAwayFromEdge() {
		int chunkX = this.centerPos.x - xStart;
		int chunkZ = this.centerPos.z - zStart;

		int radiusX = (int) Math.round((this.getSizeX() - 1) / 2);
		int radiusZ = (int) Math.round((this.getSizeZ() - 1) / 2);

		boolean inX = chunkX <= (17 - radiusX) && chunkX > radiusX - 2;
		boolean inZ = chunkZ <= (17 - radiusZ) && chunkZ > radiusZ - 2;

		return inX && inZ;
	}

	public double density() {
		double area = Math.PI * (getSizeX() / 2D) * (getSizeZ() / 2D);
		//double area = getSizeX()*getSizeZ();
        return (float) (size() / area);
	}

	public double getAvgFloor() {
		return totalFloor / cave.size();
	}

	public double getAvgCeiling() {
		return totalCeiling / cave.size();
	}

	public double getAvgX() {
		return totalX / cave.size();
	}

	public double getAvgZ() {
		return totalZ / cave.size();
	}

	public Biome getBiome(World world) {
		return world.getBiomeForCoordsBody(new BlockPos(this.getAvgX(), this.getAvgFloor(), this.getAvgZ()));
	}

	public double getSizeX() {
		return maxX - minX;
	}

	public double getSizeZ() {
		return maxZ - minZ;
	}

	public CavePosition centerPos = null;

	public CavePosition getCenter() {
		return centerPos;
	}

	public double getCenterScore() {
		CavePosition bestpos = null;
		double bestscore = 10;

		for (CavePosition checkpos : this.caveList) {
			double posFailScore = getClearance(checkpos);
			if (posFailScore < bestscore) {
				bestpos = checkpos;
				bestscore = posFailScore;
			} else if(posFailScore == bestscore) {
				if (checkpos.ceiling - checkpos.floor > bestpos.ceiling - bestpos.floor)
					bestpos = checkpos;
			}
		}
		
		centerPos = bestpos;
		return bestscore;
	}

	public double getClearance(CavePosition pos) {

		if (4 > pos.ceiling - pos.floor)
			return 100;
	
		int loop = 4;

		double failScore = 0;

		for (int xloop = -loop; xloop <= loop; xloop++) {
			for (int zloop = -loop; zloop <= loop; zloop++) {
				CavePosition checkpos = cave.get(new XZ(pos.x + xloop, pos.z + zloop));
				if (checkpos != null) {
					if (pos.floor != checkpos.floor) {
						double floorDif = pos.floor - checkpos.floor;
						double score = (floorDif * floorDif) / (xloop * xloop + zloop * zloop);
						failScore += score;
					}
				} else {
					double score = 10 / (double) (xloop * xloop + zloop * zloop);
					failScore += score;
				}
			}
		}

		return failScore;

	}

	public double dungeonScore(World world, double surfaceAvg) {
		if (this.size() < 25)
			return 0;
		
		if (this.density() < 0.9)
			return 0;

		if (this.minFloor < 12)
			return 0;

		//below the surface
		if (this.maxCeiling > surfaceAvg)
			return 0;

		// at least 5 in both dimensions
		if (this.getSizeX() < 5 || this.getSizeZ() < 5 || this.getSizeX() + this.getSizeZ() < 14)
			return 0;

		//double sizeDif = Math.abs(this.getSizeX() - this.getSizeZ());
		//if (sizeDif < 3){ // x and z are within half of eachother 
		//	return 0;
		//}

		//and because it we just did check center, we know that pos can't == null
		double failScore = this.getCenterScore();

		//has at 5x5 block area that is totally clear
		if (this.centerPos == null)
			return 0;

		if (4 > this.centerPos.ceiling-this.centerPos.floor || 4 > this.getAvgCeiling() - this.getAvgFloor())
			return 0;

		if (!this.isAwayFromEdge())
			return 0;
		
		IBlockState state = world.getBlockState(centerPos.getFloorPos());
		IBlockState up = world.getBlockState(centerPos.getFloorPos().up());

		if (!BlockSets.ReplaceHashset.contains(state.getBlock()) && up.getMaterial() != Material.AIR)
			return 0;

		return 100 - failScore;
	}

	public CavePosition getRandomPosition(Random random) {
		return caveList.get(random.nextInt(caveList.size()));
	}

	public BlockPos getRandomWall(Random random) {
		return !wallPos.isEmpty() ? this.wallPos.get(random.nextInt(wallPos.size())) : null;
	}

	public int size() {
		return this.cave.size();
	}

	public double distFromCenter(BlockPos pos) {
		BlockPos center = this.centerPos.getMidPos();

		int x = center.getX() - pos.getX();
		int y = center.getY() - pos.getY();
		int z = center.getZ() - pos.getZ();

		return Math.sqrt(x * x + y * y + z * z);
	}

	public void setCaveArrayList() {
		caveList.addAll(this.cave.values());
	}
	
	public double getWallDist(CavePosition pos1) {
		double dist = 99999999;

		for (CavePosition pos2 : cave.values()) {
			if (!pos2.wall.isEmpty()) {
				int xdif = pos2.x - pos1.x;
				int zdif = pos2.z - pos1.z;
				int distsq = xdif * xdif + zdif * zdif;
				if (distsq<dist)
					dist=distsq;
			}
		}

		double smallest = Math.sqrt(dist);

		//and now it checks that this distance is less than the distance to the edge of the chunk

		smallest = pos1.x - xStart < smallest ? pos1.x - xStart : smallest;
		smallest = xEnd - pos1.x < smallest ? xEnd - pos1.x : smallest;
		smallest = pos1.z - zStart < smallest ? pos1.z - zStart : smallest;
		smallest = zEnd - pos1.z < smallest ? zEnd - pos1.z : smallest;
		
		return smallest;
	}
}
