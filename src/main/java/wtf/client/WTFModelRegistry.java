package wtf.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wtf.blocks.BlockDenseOre;
import wtf.init.WTFContent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class WTFModelRegistry {

    public static final Map<ResourceLocation, Integer> metaMap = new HashMap<>();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent register) {
        // Get all items
        for(Item item : WTFContent.items) {
            Integer meta = metaMap.get(item.getRegistryName());
            // If the current item does not have a corresponding key in the meta map, 0. If there is, get the meta from the meta map
            if(meta == null)
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
            else for (int i = 0; i < meta + 1; i++) {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName().toString(), "inventory" + i));
            }
        }

        for(Block block : WTFContent.blocks) {
            if(block instanceof BlockDenseOre) {
                ModelLoader.setCustomStateMapper(block, WTFStateMappers.DENSE_ORE_STATE_MAPPER);
                for (int i = 0; i <= 2; i++) {
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new DenseOreResourceLocation((BlockDenseOre) block, i));
                }
            }
        }
    }
}
