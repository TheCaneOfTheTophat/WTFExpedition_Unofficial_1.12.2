package wtf.entities.simpleentities;

import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityCaveHusk extends EntityHusk {

    public EntityCaveHusk(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        EntityZombie zombie = new EntityZombie(this.world);

        return zombie.getCanSpawnHere();
    }
}
