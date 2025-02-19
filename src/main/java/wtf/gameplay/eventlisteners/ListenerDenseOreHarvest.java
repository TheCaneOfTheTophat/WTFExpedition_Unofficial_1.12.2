package wtf.gameplay.eventlisteners;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.blocks.BlockDenseOre;
import wtf.blocks.BlockDenseOreFalling;

import java.util.List;

public class ListenerDenseOreHarvest {

    @SubscribeEvent
    public void BlockHarvestEvent(BlockEvent.HarvestDropsEvent event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if(block instanceof BlockDenseOre || block instanceof BlockDenseOreFalling) {
            EntityPlayer player = event.getHarvester();

            if(player != null) {
                Vec3d vec3d = player.getPositionEyes(1.0F);
                Vec3d vec3d1 = player.getLook(1.0F);
                double blockReachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
                Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
                RayTraceResult result = event.getWorld().rayTraceBlocks(vec3d, vec3d2, false, false, true);

                if(result != null) {
                    List<ItemStack> drops = event.getDrops();
                    event.setDropChance(0f);
                    for (ItemStack stack : drops)
                        Block.spawnAsEntity(event.getWorld(), event.getPos().offset(result.sideHit), stack);
                }
            }
        }
    }
}
