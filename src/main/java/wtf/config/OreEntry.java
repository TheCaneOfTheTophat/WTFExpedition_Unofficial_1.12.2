package wtf.config;

import java.util.ArrayList;
import java.util.Map;

public class OreEntry {
    private String blockId = "";
    private String name = "";

    private String[] stoneList = {};
    private String stoneListMode = "whitelist";

    private int amountMin = 0;
    private int amountMax = 0;

    private int surfaceHeightMinPercentage = 0;
    private int SurfaceHeightMaxPercentage = 100;

    private int[] dimensionList = {};
    private String dimensionListMode = "whitelist";

    private boolean useDenseBlock = false;

    private String overlayPath = "";

    private int veinPercentDensity = 100;

    private String[] biomeTypeList = {};
    private String biomeTypeListMode = "whitelist";

    private Map<String, Integer> percentGenerationPerBiomeType = null;

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
