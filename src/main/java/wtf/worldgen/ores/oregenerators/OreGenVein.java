package wtf.worldgen.ores.oregenerators;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wtf.worldgen.GenMethods;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.utilities.wrappers.UnsortedChunkCaves;

import java.util.ArrayList;
import java.util.Random;

public class OreGenVein extends OreGenAbstract {

	public final int veinLength;
	public final int veinWidth;
	public final int veinHeight;
	public final float veinPitch;
	public static final float pi8 = (float) (Math.PI / 8);
	public static final float pi4 = (float) (Math.PI / 4);
	public static final float pi = (float) (Math.PI);
	
	public OreGenVein(IBlockState state, String name, int[] genRange, int[] minMaxPerChunk, boolean dimensionWhiteList, boolean biomeWhiteList, int[] dimensions, float pitch, boolean genDense, int biomeLeniency) {
		super(state, name, genRange, minMaxPerChunk, dimensionWhiteList, biomeWhiteList, genDense, biomeLeniency);

		this.veinLength = dimensions[0];
		this.veinWidth = dimensions[1];
		this.veinHeight = dimensions[2];
		this.veinPitch = pitch;
	}

	@Override
	public void doOreGen(World world, Random rand, ChunkPos pos, int surfaceAverage, UnsortedChunkCaves caves, ArrayList<BlockPos> water) {
		int blocksPerChunk = this.getBlocksPerChunk(world, rand, pos, surfaceAverage, biomeLeniency);
		int blocksReq = this.blocksReq();

		while (blocksPerChunk > blocksReq || (blocksPerChunk > 0 && rand.nextInt(blocksReq) < blocksPerChunk)) {
			int x = pos.getXStart() + rand.nextInt(16) + 8;
			int y = this.getGenStartHeight(surfaceAverage, rand);
			int z = pos.getZStart() + rand.nextInt(16) + 8;

			BlockPos orePos = new BlockPos(x, y, z);
			boolean generate = checkBiomes(world, orePos, biomeLeniency);

			blocksPerChunk -= generate ? genVein(world, rand , orePos, surfaceAverage, caves) : 1;
			
			//The core issue, is that using the density function makes exponentially dense veins
		}
	}

	@Override
	public int genVein(World world, Random rand, BlockPos pos, int surfaceAverage, UnsortedChunkCaves caves) {
		
		//int length = veinLength * rand.nextInt(5) - 2;
		int length = veinLength;
		int blocksSet = 0;
		
		float pitchY = veinPitch + (rand.nextFloat() * pi4) - pi8;
		float pitchX = rand.nextFloat() * pi;
		float vecY = MathHelper.cos(pitchY);
		float vecX = MathHelper.cos(pitchX) * MathHelper.sin(pitchY);
		float vecZ = MathHelper.sin(pitchX) * MathHelper.sin(pitchY);
		
		float vecWidthY = MathHelper.cos(pitchY + pi4);
		float vecWidthX = MathHelper.cos(pitchX + pi4);// * MathHelper.sin(pitchY+pi4);
		float vecWidthZ = MathHelper.sin(pitchX + pi4);// * MathHelper.sin(pitchY+pi4);

		double x = pos.getX() - vecWidthX * veinWidth / 2 - vecX * length / 2;
		double y = pos.getY() - vecWidthY * veinWidth / 2 - vecY * length / 2;
		double z = pos.getZ() - vecWidthZ * veinWidth / 2 - vecZ * length / 2;
		
		pos = new BlockPos(x, y, z);
		
		for (int widthLoop = 0; widthLoop < veinWidth; widthLoop++) {
			for (int heightLoop = 0; heightLoop < veinHeight; heightLoop++) {
				double xpos = pos.getX() + widthLoop * vecWidthX;
				double zpos = pos.getZ() + widthLoop * vecWidthZ;
				double ypos = pos.getY() + heightLoop * vecWidthY;
				
				for (int lengthLoop = 0; lengthLoop < length; lengthLoop++) {
					int densityToSet = genDenseOres ? getDensityToSet(rand, (int) ypos, surfaceAverage) : 0;
					
					if (rand.nextFloat() < this.veinDensity) {
						blocksSet += densityToSet + 1;
						GenMethods.setOre(world, new BlockPos(xpos, ypos , zpos), this.oreBlock, densityToSet);
					}
						
					xpos += vecX;
					zpos += vecZ;
					ypos += vecY;
				}
			}
		}

		return blocksSet;
	}

	@Override
	public int blocksReq() {
		return (int) (this.veinLength * this.veinWidth * this.veinHeight * this.veinDensity) * 2;
	}
}

