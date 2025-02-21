package wtf.blocks.enums;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public enum IcicleType implements IStringSerializable {

    icicle_small(0, "stalactite_small", new AxisAlignedBB(0.2F, 0.2F, 0.2F, 0.8F, 1F, 0.8F)),
    icicle_base(1, "stalactite_base", new AxisAlignedBB(0.2F, 0F, 0.2F, .8F, 1F, .8F)),
    icicle_tip(2, "stalactite_tip", new AxisAlignedBB(0.3F, 0.4F, 0.3F, 0.7F, 1F, 0.7F));

    private final int ID;
    private final String name;
    public final AxisAlignedBB boundingBox;

    IcicleType(int ID, String name, AxisAlignedBB box) {
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
