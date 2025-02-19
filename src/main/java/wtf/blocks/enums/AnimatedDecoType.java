package wtf.blocks.enums;

import net.minecraft.util.IStringSerializable;
import wtf.init.BlockSets;

public enum AnimatedDecoType implements IStringSerializable {
    LAVA_CRUST("lava_crust", BlockSets.Modifier.LAVA_CRUST, "lava_overlay"),
    DRIP_WATER("wet", BlockSets.Modifier.WATER_DRIP, null),
    DRIP_LAVA("lava_dripping", BlockSets.Modifier.LAVA_DRIP, null);

    private final String name;
    public final BlockSets.Modifier modifier;
    private final String overlayName;

    AnimatedDecoType(String name, BlockSets.Modifier mod, String overlayName) {
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
