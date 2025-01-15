package wtf.config;

import wtf.blocks.BlockDecoAnim;
import wtf.blocks.BlockDecoStatic;

public class BlockEntry {
    private String blockId;
    private String facturedBlockId;
    private String name;

    private boolean fracturesFirstWhenMined;
    private int percentageMineSpeedModiifier;

    private StaticDecor staticDecor = new StaticDecor();
    private AnimatedDecor animatedDecor = new AnimatedDecor();

    private boolean speleothems;

    public String getBlockId() {
        return blockId;
    }

    public String getFacturedBlockId() {
        return facturedBlockId;
    }

    public String getName() {
        return name;
    }

    public boolean fracturesFirstWhenMined() {
        return fracturesFirstWhenMined;
    }

    public int getPercentageMineSpeedModiifier() {
        return percentageMineSpeedModiifier;
    }

    public boolean getStaticDecorType(BlockDecoStatic.DecoType type) {
        switch(type) {
            case MOSS: return this.staticDecor.mossy;
            case SOUL: return this.staticDecor.soul;
            case CRACKED: return this.staticDecor.cracked;
            default: return false;
        }
    }

    public boolean getAnimatedDecorType(BlockDecoAnim.ANIMTYPE type) {
        switch(type) {
            case LAVA_CRUST: return this.animatedDecor.lava_crust;
            case DRIP_WATER: return this.animatedDecor.water_drip;
            case DRIP_LAVA: return this.animatedDecor.lava_drip;
            default: return false;
        }
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
