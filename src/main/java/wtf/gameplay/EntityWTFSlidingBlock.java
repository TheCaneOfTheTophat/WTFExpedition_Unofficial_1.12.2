package wtf.gameplay;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityWTFSlidingBlock extends EntityWTFFallingBlock {

	private final IBlockState fallTile;
	public final int oriY;
	
	public EntityWTFSlidingBlock(World worldIn, BlockPos pos, BlockPos targetpos, IBlockState fallingBlockState) {
		super(worldIn, pos, fallingBlockState);
		double motx = pos.getX() - targetpos.getX();
		double motz = pos.getZ() - targetpos.getZ();
		this.oriY = pos.getY();
		this.fallTile = fallingBlockState;

		addVelocity(0.05D * motx, -0.1D, 0.05D * motz);
		worldIn.spawnEntity(this);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
    
	@Override
	public void onUpdate() {
		if (this.fallTime++ > 18) {
			if(!this.world.isRemote) {
				Block block = this.fallTile.getBlock();
				this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
			}
			setDead();
			super.onUpdate();
		}
    	else
    		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }
}
