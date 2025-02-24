package wtf.entities.customentities;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFireElemental extends EntityBlockHead implements IRangedAttackMob {

	public EntityFireElemental(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
	}

    @Override
	protected void initEntityAI() {
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAIAttackMelee(this, 1.2D, false));
    }

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);

		this.setCanPickUpLoot(false);

		return livingdata;
	}

	public void setCombatTask() {}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {}
	 
	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public float getBrightness() {
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
	    return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
	    float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
	    int i = 0;

	    if (entityIn instanceof EntityLivingBase) {
	        f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
	        i += EnchantmentHelper.getKnockbackModifier(this);
	    }

	    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

	    if (flag) {
	        if (i > 0 && entityIn instanceof EntityLivingBase) {
	            ((EntityLivingBase)entityIn).knockBack(this, i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
	            this.motionX *= 0.6D;
	            this.motionZ *= 0.6D;
	        }

	        entityIn.setFire(4 * 4);

	        this.applyEnchantments(this, entityIn);
	    }

	    return flag;
	}

	@Override
	public void onDeath(DamageSource cause) {
		if (!this.world.isRemote) {
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1.5F, true, flag);
			this.setDead();
		}
		super.onDeath(cause);
	}

	@Override
	public void onLivingUpdate() {
	    if (!this.onGround && this.motionY < 0.0D)
	        this.motionY *= 0.6D;

	    if (this.world.isRemote) {
	        if (this.rand.nextInt(24) == 0 && !this.isSilent())
	            this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);

	        for (int i = 0; i < 2; ++i)
	            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
	    }

	    super.onLivingUpdate();
	}
}
