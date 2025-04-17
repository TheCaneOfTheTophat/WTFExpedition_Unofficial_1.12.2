package wtf.enums;

import net.minecraft.util.IStringSerializable;

public enum StaticDecoType implements IStringSerializable {

    MOSS("mossy", "moss_overlay"),
    SOUL("soul", "soul_overlay"),
    CRACKED("cracked", "cracked_overlay");

    private final String name;
    private final String overlayName;

    StaticDecoType(String name, String overlayName) {
        this.name = name;
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
