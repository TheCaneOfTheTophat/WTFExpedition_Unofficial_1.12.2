package wtf.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OreEntry {
    private final String blockId;
    private final String name;

    private final ArrayList<String> stoneList;

    private final int minAmountPerChunk;
    private final int maxAmountPerChunk;

    private final int surfaceHeightMinPercentage;
    private final int surfaceHeightMaxPercentage;

    private final ArrayList<Integer> dimensionList;
    private final boolean dimensionListWhitelist;

    private final boolean useDenseBlock;

    private final String overlayPath;

    private final int veinPercentDensity;

    private final ArrayList<String> biomeTypeList;
    private final boolean biomeTypeListWhitelist;

    private final HashMap<String, Integer> percentGenerationPerBiomeType;

    private final OreGeneratorSettings settings;

    public OreEntry(String blockId, String name, ArrayList<String> stoneList, int minAmountPerChunk, int maxAmountPerChunk, int surfaceHeightMinPercentage, int surfaceHeightMaxPercentage, ArrayList<Integer> dimensionList, boolean dimensionListWhitelist, boolean useDenseBlock, String overlayPath, int veinPercentDensity, ArrayList<String> biomeTypeList, boolean biomeTypeListWhitelist, HashMap<String, Integer> percentGenerationPerBiomeType, OreGeneratorSettings settings) {
        this.blockId = blockId;
        this.name = name;
        this.stoneList = stoneList;
        this.minAmountPerChunk = minAmountPerChunk;
        this.maxAmountPerChunk = maxAmountPerChunk;
        this.surfaceHeightMinPercentage = surfaceHeightMinPercentage;
        this.surfaceHeightMaxPercentage = surfaceHeightMaxPercentage;
        this.dimensionList = dimensionList;
        this.dimensionListWhitelist = dimensionListWhitelist;
        this.useDenseBlock = useDenseBlock;
        this.overlayPath = overlayPath;
        this.veinPercentDensity = veinPercentDensity;
        this.biomeTypeList = biomeTypeList;
        this.biomeTypeListWhitelist = biomeTypeListWhitelist;
        this.percentGenerationPerBiomeType = percentGenerationPerBiomeType;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public String getBlockId() {
        return blockId;
    }

    public ArrayList<String> getStoneList() {
        return stoneList;
    }

    public int getMinAmountPerChunk() {
        return minAmountPerChunk;
    }

    public int getMaxAmountPerChunk() {
        return maxAmountPerChunk;
    }

    public int getSurfaceHeightMinPercentage() {
        return surfaceHeightMinPercentage;
    }

    public int getSurfaceHeightMaxPercentage() {
        return surfaceHeightMaxPercentage;
    }

    public boolean usesDenseBlocks() {
        return useDenseBlock;
    }

    public ArrayList<Integer> getDimensionList() {
        return dimensionList;
    }

    public boolean isDimensionListWhitelist() {
        return dimensionListWhitelist;
    }

    public String getRawOverlayPath() {
        return overlayPath;
    }

    public ArrayList<String> getAllOverlayPaths() {
        ArrayList<String> paths = new ArrayList<>();

        for(int i = 0; i <= 2; i++)
            paths.add(overlayPath + i);

        return paths;
    }

    public int getVeinPercentDensity() {
        return veinPercentDensity;
    }

    public ArrayList<String> getBiomeTypeList() {
        return biomeTypeList;
    }

    public boolean isBiomeTypeListWhitelist() {
        return biomeTypeListWhitelist;
    }

    public Map<String, Integer> getPercentGenerationPerBiomeType() {
        return percentGenerationPerBiomeType;
    }

    public OreGeneratorSettings getSettings() {
        return settings;
    }
}
