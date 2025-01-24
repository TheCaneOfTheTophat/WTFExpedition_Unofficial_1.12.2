package wtf.gameplay.eventlisteners;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.config.WTFExpeditionConfig;

public class ListenerChickenDrops {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!event.getEntity().world.isRemote && (event.getEntity() instanceof EntityChicken)){
			EntityChicken chicken = (EntityChicken) event.getEntity();
			if (!chicken.isChild()){

				if(event.getEntity().getEntityWorld().rand.nextInt(WTFExpeditionConfig.featherDropRate) == 1){
					chicken.dropItem(Items.FEATHER, 1);
				}
			}
		}
	}
	
}
