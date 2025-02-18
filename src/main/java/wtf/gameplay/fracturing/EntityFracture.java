package wtf.gameplay.fracturing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;
import wtf.config.BlockEntry;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.GravityMethods;
import wtf.init.JSONLoader;

public class EntityFracture extends Entity {

	private int count;
	ArrayList<FracVec> cracking = new ArrayList<>();

	private EntityFracture(World worldIn, BlockPos pos) {
		super(worldIn);
		this.posX=pos.getX();
		this.posY=pos.getY();
		this.posZ=pos.getZ();
	}
	
	public static void fractureAdjacent(World world, BlockPos pos) {
		EntityFracture crack = new EntityFracture(world, pos);
		crack.addAllAdj(pos);
		world.spawnEntity(crack);
	}
	
	public static void fractureAdjacentSimple(World world, BlockPos pos) {
		EntityFracture crack = new EntityFracture(world, pos);
		crack.addDirectlyAdj(pos);
		world.spawnEntity(crack);
	}
	
	public static void fractureOre(World world, BlockPos pos, int toolLevel) {
		EntityFracture crack = new EntityFracture(world, pos);
		crack.crackOre(world, pos, toolLevel);
		world.spawnEntity(crack);
	}
	
	public static void fractureHammer(World world, BlockPos pos, int toolLevel) {
		EntityFracture crack = new EntityFracture(world, pos);
		crack.crackHammer(world, pos, toolLevel);
		world.spawnEntity(crack);
	}
	
	public void crackOre(World world, BlockPos pos, int toolLevel) {
		//tool level determines how many cracks happen
		//ore level determines how strong those cracks are
		
		//currently only cracking in a single direction
		
		int blockLevel = world.getBlockState(pos).getBlock().getHarvestLevel(world.getBlockState(pos)); 
		
		EntityFracture crack = new EntityFracture(world, pos);
				
		int numLoop = (toolLevel + 1) * 4;
		int maxDist = 1 + blockLevel;
		
		for (int loop = 0; loop < numLoop; loop++) {
			int blockToFrac = MathHelper.clamp(1+rand.nextInt(blockLevel+1), 1, maxDist);

			if (rand.nextBoolean())
				cracking.add(new FracVec(pos, blockToFrac, maxDist, rand));
			else {
				for (int vecLoop = 0; vecLoop < blockToFrac; vecLoop++) {
					cracking.add(new FracVec(pos, 1, maxDist, rand));
				}
			}
		}
		world.spawnEntity(crack);
	}
	
	public void crackHammer(World world, BlockPos pos, int toolLevel) {
		EntityFracture crack = new EntityFracture(world, pos);
		Random posRandom = new Random(pos.hashCode());
		
		int numLoop = toolLevel + 1;
		
		for (int loop = 0; loop < numLoop; loop++) {
			int blockToFrac = 1 + rand.nextInt(toolLevel+1);
			int maxDist = 1 + toolLevel;
			cracking.add(new FracVec(pos, blockToFrac, maxDist, posRandom));
		}
		world.spawnEntity(crack);
	}

	//replace BlockPos with a vector and max distance- should make it more or less instantly compatible with fracturing
	//start by doing just this existing one as a vector
	//then start copying over the existing frac methods

	private void addAllAdj(BlockPos pos) {
		ArrayList<FracVec> list = new ArrayList<>();

		for (BlockPos adjpos : BlockPos.getAllInBoxMutable(pos.add(1, 1, 1), pos.add(-1, -1, -1))) {
			FracVec vec = new FracVec(pos, 1, 1, (pos.getX() - adjpos.getX()), (pos.getY() - adjpos.getY()), (pos.getZ() - adjpos.getZ()));
			list.add(vec);
		}

		Collections.shuffle(list);
		cracking.addAll(list);
	}
	
	private void addDirectlyAdj(BlockPos pos) {
		ArrayList<FracVec> list = new ArrayList<>();
		list.add(new FracVec(pos, 1, 1, 1, 0, 0));
		list.add(new FracVec(pos, 1, 1, -1, 0, 0));
		list.add(new FracVec(pos, 1, 1, 0, 1, 0));
		list.add(new FracVec(pos, 1, 1, 0, -1, 0));
		list.add(new FracVec(pos, 1, 1, 0, 0, 1));
		list.add(new FracVec(pos, 1, 1, 0, 0, -1));
		Collections.shuffle(list);
		cracking.addAll(list);
	}

	@Override
	public void onUpdate() {
		if (count < cracking.size()) {
			int random = (world.rand.nextInt(3) + 1) * cracking.size() / 27;
			for (int loop = random; loop > -1 && count < cracking.size(); loop--) {
				FracVec vec = cracking.get(count);
				doVec(vec);

				count++;
			}
		} else
			this.setDead();
	}


	private void doVec(FracVec vec) {
		for (int fracloop = 0; fracloop <= vec.blocksToFrac; fracloop++) {
			for (int dist = 1; dist <= vec.maxDist; dist++) {
				fractureBlock(world, vec.getPos(dist), true, true);
            }
		}
	}

	public static void fractureBlock(World world, BlockPos pos, boolean indirectFracture, boolean breakBlockEffect) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		BlockEntry entry;
		IBlockState cobble;

		if (state.getMaterial() == Material.AIR || block instanceof BlockFluidBase)
			return;
		else if (block instanceof BlockDecoAnim && ((BlockDecoAnim) block).getType() == BlockDecoAnim.AnimatedDecoType.LAVA_CRUST) {
			if(indirectFracture)
				world.setBlockState(pos, Blocks.LAVA.getDefaultState());
			if(breakBlockEffect)
				world.playEvent(2001, pos, Block.getStateId(state));
			return;
		} else if (block instanceof BlockDecoStatic && ((BlockDecoStatic) block).getType() == BlockDecoStatic.StaticDecoType.CRACKED) {
			entry = JSONLoader.getEntryFromState(((BlockDecoStatic) block).parentBackground);
			if(indirectFracture)
				fractureAdjacent(world, pos);
		} else
			entry = JSONLoader.getEntryFromState(state);

		if(entry != null && !entry.getFracturedBlockId().isEmpty())
			cobble = JSONLoader.getStateFromId(entry.getFracturedBlockId());
		else
			return;

		if(breakBlockEffect)
			world.playEvent(2001, pos, Block.getStateId(state));
		world.setBlockState(pos, cobble);

		if (WTFExpeditionConfig.additionalBlockGravity) {
			GravityMethods.dropBlock(world, pos, true);
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

    @Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}
}
