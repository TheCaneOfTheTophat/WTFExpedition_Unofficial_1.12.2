package wtf.entities.customentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBogLantern extends EntityAmbientCreature {
	
	private float flameSize;
    boolean flameOn = true;
    private BlockPos spawnPosition;

	public EntityBogLantern(World worldIn) {
		super(worldIn);
        this.setSize(0.5F, 0.5F);
		this.flameSize = 0.25F;
		this.enablePersistence();
    }

    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {}

    protected void collideWithNearbyEntities() {}
	
    @Override
	protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
       // this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
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
        return flameOn;
    }
	
    @Override
	public float getRenderSizeModifier() {
        return 0.1F;
    }

    @Override
    public void onUpdate() {
       super.onUpdate();

       if(this.world.isRemote && (ticksExisted == 28 && !flameOn) || (ticksExisted == 30 && flameOn))
           for(int i = 0; i < Math.round(42 * flameSize); i++)
               spawnParticles();

       if (ticksExisted > 30) {
           float light = this.world.getLightBrightness(new BlockPos(this.posX, this.posY, this.posZ));
           flameSize = 1.5F - light;
           flameOn = !flameOn;
           ticksExisted = flameOn ? -this.rand.nextInt(100) - 100 : -this.rand.nextInt(30);
       }

        this.motionY *= 0.6000000238418579D;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote && flameOn)
            for(int i = 0; i < Math.round(flameSize * 1.5); i++)
                if(rand.nextFloat() > 0.75)
                    spawnParticles();

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        super.updateAITasks();

        if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
            this.spawnPosition = null;

        if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int) this.posX, (int) this.posY, (int) this.posZ) < 4.0D)
            this.spawnPosition = new BlockPos((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - 2, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));

        double d0 = (double) this.spawnPosition.getX() + 0.5D - this.posX;
        double d1 = (double) this.spawnPosition.getY() + 0.1D - this.posY;
        double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.posZ;
        this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
        this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
        this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
        float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
        float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
        this.moveForward = 0.5F;
        this.rotationYaw += f1;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    private void spawnParticles() {
        double d1 = this.posX + ((this.rand.nextDouble() - 0.5D) * flameSize / 2);
        double d2 = (this.posY + flameSize / 4) + 0.1D + ((this.rand.nextDouble() - 0.5D) * flameSize / 2);
        double d3 = this.posZ + ((this.rand.nextDouble() - 0.5D) * flameSize / 2);
        this.world.spawnParticle(EnumParticleTypes.FLAME, d1, d2, d3, 0, 0, 0);
    }

    public float getFlameSize() {
        return flameSize ;
    }
}
