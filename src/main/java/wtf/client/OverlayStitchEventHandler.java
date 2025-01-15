package wtf.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.config.OreEntry;
import wtf.init.JSONLoader;

public class OverlayStitchEventHandler {
    @SubscribeEvent
    public void addOverlayTexturesToAtlas(TextureStitchEvent.Pre event) {
        for(OreEntry entry : JSONLoader.oreEntries)
            for(String path : entry.getAllOverlayPaths())
                event.getMap().registerSprite(new ResourceLocation(path));
    }
}
