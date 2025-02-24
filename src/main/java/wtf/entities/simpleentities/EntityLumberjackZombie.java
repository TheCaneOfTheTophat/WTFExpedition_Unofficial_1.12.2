package wtf.entities.simpleentities;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityLumberjackZombie extends EntityZombie {

	public EntityLumberjackZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack((rand.nextBoolean() ? Items.STONE_AXE : Items.IRON_AXE), 1));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET, 1));

		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound compoundTag = compound.getCompoundTag("display");
		compound.setTag("display", compoundTag);
		compoundTag.setInteger("color", 6723840);
		ItemStack stack = new ItemStack(Items.LEATHER_CHESTPLATE, 1);
		stack.setTagCompound(compound);

		setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
	}
}
