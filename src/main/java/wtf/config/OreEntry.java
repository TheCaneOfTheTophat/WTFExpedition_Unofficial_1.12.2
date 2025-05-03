package wtf.config;

import java.util.ArrayList;

public class OreEntry {
    private final String blockId;
    private final String name;

    private final ArrayList<String> stoneList;
    private final String defaultStone;

    private final boolean useDenseBlock;

    private final boolean fracturesAdjacentBlocks;

    private final String overlayPath;

    private final ArrayList<OreGeneratorSettings> settings;

    public OreEntry(String blockId, String name, ArrayList<String> stoneList, String defaultStone, boolean useDenseBlock, boolean fracturesAdjacentBlocks, String overlayPath, ArrayList<OreGeneratorSettings> settings) {
        this.blockId = blockId;
        this.name = name;
        this.stoneList = stoneList;
        this.defaultStone = defaultStone;
        this.useDenseBlock = useDenseBlock;
        this.fracturesAdjacentBlocks = fracturesAdjacentBlocks;
        this.overlayPath = overlayPath;
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

    public String getDefaultStone() {
        return defaultStone;
    }

    public boolean usesDenseBlocks() {
        return useDenseBlock;
    }

    public boolean fracturesAdjacentBlocks() {
        return fracturesAdjacentBlocks;
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

    public ArrayList<OreGeneratorSettings> getGenerators() {
        return settings;
    }
}
