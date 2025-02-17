package wtf.gameplay;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.config.BlockEntry;
import wtf.config.WTFExpeditionConfig;
import wtf.init.BlockSets;
import wtf.init.JSONLoader;

public class GravityMethods {

	// TODO Grass-to-dirt
	// TODO Anti-nerd pole shouldn't annihilate the block

	private final static Random random = new Random();

	private static final int grassHash = Blocks.GRASS.hashCode();
	private static final int airHash = Blocks.AIR.hashCode();

	public static void checkPos(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos); 
		Block block = state.getBlock();
		BlockEntry entry = JSONLoader.getEntryFromState(state);

		if (entry == null || entry.getPercentageStability() >= 100)
			return;

		//if the block beneath isn't solid
		if (BlockSets.nonSolidBlockSet.contains(world.getBlockState(pos.down()).getBlock())) {
			int blockhash = block.hashCode();
			double fallchance = 1;
				for (int loop = 1; loop < 6 && blockhash == world.getBlockState(pos.up(loop)).getBlock().hashCode(); loop++) {
					fallchance *= (1 - entry.getPercentageStability() / 100F);
				}
				if (random.nextFloat() < fallchance)
					dropBlock(world, pos, true);
				return;
		}


		//start check for tower conditions : all adjacent are air, and unstable block below
		BlockPos downpos = pos.down();
		IBlockState downBlockState = world.getBlockState(downpos);
		BlockEntry downBlockEntry = JSONLoader.getEntryFromState(downBlockState);
		if (WTFExpeditionConfig.antiNerdPole
				&& (downBlockEntry != null && !(downBlockEntry.getPercentageStability() >= 100))
				&& unstableTowerPos(world, pos.down())
				&& !fenceNear(world, pos, 1, 2)){ //we check for fences down two blocks at the start, then in each posCheck it checks down 1 level

			int count = 1;
			while (count < 5) {
				if (unstableTowerPos(world, pos.down(count)))
					count++;
				else
					break;
			}

			if (random.nextFloat() * count > downBlockEntry.getPercentageStability() / 100F) {
				EntityFallingBlock entityfallingblock = new EntityWTFSlidingBlock(world, pos, getRandomAdj(pos), state);
				entityfallingblock.setHurtEntities(WTFExpeditionConfig.fallingBlocksDamage);
			}
			//downward iteration is within the sliding block class
		}
	}

	public static boolean unstableTowerPos(World world, BlockPos pos){
		return world.getBlockState(pos.north()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.north().east()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.north().west()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.south()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.south().east()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.south().west()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.east()).getBlock().hashCode() == airHash
				&& world.getBlockState(pos.west()).getBlock().hashCode() == airHash
				&& !fenceNear(world, pos.down(2), 1, 0);
	}

	public static boolean dropBlock(World world, BlockPos pos, Boolean checkSupport) {
		IBlockState state = world.getBlockState(pos);

		BlockEntry entry = JSONLoader.getEntryFromState(state);
		if (entry != null && !(entry.getPercentageStability() >= 100)) {
			if (checkSupport && fenceNear(world, pos, 1, 1))
				return false;

			EntityFallingBlock entityfallingblock = new EntityWTFFallingBlock(world, pos, state);
			entityfallingblock.setHurtEntities(WTFExpeditionConfig.fallingBlocksDamage);
			world.spawnEntity(entityfallingblock);

			if (checkSupport)
				checkPos(world, pos.up());
			else
				dropBlock(world, pos.up(), false);

			return true;
		}
		return false;
	}


	public static boolean fenceNear(World world, BlockPos pos, int radius, int down) {
		for (BlockPos boxpos : BlockPos.getAllInBoxMutable(pos.add(radius, 0, radius), pos.add(-radius, -down, -radius))) {
			if (world.getBlockState(boxpos).getBlock() instanceof BlockFence)
				return true;
		}
		return false;
	}

	public static BlockPos getRandomAdj(BlockPos pos) {
		int chance = random.nextInt(4);
		switch (chance){
		case 0:
			return pos.north();
		case 1:
			return pos.east();
		case 2:
			return pos.south();
		case 3:
			return pos.west();
		}
		return null;
	}
}
