package wtf.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wtf.config.GameplayConfig;
import wtf.config.MasterConfig;

import static wtf.init.WTFItems.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class WTFModels {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent register) {
        setModel(sulfur);
        setModel(nitre);

        if(MasterConfig.gameplaytweaks && GameplayConfig.homescroll)
            setModel(home_scroll);
    }

    public static void setModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
