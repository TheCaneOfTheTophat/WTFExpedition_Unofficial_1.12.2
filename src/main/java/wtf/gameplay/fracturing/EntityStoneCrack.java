package wtf.gameplay.fracturing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;
import wtf.config.BlockEntry;
import wtf.config.WTFExpeditionConfig;
import wtf.init.JSONLoader;

public class EntityStoneCrack extends Entity {

	private int count;
	Random random = new Random();
	private final BlockPos ori;

	private EntityStoneCrack(World worldIn, BlockPos pos) {
		super(worldIn);
		this.posX=pos.getX();
		this.posY=pos.getY();
		this.posZ=pos.getZ();
		this.ori = pos;
	}
	
	public static void crackStone(World world, BlockPos pos) {
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
		crack.addAllAdj(pos);
		world.spawnEntity(crack);
	}
	
	public static void FracOreSmple(World world, BlockPos pos) {
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
		crack.addAdj(pos);
		world.spawnEntity(crack);
	}
	
	public static void FracOre(World world, BlockPos pos, int toolLevel) {
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
		crack.crackOre(world, pos, toolLevel);
		world.spawnEntity(crack);
	}
	
	public static void cracHammer(World world, BlockPos pos, int toolLevel) {
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
		crack.crackHammer(world, pos, toolLevel);
		world.spawnEntity(crack);
	}
	
	public void crackOre(World world, BlockPos pos, int toolLevel) {
		//tool level determines how many cracks happen
		//ore level determines how strong those cracks are
		
		//currently only cracking in a single direction
		
		int blockLevel = world.getBlockState(pos).getBlock().getHarvestLevel(world.getBlockState(pos)); 
		
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
				
		int numLoop = (toolLevel+1)*4;
		int maxDist = 1 + blockLevel;
		
		for (int loop = 0; loop < numLoop; loop++){
			int blockToFrac = MathHelper.clamp(1+random.nextInt(blockLevel+1), 1, maxDist);
			
			
			if (random.nextBoolean()){
				cracking.add(new FracVec(pos, blockToFrac, maxDist, random));
			}
			else {
				for (int vecLoop = 0; vecLoop < blockToFrac; vecLoop++){
					cracking.add(new FracVec(pos, 1, maxDist, random));
				}
			}
		}
		
		world.spawnEntity(crack);
		
		
	}
	
	public void crackHammer(World world, BlockPos pos, int toolLevel){
		EntityStoneCrack crack = new EntityStoneCrack(world, pos);
		Random posRandom = new Random(pos.hashCode());
		
		int numLoop = toolLevel+1;
		
		for (int loop = 0; loop < numLoop; loop++){
			int blockToFrac = 1 + random.nextInt(toolLevel+1);
			int maxDist = 1 + toolLevel;
			cracking.add(new FracVec(pos, blockToFrac, maxDist, posRandom));
		}
		world.spawnEntity(crack);
		
	}
	
	
	
	
	

	//replace BlockPos with a vector and max distance- should make it more or less instantly compatible with fracturing
	//start by doing just this existing one as a vector
	//then start copying over the existing frac methods

	private void addAllAdj(BlockPos pos){
		ArrayList<FracVec> list = new ArrayList<FracVec>();
		for (int xloop = -1; xloop < 2; xloop++){
			for (int yloop = -1; yloop < 2; yloop++){
				for (int zloop = -1; zloop < 2; zloop++){
					FracVec vec = new FracVec(pos, 1, 1, xloop, yloop, zloop);
					list.add(vec);
				}	
			}	
		}
		Collections.shuffle(list);
		cracking.addAll(list);

	}
	
	private void addAdj(BlockPos pos){
		ArrayList<FracVec> list = new ArrayList<FracVec>();
		list.add(new FracVec(pos, 1, 1, 1, 0, 0));
		list.add(new FracVec(pos, 1, 1, -1, 0, 0));
		list.add(new FracVec(pos, 1, 1, 0, 1, 0));
		list.add(new FracVec(pos, 1, 1, 0, -1, 0));
		list.add(new FracVec(pos, 1, 1, 0, 0, 1));
		list.add(new FracVec(pos, 1, 1, 0, 0, -1));
		Collections.shuffle(list);
		cracking.addAll(list);
	}
	

	ArrayList<FracVec> cracking = new ArrayList<FracVec>();

	@Override
	public void onUpdate()
	{
		if (count < cracking.size() ){
			int random = (world.rand.nextInt(3)+1) * cracking.size()/27;
			for (int loop = random; loop > -1 && count < cracking.size(); loop--){
				FracVec vec = cracking.get(count);
				doVec(vec);

				count++;
			}
		}
		else {
			this.setDead();
		}
	}


	private void doVec(FracVec vec) {
		for (int fracloop = 0; fracloop <= vec.blocksToFrac; fracloop++) {
			for (int dist = 1; dist <= vec.maxDist; dist++){

				BlockPos pos = vec.getPos(dist);
				IBlockState state = world.getBlockState(pos);
				BlockEntry entry = JSONLoader.getEntryFromState(state);
				IBlockState cobble;

				if(entry != null && !entry.getFracturedBlockId().isEmpty())
					cobble = JSONLoader.getStateFromId(entry.getFracturedBlockId());
				else
					return;
				
				if (state.getBlock() instanceof BlockDecoAnim && ((BlockDecoAnim) state.getBlock()).getType() == BlockDecoAnim.AnimatedDecoType.LAVA_CRUST)
					world.setBlockState(pos, Blocks.LAVA.getDefaultState());
				else if (state.getBlock() instanceof BlockDecoStatic && ((BlockDecoStatic) state.getBlock()).getType() == BlockDecoStatic.StaticDecoType.CRACKED){
					BlockDecoStatic block = (BlockDecoStatic) state.getBlock();
					entry = JSONLoader.getEntryFromState(block.parentBackground);
					cobble = JSONLoader.getStateFromId(entry.getFracturedBlockId());

					if (cobble != null) {
						world.setBlockState(pos, cobble);
						if (WTFExpeditionConfig.additionalBlockGravity) {
							// GravityMethods.dropBlock(world, pos, true);
						}
						addAllAdj(pos);
					}
				}
				else if (cobble != null){
					world.setBlockState(pos, cobble);
					if (WTFExpeditionConfig.additionalBlockGravity) {
//						GravityMethods.dropBlock(world, pos, true);
					}
				}
			}
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}
}
