package wtf.config;

import java.util.ArrayList;
import java.util.Map;

public class OreEntry {
    private String blockId;
    private String name;

    private String[] stoneList;
    private String stoneListMode;

    private int amountMin;
    private int amountMax;

    private int surfaceHeightMinPercentage;
    private int SurfaceHeightMaxPercentage;

    private int[] dimensionList;
    private String dimensionListMode;

    private boolean useDenseBlock;

    private String overlayPath;

    private int veinPercentDensity;

    private String[] biomeTypeList;
    private String biomeTypeListMode;

    private Map<String, Integer> percentGenerationPerBiomeType;

    public Map<String, Integer> getPercentGenerationPerBiomeType() {
        return percentGenerationPerBiomeType;
    }

    public String getBiomeTypeListMode() {
        return biomeTypeListMode;
    }

    public String[] getBiomeTypeList() {
        return biomeTypeList;
    }

    public int getVeinPercentDensity() {
        return veinPercentDensity;
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

    public boolean hasDenseBlocks() {
        return useDenseBlock;
    }

    public String getDimensionListMode() {
        return dimensionListMode;
    }

    public int[] getDimensionList() {
        return dimensionList;
    }

    public int getSurfaceHeightMaxPercentage() {
        return SurfaceHeightMaxPercentage;
    }

    public int getSurfaceHeightMinPercentage() {
        return surfaceHeightMinPercentage;
    }

    public int getAmountMax() {
        return amountMax;
    }

    public int getAmountMin() {
        return amountMin;
    }

    public String getStoneListMode() {
        return stoneListMode;
    }

    public String[] getStoneList() {
        return stoneList;
    }

    public String getName() {
        return name;
    }

    public String getBlockId() {
        return blockId;
    }
}
