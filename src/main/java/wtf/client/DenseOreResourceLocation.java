package wtf.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import wtf.blocks.BlockDenseOre;

public class DenseOreResourceLocation extends ModelResourceLocation {
    public final BlockDenseOre ore;
    public final int meta;

    public DenseOreResourceLocation(BlockDenseOre ore, int meta) {
        super(ore.getRegistryName(), "density=" + meta);
        this.ore = ore;
        this.meta = meta;
    }
}
