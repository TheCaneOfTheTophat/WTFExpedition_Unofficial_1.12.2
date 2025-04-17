package wtf.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import wtf.enums.Modifier;

public class DummyModifierBlock extends Block {
    public final Modifier modifier;

    public DummyModifierBlock(Modifier modifier) {
        super(Material.AIR);
        this.modifier = modifier;
    }
}
