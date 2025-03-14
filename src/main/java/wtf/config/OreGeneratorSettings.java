package wtf.config;

public class OreGeneratorSettings {

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

    public OreGeneratorSettings(String primary, String secondary, double veinPitchAverage, int veinLength, int veinWidth, int veinVerticalThickness, int cloudDiameter, boolean ceiling, boolean wall, boolean floor, int blocksPerCluster) {
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
