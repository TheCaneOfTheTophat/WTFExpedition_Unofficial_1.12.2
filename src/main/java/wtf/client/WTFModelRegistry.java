package wtf.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wtf.blocks.*;
import wtf.blocks.enums.IcicleType;
import wtf.init.WTFContent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class WTFModelRegistry {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent register) {
        // Get all items
        for(Item item : WTFContent.items) {
            if(item instanceof ItemBlock) {
                if (((ItemBlock) item).getBlock() instanceof BlockIcicle)
                    for (IcicleType type : IcicleType.values())
                        ModelLoader.setCustomModelResourceLocation(item, type.getID(), new ModelResourceLocation(item.getRegistryName().toString(), "type=" + type.getName()));
                else if (((ItemBlock) item).getBlock() instanceof BlockStainedTerracottaPatch)
                    for (EnumDyeColor type : EnumDyeColor.values())
                        ModelLoader.setCustomModelResourceLocation(item, type.getMetadata(), new ModelResourceLocation(item.getRegistryName().toString(), "color=" + type.getName()));
                else if (((ItemBlock) item).getBlock() instanceof BlockRoots)
                    for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
                        ModelLoader.setCustomModelResourceLocation(item, type.getMetadata(), new ModelResourceLocation(item.getRegistryName().toString(), "variant=" + type.getName()));
                    else
                        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
            }
            else
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
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
