package wtf;

import net.minecraft.item.ItemStack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.eventlisteners.*;
import wtf.init.BlockSets;
import wtf.init.JSONLoader;
import wtf.init.LootEventListener;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


@Mod(modid = WTFExpedition.modID, dependencies = "after:undergroundbiomes")
@SuppressWarnings({"unused", "ConstantConditions"})
public class WTFExpedition {
	public static final String modID = "wtfexpedition";
	public static String configDirectoryString;
	public static Path configDirectory;

	@SidedProxy(clientSide="wtf.client.ClientProxy", serverSide="wtf.CommonProxy")
	public static CommonProxy proxy;

	public static Logger wtfLog;

	@Instance(modID)
	public static WTFExpedition instance;
	
	public static boolean UBC;

	public static CreativeTabs wtfTab = new CreativeTabs("WTFExpedition") {
		@Override
		@Nonnull
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE));
		}
	};

	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		wtfLog = event.getModLog();

		configDirectory = Paths.get(event.getModConfigurationDirectory().toString(), "WTFExpedition");
		configDirectoryString = configDirectory.toString();

		WTFExpeditionConfig.config = new Configuration(new File(configDirectoryString, "WTFExpedition.cfg"), true);
		WTFExpeditionConfig.syncConfig();

		JSONLoader.loadJsonContent();

		// UBC = Loader.isModLoaded("undergroundbiomes");

//
//		if (UBC)
//			UBCCompat.loadUBCStone();
//		else
//			coreLog.info("Underground Biomes Construct not detected");
//
		BlockSets.initBlockSets();
//		proxy.initWCICRender();
//		WTFEntities.initEntites();
//		WTFRecipes.initRecipes();
//
//		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
//
//		WTFSubstitutions.init();
//
		proxy.preInitialization();
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event) {
//		MinecraftForge.EVENT_BUS.register(new CoreWorldGenListener());
//		MinecraftForge.TERRAIN_GEN_BUS.register(new CoreWorldGenListener());

//		MinecraftForge.EVENT_BUS.register(new TickGenBuffer());

		// GAMEPLAY TWEAKS
		if (WTFExpeditionConfig.gameplayTweaksEnabled) {
			if (WTFExpeditionConfig.modifyCropGrowth) {
				MinecraftForge.EVENT_BUS.register(new ListenerPlantGrowth());
				WTFExpedition.wtfLog.info("Plant growth modification enabled!");
			}
			if (WTFExpeditionConfig.waterSourceControl) {
				MinecraftForge.EVENT_BUS.register(new ListenerWaterSpawn());
				WTFExpedition.wtfLog.info("Water source control enabled!");
			}
			if (WTFExpeditionConfig.miningStoneFractures) {
				MinecraftForge.EVENT_BUS.register(new ListenerBreakFracture());
				WTFExpedition.wtfLog.info("Stone fracturing enabled!");
			}
			if (WTFExpeditionConfig.miningOreFractures) {
				MinecraftForge.EVENT_BUS.register(new ListenerOreFracture());
				WTFExpedition.wtfLog.info("Ore fracturing enabled!");
			}
			if (WTFExpeditionConfig.miningSpeedModificationEnabled) {
				MinecraftForge.EVENT_BUS.register(new ListenerMiningSpeed());
				WTFExpedition.wtfLog.info("Mining speed modification enabled!");
			}
			if (WTFExpeditionConfig.customExplosions) {
				MinecraftForge.EVENT_BUS.register(new ListenerCustomExplosion());
				WTFExpedition.wtfLog.info("Custom explosions enabled!");
			}
			if (WTFExpeditionConfig.additionalBlockGravity) {
				MinecraftForge.EVENT_BUS.register(new ListenerGravity());
				WTFExpedition.wtfLog.info("Additional block gravity enabled!");
			}
			if (WTFExpeditionConfig.featherDropRate > 0) {
				MinecraftForge.EVENT_BUS.register(new ListenerChickenDrops());
				WTFExpedition.wtfLog.info("Chicken feather drops enabled!");
			}
			if (WTFExpeditionConfig.stickDropPercentage > 0) {
				MinecraftForge.EVENT_BUS.register(new ListenerLeafDrops());
				WTFExpedition.wtfLog.info("Stick drop modifier enabled!");
			}
			if (WTFExpeditionConfig.playerlessMobDropPercentage > 0) {
				MinecraftForge.EVENT_BUS.register(new ListenerEntityDrops());
				WTFExpedition.wtfLog.info("Non-player killed mob loot modifier enabled!");
			}

			MinecraftForge.EVENT_BUS.register(new LootEventListener());
		}

//		if (WTFExpeditionConfig.oreGenEnabled) {
//			MinecraftForge.ORE_GEN_BUS.register(new VanillOreGenCatcher());
//		}
//
//		if (WTFExpeditionConfig.overworldGenerationEnabled){
//			if (WTFExpeditionConfig.bigTreesEnabled){
//				MinecraftForge.TERRAIN_GEN_BUS.register(new WorldGenTreeCancel());
//			}
//		}
//
//		if (WTFExpeditionConfig.replaceSandstone) {
//			MinecraftForge.TERRAIN_GEN_BUS.register(new SandstoneNaturaliser());
//			MinecraftForge.EVENT_BUS.register(new SandstoneNaturaliser());
//		}
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
//		if (GameplayConfig.wcictable)
//			RecipeParser.init();
	}

}
