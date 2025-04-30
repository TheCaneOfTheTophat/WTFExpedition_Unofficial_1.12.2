package wtf.config;

import java.util.ArrayList;
import java.util.HashMap;

public class OreGeneratorSettings {

    public final String name;

    public final int minAmountPerChunk;
    public final int maxAmountPerChunk;

    public final int surfaceHeightMinPercentage;
    public final int surfaceHeightMaxPercentage;

    public final int minY;
    public final int maxY;

    public final ArrayList<Integer> dimensionList;
    public final boolean dimensionListWhitelist;

    public final int veinPercentDensity;

    public final ArrayList<String> biomeTypeList;
    public final boolean biomeTypeListWhitelist;
    public final int biomeLeniency;

    public final HashMap<String, Integer> percentGenerationPerBiomeType;

    public final String primaryGenerationType;
    public final String secondaryGenerationType;

    // Vein fields
    public final double veinPitchAverage;
    public final int veinLength;
    public final int veinWidth;
    public final int veinVerticalThickness;

    // Cloud fields
    public final int cloudDiameter;

    // Cave fields
    public final boolean ceiling;
    public final boolean wall;
    public final boolean floor;

    // Vanilla fields
    public final int blocksPerCluster;

    public OreGeneratorSettings(String name, int minAmountPerChunk, int maxAmountPerChunk, int surfaceHeightMinPercentage, int surfaceHeightMaxPercentage, int minY, int maxY, ArrayList<Integer> dimensionList, boolean dimensionListWhitelist, int veinPercentDensity, ArrayList<String> biomeTypeList, boolean biomeTypeListWhitelist, int biomeLeniency, HashMap<String, Integer> percentGenerationPerBiomeType, String primary, String secondary, double veinPitchAverage, int veinLength, int veinWidth, int veinVerticalThickness, int cloudDiameter, boolean ceiling, boolean wall, boolean floor, int blocksPerCluster) {
        this.name = name;
        this.minAmountPerChunk = minAmountPerChunk;
        this.maxAmountPerChunk = maxAmountPerChunk;
        this.surfaceHeightMinPercentage = surfaceHeightMinPercentage;
        this.surfaceHeightMaxPercentage = surfaceHeightMaxPercentage;
        this.minY = minY;
        this.maxY = maxY;
        this.dimensionList = dimensionList;
        this.dimensionListWhitelist = dimensionListWhitelist;
        this.veinPercentDensity = veinPercentDensity;
        this.biomeTypeList = biomeTypeList;
        this.biomeTypeListWhitelist = biomeTypeListWhitelist;
        this.biomeLeniency = biomeLeniency;
        this.percentGenerationPerBiomeType = percentGenerationPerBiomeType;
        this.primaryGenerationType = primary;
        this.secondaryGenerationType = secondary;
        this.veinPitchAverage = veinPitchAverage;
        this.veinLength = veinLength;
        this.veinWidth = veinWidth;
        this.veinVerticalThickness = veinVerticalThickness;
        this.cloudDiameter = cloudDiameter;
        this.ceiling = ceiling;
        this.wall = wall;
        this.floor = floor;
        this.blocksPerCluster = blocksPerCluster;
    }
}
