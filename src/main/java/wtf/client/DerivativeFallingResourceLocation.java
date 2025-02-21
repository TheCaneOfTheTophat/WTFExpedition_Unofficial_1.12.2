package wtf.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import wtf.blocks.AbstractBlockDerivativeFalling;

public class DerivativeFallingResourceLocation extends ModelResourceLocation {

    public final AbstractBlockDerivativeFalling block;
    public final int meta;

    public DerivativeFallingResourceLocation(AbstractBlockDerivativeFalling block, String variantIn, int meta) {
        super(block.getRegistryName() + "_hardcoded", variantIn + "=" + meta);
        this.block = block;
        this.meta = meta;
    }
}
