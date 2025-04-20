package wtf.config;

import wtf.enums.Modifier;

import java.util.HashMap;

public class BlockEntry {

    private final String blockId;
    private final String fracturedBlockId;
    private final String name;
    private final String texture;

    private final boolean fracturesFirstWhenMined;

    private final int percentageMineSpeedModifier;
    private final int percentageStability;

    private final HashMap<Modifier, String> modifiers;

    private final boolean speleothems;
    private final boolean irreplaceable;
    private final boolean noAddons;

    public BlockEntry(String blockId, String fracturedBlockId, String name, String texture, boolean fracturesFirstWhenMined, int percentageMineSpeedModifier, int percentageStability, HashMap<Modifier, String> modifiers, boolean speleothems, boolean irreplaceable, boolean noAddons) {
        this.blockId = blockId;
        this.fracturedBlockId = fracturedBlockId;
        this.name = name;
        this.texture = texture;
        this.fracturesFirstWhenMined = fracturesFirstWhenMined;
        this.speleothems = speleothems;
        this.modifiers = modifiers;
        this.percentageMineSpeedModifier = percentageMineSpeedModifier;
        this.percentageStability = percentageStability;
        this.irreplaceable = irreplaceable;
        this.noAddons = noAddons;
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
        return fracturesFirstWhenMined;
    }

    public float getPercentageMineSpeedModifier() {
        return percentageMineSpeedModifier / 100F;
    }

    public int getPercentageStability() {
        return percentageStability;
    }

    public HashMap<Modifier, String> getModifiers() {
        return modifiers;
    }

    public boolean hasSpeleothems() {
        return speleothems;
    }

    public boolean isIrreplaceable() {
        return irreplaceable;
    }

    public boolean hasNoAddons() {
        return noAddons;
    }
}
