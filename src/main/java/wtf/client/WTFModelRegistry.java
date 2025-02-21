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
import wtf.blocks.*;
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
            else for (int i = 0; i < meta + 1; i++)
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName().toString(), "inventory" + i));
        }

        // Get all blocks
        for(Block block : WTFContent.blocks) {
            // Set state mapper for dense ores
            if(block instanceof BlockDenseOre || block instanceof BlockDenseOreFalling) {
                ModelLoader.setCustomStateMapper(block, WTFStateMappers.DENSE_ORE_STATE_MAPPER);
                for (int i = 0; i <= 2; i++) {
                    ModelResourceLocation mrl = block instanceof BlockDenseOreFalling ? new DerivativeFallingResourceLocation((BlockDenseOreFalling) block, "density", i) : new DerivativeResourceLocation((BlockDenseOre) block, "density", i);
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, mrl);
                }
            }

            // Set state mapper for speleothems
            else if(block instanceof BlockSpeleothem) {
                ModelLoader.setCustomStateMapper(block, WTFStateMappers.SPELEOTHEM_STATE_MAPPER);
                for (int i = 0; i <= 6; i++)
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new DerivativeResourceLocation((BlockSpeleothem) block, "type", i));
            }

            // Set state mapper for decor
            else if(block instanceof BlockDecoStatic || block instanceof BlockDecoAnim) {
                ModelLoader.setCustomStateMapper(block, WTFStateMappers.DECORATION_STATE_MAPPER);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new DerivativeResourceLocation((AbstractBlockDerivative) block, "normal"));
            }
        }
    }
}
