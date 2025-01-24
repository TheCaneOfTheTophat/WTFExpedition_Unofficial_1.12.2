package wtf;

import net.minecraft.item.ItemStack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
import wtf.init.JSONLoader;

import java.io.File;
import java.nio.file.Paths;


@Mod(modid = WTFExpedition.modID, dependencies = "after:undergroundbiomes")
public class WTFExpedition {
	public static final String modID = "wtfexpedition";
	public static String configDirectory;

	@SidedProxy(clientSide="wtf.client.ClientProxy", serverSide="wtf.CommonProxy")
	public static CommonProxy proxy;

	public static Logger wtfLog;

	@Instance(modID)
	public static WTFExpedition instance;
	
	public static boolean UBC;

	public static CreativeTabs wtfTab = new CreativeTabs("WTFExpedition") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE));
		}
	};

	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		wtfLog = event.getModLog();

		WTFExpedition.configDirectory = Paths.get(event.getModConfigurationDirectory().toString(), "WTFExpedition").toString();

		WTFExpeditionConfig.config = new Configuration(new File(configDirectory, "WTFExpedition.cfg"), true);
		WTFExpeditionConfig.syncConfig();

		JSONLoader.loadJsonContent();

		// UBC = Loader.isModLoaded("undergroundbiomes");

//
//		if (UBC)
//			UBCCompat.loadUBCStone();
//		else
//			coreLog.info("Underground Biomes Construct not detected");
//
//		WTFStoneRegistry.loadStoneReg();
//		BlockSets.initBlockSets();
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
//		EventListenerRegistry.initListeners();
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
//		if (GameplayConfig.wcictable)
//			RecipeParser.init();
	}

}
