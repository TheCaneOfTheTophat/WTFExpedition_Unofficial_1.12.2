package wtf.utilities.wrappers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.CustomExplosion;
import wtf.gameplay.ExploderEntity;
import wtf.gameplay.fracturing.EntityFracture;
import wtf.init.BlockSets;

import java.util.Random;

public class ExpVec extends Vec {

	private final Random random;
	private final World world;
	private double str;
	float attenuation = 0.75F;
	private final boolean flaming;
	private final CustomExplosion explosion;
	int waterHash = Material.WATER.hashCode();
	int lavaHash = Material.LAVA.hashCode();

	public ExpVec(World world, BlockPos pos, double x, double y, double z, double vecStr, CustomExplosion explosion) {
		super(pos, x, y, z);
		this.world = world;
		this.str = vecStr;
		this.explosion = explosion;
		this.flaming = explosion.flaming;
		this.random = new Random();
	}

	IBlockState air = Blocks.AIR.getDefaultState();

	public boolean increment() {
		if (!hasNext())
			return false;

		BlockPos pos = this.next();

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		float resistance = block.getExplosionResistance(explosion.sourceEntity);
		int hash = state.getMaterial().hashCode();

		if (hash == waterHash || hash == lavaHash)
			world.setBlockState(pos, air, 2);
		else {
			if (BlockSets.explosiveBlocks.containsKey(block)) {
				world.setBlockState(pos, air, 2);
				ExploderEntity entity = new ExploderEntity(world, pos, BlockSets.explosiveBlocks.get(block));
				world.spawnEntity(entity);
			}

			double atomize = resistance * WTFExpeditionConfig.atomizingExplosionLevel;

			float chance = (float) ((atomize-str) / atomize);
			if (chance > 1F) {
				str -= resistance;
				block.onBlockDestroyedByExplosion(world, pos, explosion);
				world.setBlockState(pos, air, 2);
			} else if (str > resistance * WTFExpeditionConfig.droppingExplosionLevel) {
				str -= resistance;
				block.onBlockDestroyedByExplosion(world, pos, explosion);

				explosion.spawnExtraParticles(pos, (float) str, pos.getX() , pos.getY(), pos.getZ());

				if (block.canDropFromExplosion(explosion))
					block.dropBlockAsItemWithChance(world, pos, state, chance, 0);

				if (flaming && world.isAirBlock(pos) && this.world.getBlockState(pos.down()).isFullBlock() && random.nextInt(2) == 0)
					world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 2);
				else
					world.setBlockState(pos, air, 2);
			} 
			else if (WTFExpeditionConfig.explosionsFracture) {
				str -= resistance / 3;
				EntityFracture.fractureBlock(world, pos, true, false);
			}
		}
		str -= attenuation;
		return true;
	}

	public double getStr() {
		return str;
	}

	public double strX() {
		return str*vecX;
	}

	public double strY() {
		return str*vecY;
	}

	public double strZ() {
		return str*vecZ;
	}

	public boolean hasNext() {
		return str > 0;
	}
}
