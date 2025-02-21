package wtf.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OreEntry {
    private final String blockId = "";
    private final String name = "";

    private final String[] stoneList = {};

    private final int minAmountPerChunk = 0;
    private final int maxAmountPerChunk = 0;

    private final int surfaceHeightMinPercentage = 0;
    private final int surfaceHeightMaxPercentage = 100;

    private final int[] dimensionList = {};
    private final boolean dimensionListWhitelist = true;

    private final boolean useDenseBlock = false;

    private final String overlayPath = "";

    private final int veinPercentDensity = 100;

    private final String[] biomeTypeList = {};
    private final boolean biomeTypeListWhitelist = false;

    private final Map<String, Integer> percentGenerationPerBiomeType = new HashMap<>();

    private final GenerationSettings generation_settings = new GenerationSettings();

    public final Map<String, Integer> getPercentGenerationPerBiomeType() {
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
        private final GenerationTypes generation_types = new GenerationTypes();
        private final VeinFields vein_generation = new VeinFields();
        private final CloudFields cloud_generation = new CloudFields();
        private final CaveFields cave_generation = new CaveFields();
        private final VanillaFields vanilla_generation = new VanillaFields();
    }

    private static class GenerationTypes {
        private final boolean vanilla = false;
        private final boolean vein = false;
        private final boolean cloud = false;
        private final boolean cluster = false;
        private final boolean single = false;
        private final boolean cave = false;
        private final boolean underwater = false;
    }

    private static class VeinFields {
        private final double veinPitchAverage = 0;
        private final int veinLength = 0;
        private final int veinWidth = 0;
        private final int veinVerticalThickness = 0;
    }

    private static class CloudFields {
        private final int cloudDiameter = 0;
    }

    private static class CaveFields {
        private final boolean ceiling = false;
        private final boolean wall = false;
        private final boolean floor = false;
    }

    private static class VanillaFields {
        private final int blocksPerCluster = 0;
    }
}
