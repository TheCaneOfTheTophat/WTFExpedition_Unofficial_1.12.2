package wtf.config;

import com.google.common.collect.ImmutableMap;
import wtf.blocks.enums.StaticDecoType;
import wtf.blocks.enums.AnimatedDecoType;

import java.util.Map;

public class BlockEntry {
    private String blockId = "";
    private String fracturedBlockId = "";
    private String name = "";
    private String texture = "";

    private boolean fracturesFirstWhenMined = true;
    private int percentageMineSpeedModifier = 100;
    private int percentageStability = 100;

    private DecorationTypes decoration = new DecorationTypes();

    private boolean speleothems = false;

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
        return fracturesFirstWhenMined;
    }

    public float getPercentageMineSpeedModifier() {
        return percentageMineSpeedModifier / 100F;
    }

    public int getPercentageStability() {
        return percentageStability;
    }

    public Map<StaticDecoType, Boolean> getStaticDecorTypes() {
        Map<StaticDecoType, Boolean> map = ImmutableMap.of(StaticDecoType.MOSS, decoration.mossy, StaticDecoType.SOUL, decoration.soul, StaticDecoType.CRACKED, decoration.cracked);
        return map;
    }

    public Map<AnimatedDecoType, Boolean> getAnimatedDecorTypes() {
        Map<AnimatedDecoType, Boolean> map = ImmutableMap.of(AnimatedDecoType.LAVA_CRUST, decoration.lava_crust, AnimatedDecoType.DRIP_WATER, decoration.water_drip, AnimatedDecoType.DRIP_LAVA, decoration.lava_drip);
        return map;
    }

    public boolean hasSpeleothems() {
        return speleothems;
    }

    private static class DecorationTypes {
        private boolean mossy = false;
        private boolean soul = false;
        private boolean cracked = false;
        private boolean lava_crust = false;
        private boolean water_drip = false;
        private boolean lava_drip = false;
    }
}
