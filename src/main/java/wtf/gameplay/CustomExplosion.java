package wtf.gameplay;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.wrappers.ExpVec;

public class CustomExplosion extends Explosion {

	private final World world;
	public Entity sourceEntity;
	public boolean flaming;
	int counterMod;
	boolean isSmoking;
	Random random = new Random();
	HashSet<ExpVec> vecList = new HashSet<>(); // create a list of explosion vectors

	public CustomExplosion(Entity entity, World world, Vec3d vec3d, float str, boolean fire) {
		super(world, entity, vec3d.x, vec3d.y, vec3d.z, str, false, false);
		flaming = fire;
		isSmoking = fire;
		this.world = world;
		BlockPos origin = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
		this.sourceEntity = entity;
		this.counterMod = 0;
		populateVectorList(origin, str);
		doExplosionB(origin, str);
	}

	/**
	 * Populates the affectedBlocksList with any blocks that should be affected
	 * by this explosion
	 */
	protected void populateVectorList(BlockPos origin, float baseStr) {
		float xpos = getModifier(origin.east());
		float xneg = getModifier(origin.west());
		float ypos = getModifier(origin.up());
		float yneg = this.sourceEntity instanceof EntityCreeper ? (float) (getModifier(origin.down())/ WTFExpeditionConfig.creeperUpwardModifier) :  getModifier(origin.down());
		float zpos = getModifier(origin.north());
		float zneg = getModifier(origin.south());
		float ftotal = xpos + xneg + ypos + yneg + zpos + zneg;

		//System.out.println(" y neg = " + yneg);

		xpos = setModifier(xpos, ftotal) * baseStr;
		xneg = setModifier(xneg, ftotal) * baseStr;
		ypos = setModifier(ypos, ftotal) * baseStr;
		yneg = setModifier(yneg, ftotal) * baseStr;
		zpos = setModifier(zpos, ftotal) * baseStr;
		zneg = setModifier(zneg, ftotal) * baseStr;

		//System.out.println("north = " + zpos + " south " + zneg);
		//System.out.println("east = " + xpos + " west " + xneg);

		//System.out.println("y+ " + ypos + " y- " + yneg);

		int xMin = MathHelper.clamp(MathHelper.floor(-4 * xneg), -12, -4);
		int xMax = MathHelper.clamp(MathHelper.floor(4 * xpos), 4, 12);
		int yMin = MathHelper.clamp(MathHelper.floor(-4 * yneg),-12, -4);
		int yMax = MathHelper.clamp(MathHelper.floor(4 * ypos), 4, 12);
		int zMin = MathHelper.clamp(MathHelper.floor(-4 * zneg),-12, -4);
		int zMax = MathHelper.clamp(MathHelper.floor(4 * zpos), 4, 12);

		for (int xloop = xMin; xloop < xMax + 1; xloop++) {
			for (int yloop = yMin; yloop < yMax + 1; yloop++) {
				for (int zloop = zMin; zloop < zMax + 1; zloop++) {

					// This checks if it's an edge of the cube
					if (xloop == xMin || xloop == xMax || yloop == yMin || yloop == yMax || zloop == zMin || zloop == zMax) {
						// the values to increment along the ray each loop
						// length of the vector
						double vectorLength = Math.sqrt(xloop * xloop + yloop * yloop + zloop * zloop);

						// setting the values
						double incX = xloop/vectorLength;
						double incY = yloop/vectorLength;
						double incZ = zloop/vectorLength;

						// selecting between pos and neg vector strength
						float xcomp = xloop == 0 ? 0 : xloop > 0 ? xpos: xneg;
						float ycomp = yloop == 0 ? 0 : yloop > 0 ? ypos: yneg;
						float zcomp = zloop == 0 ? 0 : zloop > 0 ? zpos: zneg;

						double absIncX = Math.abs(incX);
						double absIncY = Math.abs(incY);
						double absIncZ = Math.abs(incZ);

						double total = absIncX + absIncY + absIncZ;

						// setting the vector strength, as a sum of each
						// component times the corresponding increment, plus a random component
						double vector = (xcomp * (absIncX / total) + ycomp * (absIncY / total) + zcomp * (absIncZ / total)) * (0.7 + random.nextFloat() * 0.6);

						//add the vector info to a vector list
						vecList.add(new ExpVec(world, origin, incX, incY, incZ, vector, this));
					}
				}
			}
		}
	}

	public void incrementVectorList() {
		Iterator<ExpVec> iterator = vecList.iterator();
		while (iterator.hasNext()) {
			ExpVec vec = iterator.next();

			if (vec.increment()) {
				this.getAffectedBlockPositions().add(vec.pos());
				this.getAffectedBlockPositions().add(vec.pos().up());
				this.getAffectedBlockPositions().add(vec.pos().down());
				this.getAffectedBlockPositions().add(vec.pos().east());
				this.getAffectedBlockPositions().add(vec.pos().west());
				this.getAffectedBlockPositions().add(vec.pos().north());
				this.getAffectedBlockPositions().add(vec.pos().south());
				BlockPos end = vec.pos();
				List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(end));

				float damage = (float) vec.getStr() * 7.5F;
				if (!list.isEmpty() && vec.getStr() > 0) {
					for (Entity entity : list) {
						if (entity instanceof EntityLivingBase) {
							double d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, damage);
							if (vec.getStr() > 0.5)
								entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) (d11*damage*WTFExpeditionConfig.explosionDamageModifier));
						}

						entity.addVelocity(vec.strX() / 10 * WTFExpeditionConfig.explosionForceModifier, vec.strY() / 10 * WTFExpeditionConfig.explosionForceModifier, vec.strZ() / 10 * WTFExpeditionConfig.explosionForceModifier);
						entity.velocityChanged = true;
					}
				}
			} else
				iterator.remove();
		}
	}

	public void doExplosionB(BlockPos origin, float baseStr) {
		this.world.playSound(null, origin.getX(), origin.getY(), origin.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

		if(!this.world.isRemote) {
			WorldServer worldServer = ((WorldServer) this.world);
			EnumParticleTypes type = baseStr >= 2.0F && isSmoking ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE;
			worldServer.spawnParticle(type, origin.getX(), origin.getY(), origin.getZ(), 0, 0, 0, 0, 1.0D);
		}
	}


	/**
	 * Actually explodes the block and spawns particles if allowed
	 */
	public void spawnExtraParticles(BlockPos origin, float baseStr, int i, int j, int k) {
		if(!this.world.isRemote) {
			WorldServer worldServer = ((WorldServer) this.world);

			double d0 = i + world.rand.nextFloat();
			double d1 = j + world.rand.nextFloat();
			double d2 = k + world.rand.nextFloat();
			double d3 = d0 - origin.getX();
			double d4 = d1 - origin.getY();
			double d5 = d2 - origin.getZ();
			double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
			d3 /= d6;
			d4 /= d6;
			d5 /= d6;
			double d7 = 0.5D / (d6 / baseStr + 0.1D);
			d7 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
			d3 *= d7;
			d4 *= d7;
			d5 *= d7;

			worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + origin.getX() * 1.0D) / 2.0D, (d1 + origin.getY() * 1.0D) / 2.0D,  (d2 + origin.getZ() * 1.0D) / 2.0D, 0, d3, d4, d5, 1.0D);
			worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0, d3, d4, d5, 1.0D);
		}
	}

	private float getModifier(BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		float mod = MathHelper.sqrt(MathHelper.sqrt(1 / (1 + state.getBlock().getExplosionResistance(world, pos, this.sourceEntity, this) * 5F * state.getBlockHardness(world, pos))));
		if (mod < 1) {
			counterMod++;
			return mod;
		} else
			return 0.95F;
	}

	private float setModifier(float dir, float totalAssigned) {
        return dir / (totalAssigned / 6F);
	}
	
	void update() {
		for (BlockPos pos : this.getAffectedBlockPositions()) {
			IBlockState iblockstate = world.getBlockState(pos);
			iblockstate.neighborChanged(world, pos, iblockstate.getBlock(), pos);
			if (WTFExpeditionConfig.additionalBlockGravity)
				GravityMethods.checkPos(world, pos);
		}
		this.clearAffectedBlockPositions();
	}
}