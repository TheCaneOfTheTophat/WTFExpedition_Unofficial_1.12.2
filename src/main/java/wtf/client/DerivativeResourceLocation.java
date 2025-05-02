package wtf.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class DerivativeResourceLocation extends ModelResourceLocation {

    public final Block block;
    public final int meta;

    public DerivativeResourceLocation(Block block, String variantIn, int meta) {
        super(block.getRegistryName(), variantIn + "=" + meta);
        this.block = block;
        this.meta = meta;
    }

    public DerivativeResourceLocation(Block block, String variantIn) {
        super(block.getRegistryName(), variantIn);
        this.block = block;
        this.meta = 0;
    }
}
