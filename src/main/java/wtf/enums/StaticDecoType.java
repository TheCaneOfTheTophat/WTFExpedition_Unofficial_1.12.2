package wtf.enums;

import net.minecraft.util.IStringSerializable;

public enum StaticDecoType implements IStringSerializable {
    MOSS("mossy", Modifier.MOSS, "moss_overlay"),
    SOUL("soul", Modifier.SOUL, "soul_overlay"),
    CRACKED("cracked", Modifier.CRACKED, "cracked_overlay");

    private final String name;
    public final Modifier modifier;
    private final String overlayName;

    StaticDecoType(String name, Modifier mod, String overlayName) {
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
