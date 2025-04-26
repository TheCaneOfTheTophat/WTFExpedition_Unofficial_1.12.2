package wtf.worldgen.trees;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtf.worldgen.trees.types.AbstractTreeType;

import static wtf.worldgen.GenMethods.*;

public class TreeInstance {

	public final Random random;
	public final World world;
	public final BlockPos pos;
	public final double oriX;
	public final double y;
	public final double oriZ;
	
	public final AbstractTreeType type;

	public final double trunkDiameter;
	public final double trunkRadius;

	public final double trunkHeight;
	public final double rootLevel;

	public final double scale;
	
	public final boolean snow;
	
	private final HashMap<BlockPos, IBlockState> trunkBlocks;
	private final HashMap<BlockPos, IBlockState> leafBlocks;
	private final HashMap<BlockPos, IBlockState> rootBlocks;
	private final HashMap<BlockPos, IBlockState> decoBlocks;


	public TreeInstance(World world, Random random, BlockPos pos, double scale, AbstractTreeType tree) {
		trunkBlocks = new HashMap<>();
		leafBlocks = new HashMap<>();
		rootBlocks = new HashMap<>();
		decoBlocks = new HashMap<>();

		this.world = world;
		this.random = random;
		this.pos = pos;
		this.oriX = pos.getX() + 0.5;
		this.y = pos.getY();
		this.oriZ = pos.getZ() + 0.5;
		this.snow = BiomeDictionary.hasType(world.getBiome(pos), Type.SNOWY);
		this.type = tree;

		this.scale = scale;
		
		trunkHeight = tree.getTrunkHeight(scale);
		trunkDiameter = tree.getTrunkDiameter(scale);
		trunkRadius = trunkDiameter / 2;

		//branchLength = MathHelper.ceiling_double_int((tree.baseBranchLength +  tree.baseBranchLength*scale)/2);
		//rootLength = (int) (trunkHeight/tree.rootLengthDivisor);
		if (!tree.airGenerate)
			rootLevel = tree.rootLevel == 0 ? random.nextInt(2): tree.rootLevel;
		else
			rootLevel = tree.airGenHeight; // +1 because generation height is cut off at > airGenHeight
	}
	
	public void setTrunk(BlockPos pos) {
		trunkBlocks.put(pos, type.wood);
	}

	public void setRoot(BlockPos pos) {
		rootBlocks.put(pos, type.wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
	}

	public void setBranch(BlockPos pos, BlockLog.EnumAxis axis) {
		rootBlocks.put(pos, type.branch.withProperty(BlockLog.LOG_AXIS, axis));
	}

	public void setLeaf(BlockPos pos) {
		if (world.isAirBlock(pos)) {
			leafBlocks.put(pos, type.leaf.withProperty(BlockLeaves.CHECK_DECAY, false));

			if (snow)
				setDeco(pos.up(), Blocks.SNOW_LAYER.getDefaultState());
		}
	}
	
	public void setDeco(BlockPos pos, IBlockState state) {
		decoBlocks.put(pos, state);
	}

	public void setBlocksForPlacement(World world) {
		HashMap<BlockPos, IBlockState> masterMap = new HashMap<>();

		masterMap.putAll(decoBlocks);
		masterMap.putAll(leafBlocks);
		masterMap.putAll(rootBlocks);
		masterMap.putAll(trunkBlocks);

		for (Entry<BlockPos, IBlockState> entry : masterMap.entrySet())
			replace(world, entry.getKey(), entry.getValue());
	}

	public boolean inTrunk(BlockPos pos) {
		return trunkBlocks.containsKey(pos);
	}
}
