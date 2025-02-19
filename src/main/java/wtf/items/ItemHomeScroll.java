package wtf.items;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import wtf.config.WTFExpeditionConfig;

public class ItemHomeScroll extends Item {

	Random random = new Random();
	
	public ItemHomeScroll() {}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(WTFExpeditionConfig.homeScrollsEnabled) {
			BlockPos home = player.getBedLocation(world.provider.getDimension());
			if (home != null) {
				stack.shrink(1);
				if (!world.isRemote) {
					player.setPositionAndUpdate(home.getX(), home.getY(), home.getZ());
					WorldServer worldServer = (WorldServer) world;
					worldServer.spawnParticle(EnumParticleTypes.PORTAL, home.getX(), home.getY() + random.nextDouble() * 2.0D, home.getZ(), 32, random.nextGaussian(), 0.0D, random.nextGaussian(), 1.0D);
					worldServer.spawnParticle(EnumParticleTypes.PORTAL, player.posX, player.posY + random.nextDouble() * 2.0D, player.posZ, 32, random.nextGaussian(), 0.0D, random.nextGaussian(), 1.0D);
				}
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}
}
