package wtf.worldgen.caves.types;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.enums.Modifier;
import wtf.utilities.wrappers.AdjPos;
import wtf.worldgen.caves.AbstractCaveType;

import java.util.Random;

import static wtf.worldgen.GenMethods.*;

public class CaveTypeJungleVolcano extends AbstractCaveType {

	public CaveTypeJungleVolcano(String name, int ceilingAddonPercentChance, int floorAddonPercentChance) {
		super(name, ceilingAddonPercentChance, floorAddonPercentChance);
	}

	@Override
	public void generateCeiling(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		double n = simplex.get3DNoiseScaled(world, pos, 0.33);
		
		if (noise > 0.66)
			if (n > 0.75)
				modify(world, pos, Modifier.FRACTURED);
			else if (n > 0.5)
				modify(world, pos, Modifier.LAVA_CRUST);
		else {
			boolean mossy =  simplex.get3DNoise(world, pos) > depth;

			if (mossy)
				modify(world, pos, Modifier.MOSS);
		}

		if (simplex.get3DNoise(world, pos) < 0.1)
			modify(world, pos, Modifier.CRACKED);
	}

	@Override
	public void generateFloor(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		double n = simplex.get3DNoiseScaled(world, pos, 0.33);
		
		if (noise > 0.66){
			if (n > 0.75)
				modify(world, pos, Modifier.FRACTURED);
			else if (n > 0.5)
				modify(world, pos, Modifier.LAVA_CRUST);
		} else {
			boolean mossy =  simplex.get3DNoise(world, pos) > depth;

			if (mossy)
				modify(world, pos, Modifier.MOSS);
		}

		if (simplex.get3DNoise(world, pos) < 0.1)
			modify(world, pos, Modifier.CRACKED);
	}


	@Override
	public void generateCeilingAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos.up(), 0.1);
		
		if (noise < 0.66) {
			setCeilingAddon(world, pos, Modifier.FRACTURED);

			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
					genVines(world, pos.east().down(loop), EnumFacing.WEST);

			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
					genVines(world, pos.west().down(loop), EnumFacing.EAST);


			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
					genVines(world, pos.north().down(loop), EnumFacing.SOUTH);


			if (rand.nextBoolean())
				for (int loop = rand.nextInt(3)+1; loop > -1; loop--)
					genVines(world, pos.south().down(loop), EnumFacing.NORTH);
		} else
			genSpeleothem(world, pos, this.getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateFloorAddons(World world, Random rand, BlockPos pos, float depth) {
		double noise = simplex.get3DNoiseScaled(world, pos.down(), 0.1);

		if (noise < 0.66) {
			if (rand.nextBoolean())
				genSpeleothem(world, pos, this.getSpeleothemSize(rand, depth), depth, false);
		} else
			genSpeleothem(world, pos, this.getSpeleothemSize(rand, depth), depth, false);
	}

	@Override
	public void generateWall(World world, Random rand, BlockPos pos, float depth, int height) {
		double noise = simplex.get3DNoiseScaled(world, pos, 0.1);
		double n = simplex.get3DNoiseScaled(world, pos, 0.33);
		
		if (noise > 0.66) {
			if (n > 0.75)
				modify(world, pos, Modifier.FRACTURED);
			else if (n > 0.5)
				modify(world, pos, Modifier.LAVA_CRUST);
		} else {
			boolean mossy =  simplex.get3DNoise(world, pos) > depth;

			if (mossy)
				modify(world, pos, Modifier.MOSS);
		}

		if (simplex.get3DNoise(world, pos) < 0.1)
			modify(world, pos, Modifier.CRACKED);
	}

	@Override
	public void generateAdjacentWall(World world, Random rand, AdjPos pos, float depth, int height) {
		boolean mossy = simplex.get3DNoise(world, pos) > depth;

		if (mossy)
			genVines(world, pos, pos.getFace(rand));
	}
}
