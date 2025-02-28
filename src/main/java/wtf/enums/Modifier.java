package wtf.enums;

import net.minecraft.util.IStringSerializable;

public enum Modifier {

    FRACTURED(null),
    MOSS(StaticDecoType.MOSS),
    SOUL(StaticDecoType.SOUL),
    CRACKED(StaticDecoType.CRACKED),
    LAVA_CRUST(AnimatedDecoType.LAVA_CRUST),
    WET(AnimatedDecoType.WET),
    LAVA_DRIPPING(AnimatedDecoType.LAVA_DRIPPING),
    FROZEN(null),
    BRICK(null);

    private final IStringSerializable decoType;

    Modifier(IStringSerializable mod) {
        this.decoType = mod;
    }

    public IStringSerializable getDecoType() {
        return decoType;
    }
}
