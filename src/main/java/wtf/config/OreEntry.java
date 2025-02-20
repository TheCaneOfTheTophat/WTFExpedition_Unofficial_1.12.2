package wtf.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OreEntry {
    private String blockId = "";
    private String name = "";

    private String[] stoneList = {};

    private int minAmountPerChunk = 0;
    private int maxAmountPerChunk = 0;

    private int surfaceHeightMinPercentage = 0;
    private int surfaceHeightMaxPercentage = 100;

    private int[] dimensionList = {};
    private boolean dimensionListWhitelist = true;

    private boolean useDenseBlock = false;

    private String overlayPath = "";

    private int veinPercentDensity = 100;

    private String[] biomeTypeList = {};
    private boolean biomeTypeListWhitelist = false;

    private Map<String, Integer> percentGenerationPerBiomeType = new HashMap<>();

    private GenerationSettings generation_settings = new GenerationSettings();

    public Map<String, Integer> getPercentGenerationPerBiomeType() {
        return percentGenerationPerBiomeType;
    }

    public boolean isBiomeTypeListWhitelist() {
        return biomeTypeListWhitelist;
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

    public boolean usesDenseBlocks() {
        return useDenseBlock;
    }

    public boolean isDimensionListWhitelist() {
        return dimensionListWhitelist;
    }

    public int[] getDimensionList() {
        return dimensionList;
    }

    public int getSurfaceHeightMaxPercentage() {
        return surfaceHeightMaxPercentage;
    }

    public int getSurfaceHeightMinPercentage() {
        return surfaceHeightMinPercentage;
    }

    public int getMaxAmountPerChunk() {
        return maxAmountPerChunk;
    }

    public int getMinAmountPerChunk() {
        return minAmountPerChunk;
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

    public boolean generatesVanilla() {
        return generation_settings.generation_types.vanilla;
    }

    public boolean generatesVein() {
        return generation_settings.generation_types.vein;
    }

    public boolean generatesCloud() {
        return generation_settings.generation_types.cloud;
    }

    public boolean generatesCluster() {
        return generation_settings.generation_types.cluster;
    }

    public boolean generatesSingle() {
        return generation_settings.generation_types.single;
    }

    public boolean generatesCave() {
        return generation_settings.generation_types.cave;
    }

    public boolean generatesUnderwater() {
        return generation_settings.generation_types.underwater;
    }

    public double getVeinPitch() {
        return generation_settings.vein_generation.veinPitchAverage;
    }

    public int getVeinLength() {
        return generation_settings.vein_generation.veinLength;
    }

    public int getVeinWidth() {
        return generation_settings.vein_generation.veinWidth;
    }

    public int getVeinVerticalThickness() {
        return generation_settings.vein_generation.veinVerticalThickness;
    }

    public int getCloudDiameter() {
        return generation_settings.cloud_generation.cloudDiameter;
    }

    public boolean generatesOnCeiling() {
        return generation_settings.cave_generation.ceiling;
    }

    public boolean generatesOnWall() {
        return generation_settings.cave_generation.wall;
    }

    public boolean generatesOnFloor() {
        return generation_settings.cave_generation.floor;
    }

    public int getBlocksPerCluster() {
        return generation_settings.vanilla_generation.blocksPerCluster;
    }

    private static class GenerationSettings {
        private GenerationTypes generation_types = new GenerationTypes();
        private VeinFields vein_generation = new VeinFields();
        private CloudFields cloud_generation = new CloudFields();
        private CaveFields cave_generation = new CaveFields();
        private VanillaFields vanilla_generation = new VanillaFields();
    }

    private static class GenerationTypes {
        private boolean vanilla = false;
        private boolean vein = false;
        private boolean cloud = false;
        private boolean cluster = false;
        private boolean single = false;
        private boolean cave = false;
        private boolean underwater = false;
    }

    private static class VeinFields {
        private double veinPitchAverage = 0;
        private int veinLength = 0;
        private int veinWidth = 0;
        private int veinVerticalThickness = 0;
    }

    private static class CloudFields {
        private int cloudDiameter = 0;
    }

    private static class CaveFields {
        private boolean ceiling = false;
        private boolean wall = false;
        private boolean floor = false;
    }

    private static class VanillaFields {
        private int blocksPerCluster = 0;
    }
}
