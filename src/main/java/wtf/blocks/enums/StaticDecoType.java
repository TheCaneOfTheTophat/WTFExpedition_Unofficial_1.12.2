package wtf.blocks.enums;

import net.minecraft.util.IStringSerializable;
import wtf.init.BlockSets;

public enum StaticDecoType implements IStringSerializable {
    MOSS("mossy", BlockSets.Modifier.MOSSY, "moss_overlay"),
    SOUL("soul", BlockSets.Modifier.SOUL, "soul_overlay"),
    CRACKED("cracked", BlockSets.Modifier.CRACKED, "cracked_overlay");


    private final String name;
    public final BlockSets.Modifier modifier;
    private final String overlayName;

    StaticDecoType(String name, BlockSets.Modifier mod, String overlayName) {
        this.name = name;
        this.modifier = mod;
        this.overlayName = overlayName;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getOverlayName() {
        return overlayName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
