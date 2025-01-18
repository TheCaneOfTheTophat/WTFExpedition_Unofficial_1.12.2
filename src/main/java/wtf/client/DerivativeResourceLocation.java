package wtf.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import wtf.blocks.AbstractBlockDerivative;

public class DerivativeResourceLocation extends ModelResourceLocation {
    public final AbstractBlockDerivative block;
    public final int meta;

    public DerivativeResourceLocation(AbstractBlockDerivative block, String variantIn, int meta) {
        super(block.getRegistryName(), variantIn + "=" + meta);
        this.block = block;
        this.meta = meta;
    }
}
