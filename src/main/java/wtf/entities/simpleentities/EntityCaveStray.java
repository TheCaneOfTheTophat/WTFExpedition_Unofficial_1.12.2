package wtf.entities.simpleentities;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.world.World;

public class EntityCaveStray extends EntityStray {

    public EntityCaveStray(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        EntitySkeleton skeleton = new EntitySkeleton(this.world);

        return skeleton.getCanSpawnHere();
    }
}
