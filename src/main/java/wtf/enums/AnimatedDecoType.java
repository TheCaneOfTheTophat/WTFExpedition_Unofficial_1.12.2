package wtf.enums;

import net.minecraft.util.IStringSerializable;

public enum AnimatedDecoType implements IStringSerializable {

    LAVA_CRUST("lava_crust", "lava_overlay"),
    WET("wet", null),
    LAVA_DRIPPING("lava_dripping", null);

    private final String name;
    private final String overlayName;

    AnimatedDecoType(String name, String overlayName) {
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
