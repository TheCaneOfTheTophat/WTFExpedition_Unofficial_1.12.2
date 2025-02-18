package wtf.gameplay;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExploderEntity extends Entity {

	private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(ExploderEntity.class, DataSerializers.VARINT);
	private boolean flaming = false;
	private float str;
	private int fuse;

	public ExploderEntity(World worldIn, BlockPos pos, float str) {
		super(worldIn);
		fuse = 2;
		this.str = str;
		this.posX = pos.getX();
		this.posY = pos.getY();
		this.posZ = pos.getZ();
	}

	public ExploderEntity(World worldIn, Vec3d pos, float str, int fuse, boolean fire) {
		super(worldIn);
		this.fuse = fuse;
		this.str = str;
		this.posX = pos.x;
		this.posY = pos.y;
		this.posZ = pos.z;
		this.flaming = fire;
	}

	//I should be able to modify this to actually do the explosion as well-
	//which allows me to modify the time between increments, and overall slow the explosion dow

	CustomExplosion explosion;

	@Override
	public void onUpdate() {
		if (!this.world.isRemote) {
			if (fuse == 0) {
				explosion = new CustomExplosion(null, this.world, new Vec3d(this.posX, this.posY, this.posZ), str, flaming);
				explosion.incrementVectorList();
				//explosion.update();
			} else if (fuse < 0) {
				if (!explosion.vecList.isEmpty()) {
					explosion.incrementVectorList();
					//explosion.update();
				} else {
					this.setDead();
					explosion.update();
				}
			}
			fuse--;
		}
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(FUSE, Integer.valueOf(10));
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

    @Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setShort("Fuse", (short)fuse);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		fuse=(compound.getShort("Fuse"));
	}

	public void setFuse(int fuseIn) {
		this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
		this.fuse = fuseIn;
	}

	public void notifyDataManagerChange(DataParameter<?> key) {
		if (FUSE.equals(key))
			this.fuse = this.getFuseDataManager();
	}

	public int getFuseDataManager() {
		return this.dataManager.get(FUSE);
	}
}
