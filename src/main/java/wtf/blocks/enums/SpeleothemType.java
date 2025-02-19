package wtf.blocks.enums;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public enum SpeleothemType implements IStringSerializable {
    stalactite_small(0, "stalactite_small", new AxisAlignedBB(0.2F, 0.2F, 0.2F, 0.8F, 1F, 0.8F)),
    stalactite_base(1, "stalactite_base", new AxisAlignedBB(0.2F, 0F, 0.2F, .8F, 1F, .8F)),
    stalactite_tip(2, "stalactite_tip", new AxisAlignedBB(0.3F, 0.4F, 0.3F, 0.7F, 1F, 0.7F)),
    speleothem_column(3, "speleothem_column", new AxisAlignedBB(0.3F, 0F, 0.3F, 0.7F, 1F, 0.7F)),
    stalagmite_small(4, "stalagmite_small", new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.8F, 0.8F)),
    stalagmite_base(5, "stalagmite_base", new AxisAlignedBB(0.2F, 0F, 0.2F, .8F, 1F, .8F)),
    stalagmite_tip(6, "stalagmite_tip", new AxisAlignedBB(0.3F, 0F, 0.3F, 0.7F, 0.7F, 0.7F));

    private final int ID;
    private final String name;
    public final AxisAlignedBB boundingBox;

    SpeleothemType(int ID, String name, AxisAlignedBB box) {
        this.ID = ID;
        this.name = name;
        this.boundingBox = box;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return getName();
    }
}
