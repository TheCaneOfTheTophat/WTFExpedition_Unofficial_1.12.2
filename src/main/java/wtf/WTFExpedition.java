package wtf;

import net.minecraft.item.ItemStack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import wtf.client.WTFModelRegistry;
import wtf.config.WTFExpeditionConfig;
import wtf.gameplay.eventlisteners.*;
import wtf.init.BlockSets;
import wtf.init.JSONLoader;
import wtf.init.WTFContent;
import wtf.network.WTFMessageBlockCrackEvent;
import wtf.worldgen.WorldGenListener;
import wtf.worldgen.ores.VanillaOreGenCatcher;
import wtf.worldgen.replacers.TorchReplacer;
import wtf.worldgen.trees.WorldGenTreeCancel;

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

	public static final SimpleNetworkWrapper CHANNEL_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(modID);
	
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

		UBC = Loader.isModLoaded("undergroundbiomes");

//		proxy.initWCICRender();
//		WTFRecipes.initRecipes();
//
//		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		proxy.preInitialization();
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event) {
		// Clear useless entry maps and sets

		JSONLoader.identifierToBlockEntry.clear();
		JSONLoader.blockEntries.clear();
		JSONLoader.oreEntries.clear();
		JSONLoader.blockGroups.clear();
		WTFModelRegistry.textureMap.clear();
		WTFContent.oreEntryMap.clear();
		WTFContent.blocks.clear();
		WTFContent.items.clear();

		CHANNEL_INSTANCE.registerMessage(WTFMessageBlockCrackEvent.Handler.class, WTFMessageBlockCrackEvent.class, 0, Side.CLIENT);

		BlockSets.initBlockSets();

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
			if (WTFExpeditionConfig.replaceTorches) {
				MinecraftForge.EVENT_BUS.register(new ListenerReplaceTorch());
				WTFExpedition.wtfLog.info("Finite torches enabled!");
				new TorchReplacer();
			}

			MinecraftForge.EVENT_BUS.register(new ListenerLoot());
		}

		MinecraftForge.EVENT_BUS.register(new WorldGenListener());

		if (WTFExpeditionConfig.oreGenEnabled) {
			MinecraftForge.ORE_GEN_BUS.register(new VanillaOreGenCatcher());
		}

		if (WTFExpeditionConfig.overworldGenerationEnabled) {
			if (WTFExpeditionConfig.bigTreesEnabled) {
				MinecraftForge.TERRAIN_GEN_BUS.register(new WorldGenTreeCancel());
			}
		}
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
//		if (GameplayConfig.wcictable)
//			RecipeParser.init();
	}
}
