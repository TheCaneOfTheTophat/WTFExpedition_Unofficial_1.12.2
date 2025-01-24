package wtf.init;

import net.minecraftforge.common.MinecraftForge;
import wtf.WTFExpedition;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.eventlisteners.ListenerBlockNameGetter;
import wtf.gameplay.eventlisteners.ListenerChickenDrops;
import wtf.gameplay.eventlisteners.ListenerCustomExplosion;
import wtf.gameplay.eventlisteners.ListenerEntityDrops;
import wtf.gameplay.eventlisteners.ListenerGravity;
import wtf.gameplay.eventlisteners.ListenerLeafDrops;
import wtf.gameplay.eventlisteners.ListenerMiningSpeed;
import wtf.gameplay.eventlisteners.ListenerOreFrac;
import wtf.gameplay.eventlisteners.ListenerPlantGrowth;
import wtf.gameplay.eventlisteners.ListenerStoneFrac;
import wtf.gameplay.eventlisteners.ListenerWaterSpawn;
import wtf.gameplay.eventlisteners.ZombieListener;
import wtf.ores.VanillOreGenCatcher;
import wtf.worldgen.CoreWorldGenListener;
import wtf.worldgen.SandstoneNaturaliser;
import wtf.worldgen.generators.TickGenBuffer;
import wtf.worldgen.trees.WorldGenTreeCancel;

public class EventListenerRegistry {

	public static void initListeners(){

		MinecraftForge.EVENT_BUS.register(new CoreWorldGenListener());
		MinecraftForge.TERRAIN_GEN_BUS.register(new CoreWorldGenListener());
		
		MinecraftForge.EVENT_BUS.register(new TickGenBuffer());
		
		if (WTFExpeditionConfig.gameplayTweaksEnabled){
			if (WTFExpeditionConfig.miningSpeedModificationEnabled){
				MinecraftForge.EVENT_BUS.register(new ListenerMiningSpeed());
				WTFExpedition.wtfLog.info("mining speed listener registered");
			}
			if (WTFExpeditionConfig.miningOreFractures){
				MinecraftForge.EVENT_BUS.register(new ListenerOreFrac());
				WTFExpedition.wtfLog.info("ore fracturing listener registered");
			}
			if (WTFExpeditionConfig.miningStoneFractures){
				MinecraftForge.EVENT_BUS.register(new ListenerStoneFrac());
				WTFExpedition.wtfLog.info("stone fracturing listener registered");
			}

			if (WTFExpeditionConfig.customExplosions){
				MinecraftForge.EVENT_BUS.register(new ListenerCustomExplosion());
				WTFExpedition.wtfLog.info("custom explosion listener registered");
			}
			if (WTFExpeditionConfig.additionalBlockGravity){
				MinecraftForge.EVENT_BUS.register(new ListenerGravity());
				WTFExpedition.wtfLog.info("block gravity listener registered");
			}
			if (WTFExpeditionConfig.stickDropPercentage > 0){
				MinecraftForge.EVENT_BUS.register(new ListenerLeafDrops());
				WTFExpedition.wtfLog.info("Leaves drop sticks listener registered");
			}
			if (WTFExpeditionConfig.featherDropRate > 0){
				MinecraftForge.EVENT_BUS.register(new ListenerChickenDrops());
				WTFExpedition.wtfLog.info("Chickens drop feathers listener registered");
			}
			if (WTFExpeditionConfig.waterSourceControl){
				MinecraftForge.EVENT_BUS.register(new ListenerWaterSpawn());
				WTFExpedition.wtfLog.info("Water spawn controller listener registered");
			}
			if (WTFExpeditionConfig.modifyCropGrowth){
				MinecraftForge.EVENT_BUS.register(new ListenerPlantGrowth());
				WTFExpedition.wtfLog.info("Plang growth speed modifier listener registered");
			}
			if (WTFExpeditionConfig.playerlessMobDropPercentage > 0){
				MinecraftForge.EVENT_BUS.register(new ListenerEntityDrops());
			}		
			
			MinecraftForge.EVENT_BUS.register(new LootEventListener());
		}
		
		if (WTFExpeditionConfig.oreGenEnabled){
			MinecraftForge.ORE_GEN_BUS.register(new VanillOreGenCatcher());
		}

		if (WTFExpeditionConfig.preventBabyZombies){
			MinecraftForge.EVENT_BUS.register(new ZombieListener());
		}

	
		if (WTFExpeditionConfig.overworldGenerationEnabled){
			if (WTFExpeditionConfig.bigTreesEnabled){
				MinecraftForge.TERRAIN_GEN_BUS.register(new WorldGenTreeCancel());
			}
		}
		
		if (WTFExpeditionConfig.replaceSandstone) {
			MinecraftForge.TERRAIN_GEN_BUS.register(new SandstoneNaturaliser());
			MinecraftForge.EVENT_BUS.register(new SandstoneNaturaliser());
			//GameRegistry.registerWorldGenerator(new SandstoneNaturaliser(), 0);
			//Core.coreLog.info("Sandstone Naturaliser Registered");
		}

		if(WTFExpeditionConfig.enableNameGetter){
			MinecraftForge.EVENT_BUS.register(new ListenerBlockNameGetter());
			WTFExpedition.wtfLog.info("Registery name getter enabled");
		}
		


		
	}

}
