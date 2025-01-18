package wtf.config;

import com.google.common.collect.ImmutableMap;
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;

import java.util.Map;

public class BlockEntry {
    private String blockId;
    private String fracturedBlockId;
    private String name;
    private String texture;

    private boolean fracturesFirstWhenMined;
    private int percentageMineSpeedModifier;

    private StaticDecor staticDecor = new StaticDecor();
    private AnimatedDecor animatedDecor = new AnimatedDecor();

    private boolean speleothems;

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

    public int getPercentageMineSpeedModifier() {
        return percentageMineSpeedModifier;
    }

    public Map<BlockDecoStatic.StaticDecoType, Boolean> getStaticDecorTypes() {
        Map<BlockDecoStatic.StaticDecoType, Boolean> map = ImmutableMap.of(BlockDecoStatic.StaticDecoType.MOSS, staticDecor.mossy, BlockDecoStatic.StaticDecoType.SOUL, staticDecor.soul, BlockDecoStatic.StaticDecoType.CRACKED, staticDecor.cracked);
        return map;
    }

    public Map<BlockDecoAnim.AnimatedDecoType, Boolean> getAnimatedDecorTypes() {
        Map<BlockDecoAnim.AnimatedDecoType, Boolean> map = ImmutableMap.of(BlockDecoAnim.AnimatedDecoType.LAVA_CRUST, animatedDecor.lava_crust, BlockDecoAnim.AnimatedDecoType.DRIP_WATER, animatedDecor.water_drip, BlockDecoAnim.AnimatedDecoType.DRIP_LAVA, animatedDecor.lava_drip);
        return map;
    }

    public boolean hasSpeleothems() {
        return speleothems;
    }

    private static class StaticDecor {
        private boolean mossy;
        private boolean soul;
        private boolean cracked;
    }

    private static class AnimatedDecor {
        private boolean lava_crust;
        private boolean water_drip;
        private boolean lava_drip;
    }
}
