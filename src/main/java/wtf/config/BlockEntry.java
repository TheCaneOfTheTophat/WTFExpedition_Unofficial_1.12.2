package wtf.config;

import com.google.common.collect.ImmutableMap;
import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;

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

    public int getPercentageMineSpeedModifier() {
        return percentageMineSpeedModifier;
    }

    public int getPercentageStability() {
        return percentageStability;
    }

    public Map<BlockDecoStatic.StaticDecoType, Boolean> getStaticDecorTypes() {
        Map<BlockDecoStatic.StaticDecoType, Boolean> map = ImmutableMap.of(BlockDecoStatic.StaticDecoType.MOSS, decoration.mossy, BlockDecoStatic.StaticDecoType.SOUL, decoration.soul, BlockDecoStatic.StaticDecoType.CRACKED, decoration.cracked);
        return map;
    }

    public Map<BlockDecoAnim.AnimatedDecoType, Boolean> getAnimatedDecorTypes() {
        Map<BlockDecoAnim.AnimatedDecoType, Boolean> map = ImmutableMap.of(BlockDecoAnim.AnimatedDecoType.LAVA_CRUST, decoration.lava_crust, BlockDecoAnim.AnimatedDecoType.DRIP_WATER, decoration.water_drip, BlockDecoAnim.AnimatedDecoType.DRIP_LAVA, decoration.lava_drip);
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
