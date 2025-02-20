package wtf.config;

import com.google.common.collect.ImmutableMap;
import wtf.blocks.enums.StaticDecoType;
import wtf.blocks.enums.AnimatedDecoType;

import java.util.Map;

public class BlockEntry {
    private final String blockId;
    private final String fracturedBlockId;
    private final String name;
    private final String texture;

    /*
    Bit 1 - Fractures first when mined
    Bit 2 - Mossy
    Bit 3 - Soul
    Bit 4 - Cracked
    Bit 5 - Lava crusted
    Bit 6 - Wet
    Bit 7 - Lava dripping
    Bit 8 - Speleothems
    Much less crowded constructor if you're using a byte.
     */

    private final byte flags;

    private final int percentageMineSpeedModifier;
    private final int percentageStability;

    public BlockEntry(String blockId, String fracturedBlockId, String name, String texture, byte flags, int percentageMineSpeedModifier, int percentageStability) {
        this.blockId = blockId;
        this.fracturedBlockId = fracturedBlockId;
        this.name = name;
        this.texture = texture;
        this.flags = flags;
        this.percentageMineSpeedModifier = percentageMineSpeedModifier;
        this.percentageStability = percentageStability;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getFracturedBlockId() {
        return fracturedBlockId;
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public boolean fracturesFirstWhenMined() {
        return (flags & 1) == 1;
    }

    public float getPercentageMineSpeedModifier() {
        return percentageMineSpeedModifier / 100F;
    }

    public int getPercentageStability() {
        return percentageStability;
    }

    public Map<StaticDecoType, Boolean> getStaticDecorTypes() {
        Map<StaticDecoType, Boolean> map = ImmutableMap.of(StaticDecoType.MOSS, (flags & 2) == 2, StaticDecoType.SOUL, (flags & 4) == 4, StaticDecoType.CRACKED, (flags & 8) == 8);
        return map;
    }

    public Map<AnimatedDecoType, Boolean> getAnimatedDecorTypes() {
        Map<AnimatedDecoType, Boolean> map = ImmutableMap.of(AnimatedDecoType.LAVA_CRUST, (flags & 16) == 16, AnimatedDecoType.DRIP_WATER, (flags & 32) == 32, AnimatedDecoType.DRIP_LAVA, (flags & 64) == 64);
        return map;
    }

    public boolean hasSpeleothems() {
        return (flags & -128) == -128;
    }
}
