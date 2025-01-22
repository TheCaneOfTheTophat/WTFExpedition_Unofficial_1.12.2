package wtf;

import net.minecraft.item.ItemStack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import wtf.config.MasterConfig;
import wtf.config.GameplayConfig;
import wtf.init.JSONLoader;


@Mod(modid = WTFExpedition.modID, name = "WTF's Expedition", dependencies = "after:undergroundbiomes")
public class WTFExpedition {
	public static final String modID = "wtfexpedition";

	@SidedProxy(clientSide="wtf.client.ClientProxy", serverSide="wtf.CommonProxy")
	public static CommonProxy proxy;
	public static Logger coreLog;

	@Instance(modID)
	public static WTFExpedition instance;
	
	public static boolean UBC;

	public static CreativeTabs wtfTab = new CreativeTabs("WTFBlocks") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE));
		}		
	};

	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event) throws Exception {
		coreLog = event.getModLog();

		JSONLoader.loadJsonContent(event);

		// UBC = Loader.isModLoaded("undergroundbiomes");

		MasterConfig.loadConfig();
//		CaveBiomesConfig.customConfig();
//
//		if (UBC)
//			UBCCompat.loadUBCStone();
//		else
//			coreLog.info("Underground Biomes Construct not detected");
//
//		OverworldGenConfig.loadConfig();
        GameplayConfig.loadConfig();
//		WTFStoneRegistry.loadStoneReg();
//		BlockSets.initBlockSets();
//		proxy.initWCICRender();
//		WTFEntities.initEntites();
//		WTFRecipes.initRecipes();
//
//		if (MasterConfig.enableOverworldGeneration)
//			WTFBiomes.init();
//
//		if (MasterConfig.enableOreGen)
//			WTFOresNewConfig.loadConfig();
//
//		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
//
//		WTFSubstitutions.init();
//
		proxy.preInitialization();
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event) throws Exception {
//		EventListenerRegistry.initListeners();
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) throws Exception {
//		if (GameplayConfig.wcictable)
//			RecipeParser.init();
	}

}
