package wtf.gameplay.eventlisteners;

import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.init.WTFContent;

public class ListenerReplaceTorch {

    @SubscribeEvent
    public void EntityPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        if(event.getPlacedBlock().getBlock() == Blocks.TORCH)
            event.getWorld().setBlockState(event.getPos(), WTFContent.lit_torch.getDefaultState().withProperty(BlockTorch.FACING, event.getState().getValue(BlockTorch.FACING)));
    }
}
