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


@Mod (modid = Core.coreID, name = "WTFs Expedition", dependencies = "after:undergroundbiomes")

public class Core {
	public static  final String coreID = "wtfcore";

	@SidedProxy(clientSide="wtf.client.ClientProxy", serverSide="wtf.CommonProxy")
	public static CommonProxy proxy;

	public static Logger coreLog;

	@Instance(coreID)
	public static Core instance;
	
	public static boolean UBC;

	public static CreativeTabs wtfTab = new CreativeTabs("WTFBlocks") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE));
		}		
	};

	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent) throws Exception {
//		coreLog = preEvent.getModLog();
//
//		proxy.writeResourcePack();
//		proxy.loadLangFile();
//
//		UBC = Loader.isModLoaded("undergroundbiomes");
//
		MasterConfig.loadConfig();
//
//
//		CaveBiomesConfig.customConfig();
//
//		if (UBC)
//			UBCCompat.loadUBCStone();
//		else
//			coreLog.info("Underground Biomes Construct not detected");
//
//		OverworldGenConfig.loadConfig();
        GameplayConfig.loadConfig();
//		WTFBlocks.initSimpleBlocks();
//		WTFStoneRegistry.loadStoneReg();
//		BlockSets.initBlockSets();
//		WTFBlocks.initDependentBlocks();
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
//		proxy.finishLangFile();
	}
	
	@EventHandler public void load(FMLInitializationEvent event) throws Exception {
//		EventListenerRegistry.initListeners();
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent) throws Exception {
//		if (MasterConfig.doResourcePack)
//			proxy.enableBlockstateTexturePack();

//		if (GameplayConfig.wcictable)
//			RecipeParser.init();
	}
}
