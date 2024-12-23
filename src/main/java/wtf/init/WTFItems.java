package wtf.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.*;
import net.minecraftforge.registries.IForgeRegistry;
import wtf.Core;
import wtf.config.GameplayConfig;
import wtf.config.MasterConfig;
import wtf.items.HomeScroll;
import wtf.items.SimpleItem;

@Mod.EventBusSubscriber
@ObjectHolder(Core.coreID)
public class WTFItems {
	public static final Item sulfur = null;
	public static final Item nitre = null;
	public static final Item home_scroll = null;

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> register) {
		IForgeRegistry<Item> registry = register.getRegistry();

		registry.register(new SimpleItem("nitre"));
		registry.register(new SimpleItem("sulfur"));

		if(MasterConfig.gameplaytweaks && GameplayConfig.homescroll)
			registry.register(new HomeScroll());
	}
}
