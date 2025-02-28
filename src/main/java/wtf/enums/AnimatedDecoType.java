package wtf.enums;

import net.minecraft.util.IStringSerializable;

public enum AnimatedDecoType implements IStringSerializable {

    LAVA_CRUST("lava_crust", Modifier.LAVA_CRUST, "lava_overlay"),
    WET("wet", Modifier.WET, null),
    LAVA_DRIPPING("lava_dripping", Modifier.LAVA_DRIPPING, null);

    private final String name;
    public final Modifier modifier;
    private final String overlayName;

    AnimatedDecoType(String name, Modifier mod, String overlayName) {
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
