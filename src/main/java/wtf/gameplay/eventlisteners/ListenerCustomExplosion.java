package wtf.gameplay.eventlisteners;

import net.minecraft.world.Explosion;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.gameplay.ExploderEntity;

public class ListenerCustomExplosion {

	@SubscribeEvent
	public void explosion(ExplosionEvent.Start event) {
		Explosion explosion = event.getExplosion();
		float size = ObfuscationReflectionHelper.getPrivateValue(Explosion.class, explosion, "size");
		boolean fire = ObfuscationReflectionHelper.getPrivateValue(Explosion.class, explosion, "causesFire");
		ExploderEntity entity = new ExploderEntity(event.getWorld(), explosion.getPosition(), size, 0, fire);
		event.getWorld().spawnEntity(entity);
		event.setCanceled(true);
	}
}
