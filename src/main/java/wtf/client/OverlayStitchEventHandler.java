package wtf.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.WTFExpedition;
import wtf.config.OreEntry;
import wtf.init.JSONLoader;

public class OverlayStitchEventHandler {

    @SubscribeEvent
    public void addOverlayTexturesToAtlas(TextureStitchEvent.Pre event) {
        ImmutableList<String> decoOverlays = ImmutableList.of("moss_overlay", "soul_overlay", "cracked_overlay", "lava_overlay");

        for(OreEntry entry : JSONLoader.oreEntries)
            if(!entry.getRawOverlayPath().isEmpty())
                for(String path : entry.getAllOverlayPaths())
                    event.getMap().registerSprite(new ResourceLocation(path));

        for(String overlay : decoOverlays)
            event.getMap().registerSprite(new ResourceLocation(WTFExpedition.modID + ":overlays/" + overlay));
    }
}
