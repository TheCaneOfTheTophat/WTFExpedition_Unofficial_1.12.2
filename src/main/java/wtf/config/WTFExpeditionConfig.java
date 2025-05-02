package wtf.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import wtf.init.BlockSets;

public class WTFExpeditionConfig {

	// Master
	public static boolean gameplayTweaksEnabled;
	public static boolean caveGenerationEnabled;
	public static boolean dungeonGenerationEnabled;
	public static boolean oreGenEnabled;
	public static boolean overworldGenerationEnabled;

	// Cave Biomes
	public static boolean enableDungeons;
	public static boolean enableSubtypes;
	public static double subtypeChance;
	public static boolean enablePuddles;

	// Tweaks: Miscellaneous
	public static double cropGrowthPercentModifier;
	public static boolean modifyCropGrowth;
	public static boolean waterSourceControl;

	// Tweaks: Mining
	public static boolean miningStoneFractures;
	public static boolean miningOreFractures;
	public static boolean simpleFracturing;
	public static boolean miningSpeedModificationEnabled;
	public static boolean modifyHammerBehaviour;

	// Tweaks: Explosives
	public static boolean customExplosions;
	public static boolean explosionsFracture;
	public static double atomizingExplosionLevel;
	public static double droppingExplosionLevel;
	public static double explosionDamageModifier;
	public static double explosionForceModifier;
	public static double creeperUpwardModifier;

	// Tweaks: Gravity
	public static boolean additionalBlockGravity;
	public static boolean fallingBlocksDamage;
	public static boolean antiNerdPole;

	// Tweaks: Loot and Crafting
	public static int featherDropRate;
	public static int stickDropPercentage;
	public static int playerlessMobDropPercentage;
	public static boolean wcicTableEnabled;
	public static boolean homeScrollsEnabled;
	public static boolean sulfurRecipeEnabled;
	public static boolean removeVanillaTools;

	// Tweaks: Torches
	public static boolean replaceTorches;
	public static int torchLifespan;
	public static int torchRange;
	public static boolean relightTorchByHand;
	public static int litTorchLight;
	public static int extinguishedTorchLight;
	
	// Ores
	public static boolean simplexOreGen;

	// Overworld: Miscellaneous
	public static boolean replaceLavaWithWater;
	public static boolean replaceLavaWithObsidian;

	// Overworld : Surface modification
	public static boolean enableSurfaceModification;
	public static double forestMossyChunkPercent;
	public static double forestMossyBlockPercent;
	public static double mountainFracturedChunkPercent;
	public static double mountainFracturedBlockPercent;
	public static double riverFracturedChunkPercent;
	public static double riverFracturedBlockPercent;

	// Overworld: Sub-Biomes - Autumn Forest
	public static int autumnForestID;
	public static int autumnHillsID;
	public static int autumnForestPercentageFrequency;
	public static int autumnForestSize;
	public static int autumnForestColorScale;

	// Overworld: Big Trees
	public static boolean bigTreesEnabled;
	public static double bigTreeReplacementPercentage;
	public static double simplexBigTreeScale;
	public static boolean shrinkMassiveTrees;
	
	// General Miscellaneous
	public static boolean loadDefaultFiles;
	
	public static Configuration config;

	// TODO Bring back block state rotations

	public static void syncConfig() {
		config.load();
		
		/* #####################################
		                  MASTER
		   ##################################### */
		List<String> propertyOrder = new ArrayList<>();
		String category = "1 : master";

		config.setCategoryComment("1 : master", "Configurations for all modules of WTFExpedition.");

		Property property = config.get(category, "Set to false to disable all gameplay tweaks for a vanilla gameplay experience", true);
		gameplayTweaksEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Set to false to disable all cave decoration", true);
		caveGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Set to false to disable all dungeon and cave subtype generation", true);
		dungeonGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Set to false to disable WTFExpedition's custom ore generation completely", true);
		oreGenEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Set to false to disable all Overworld generation added by WTFExpedition", true);
		overworldGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		                CAVE BIOMES
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "2 : cave biomes";

		config.setCategoryComment(category, "Configurations for the Cave Biomes module of WTFExpedition.");

		property = config.get(category, "Allow generation of custom mob based dungeons", true);
		enableDungeons = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Allow generation of cave subtypes", true);
		enableSubtypes = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Percent generation chance of cave subtypes and dungeons", 50);
		subtypeChance = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Allow generation of puddles in wet cave biomes", true);
		enablePuddles = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		               TWEAKS: MISC
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3a : tweaks";

		config.setCategoryComment(category, "Configurations for the Tweaks module of WTFExpedition.");

		property = config.get(category, "Modify crop growth rates and harvest according to biome type", true);
		modifyCropGrowth = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Growth rate percent modifier for crops", 10);
		cropGrowthPercentModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Prevent infinite water source blocks outside biomes with the WET type", false);
		waterSourceControl = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		              TWEAKS: MINING
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3b : tweaks - mining";

		property = config.get(category, "Stone fractures upon mining. Individual stones can be disabled in the block registry", true);
		miningStoneFractures = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Ores fracture adjacent stone upon mining. Individual blocks can be removed from the config list", true);
		miningOreFractures = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Use simplified fracturing (always fractures all 6 adjacent blocks)", false);
		simpleFracturing = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable mining speed modification. Values are set in the block registry", true);
		miningSpeedModificationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Modify hammer behaviour", true);
		modifyHammerBehaviour = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Blocks that fracture adjacent stone when mined", new String[] {"minecraft:coal_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:lapis_ore", "minecraft:diamond_ore", "minecraft:redstone_ore", "minecraft:lit_redstone_ore", "minecraft:emerald_ore"});
        BlockSets.adjacentFracturingBlocks.addAll(Arrays.asList(property.getStringList()));
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		            TWEAKS: EXPLOSIVES
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3c : tweaks - explosives";

		property = config.get(category, "Override explosions with Expedition's custom explosions. All other explosion-based values require custom explosions to be turned on", true);
		customExplosions = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Explosions fracture stone", true);
		explosionsFracture = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Explosion level above which blocks atomize, and below which they drop", 5);
		atomizingExplosionLevel = property.getDouble();
		propertyOrder.add(property.getName());

		property = config.get(category, "Explosion level above which blocks drop, and below which they fracture (if fracturing on- if off nothing happens below)", 1);
		droppingExplosionLevel = property.getDouble();
		propertyOrder.add(property.getName());

		property = config.get(category, "Explosion damage percentage modifier", 100);
		explosionDamageModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Explosion force percentage modifier", 100);
		explosionForceModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Creeper explosion upward modifier. Increase to have creeper explosions explode more upwards", 1.5);
		creeperUpwardModifier = property.getDouble() * 100;
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		              TWEAKS: GRAVITY
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3d : tweaks - gravity";

		property = config.get(category, "Enable gravity for additional blocks", true);
		additionalBlockGravity = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable damage from falling blocks", true);
		fallingBlocksDamage = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Nerdpole prevention: Prevent indefinite stacking of non-stable blocks, causing them to slide off", true);
		antiNerdPole = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		        TWEAKS: LOOT AND CRAFTING
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3d : tweaks - loot & crafting";

		property = config.get(category, "Average number of minutes between chickens dropping feathers", 30);
		featherDropRate = property.getInt() * 20 * 60;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of leaf blocks that drop extra sticks when broken", 50);
		stickDropPercentage = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "When not killed by a player, mobs only drop items this percentage of the time", 25);
		playerlessMobDropPercentage = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Crafting Recipe Table", true);
		wcicTableEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable home scrolls", true);
		homeScrollsEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Netherrack --> Sulfur recipe", true);
		sulfurRecipeEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Remove recipes for vanilla tools (Requires Tinkers Construct to be installed)", false);
		removeVanillaTools = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		/* #####################################
		              TWEAKS: TORCHES
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "3e : tweaks - torches";

		property = config.get(category, "Replace vanilla torches with finite torches", false);
		replaceTorches = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Chance in 100 per block tick (avg every 45 seconds) that an unattended torch will go out", 20);
		torchLifespan = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Number of blocks a player must be within to prevent a torch from going out", 20);
		torchRange = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Torches can be relit by hand (true), or require flint and steel (false)", true);
		relightTorchByHand = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Light level of lit torches", 14);
		litTorchLight = Math.max(Math.min(property.getInt(), 15), 0);
		propertyOrder.add(property.getName());

		property = config.get(category, "Light level of extinguished torches", 2);
		extinguishedTorchLight = Math.max(Math.min(property.getInt(), 15), 0);
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		/* #####################################
		                   ORES
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "4 : ores";

		config.setCategoryComment(category, "Configurations for the Ores module of WTFExpedition.");

		property = config.get(category, "Use simplex noise instead of random for ore generation", true);
		simplexOreGen = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		              OVERWORLD: MISC
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "5a : overworld";

		config.setCategoryComment(category, "Configurations for the Overworld module of WTFExpedition.");

		property = config.get(category, "Replace lava with water in ocean biomes", false);
		replaceLavaWithWater = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Replace lava with obsidian in cold biomes", true);
		replaceLavaWithObsidian = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		/* #####################################
		      OVERWORLD: SURFACE MODIFICATION
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "5b : overworld - surface modification";

		property = config.get(category, "Enable all surface modification", true);
		enableSurfaceModification = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of chunks which will have mossy surfaces in forests", 50);
		forestMossyChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of mossy blocks in mossy forest chunks", 50);
		forestMossyBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of chunks which will have fractured surfaces in mountains", 50);
		mountainFracturedChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of fractured blocks in fractured mountain chunks", 20);
		mountainFracturedBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of chunks which will have fractured surfaces in rivers", 50);
		riverFracturedChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of fractured blocks in fractured river chunks", 20);
		riverFracturedBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		/* #####################################
		   OVERWORLD: SUB-BIOMES - AUTUMN FOREST
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "5c : overworld - sub biomes - autumn forest";

		property = config.get(category, "Autumn Forest ID, set to -1 to disable", 40);
		autumnForestID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autumn Hills ID, set to -1 to disable", 41);
		autumnHillsID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage frequency of Autumn Forest sub biomes within their parent biomes", 15);
		autumnForestPercentageFrequency = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autumn forest size - setting smaller will give isolated patches, larger gives large swathes", 30);
		autumnForestSize = property.getInt() * 32;
		propertyOrder.add(property.getName());

		property = config.get(category, "Autumn Color Scaling- setting smaller will give faster changes in colour", 10);
		autumnForestColorScale = property.getInt();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);

		/* #####################################
		           OVERWORLD: BIG TREES
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "5d : overworld - big trees";

		property = config.get(category, "Enable custom tree generation (required for the rest of the tree configs to have effect)", true);
		bigTreesEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Percentage of trees generated that this mod will attempt to replace with custom big trees", 50);
		bigTreeReplacementPercentage = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get(category, "Scale for the simplex tree replacement - smaller values allow more mixing of tree types, larger values separate them out more", 3.0D);
		simplexBigTreeScale = property.getDouble();
		propertyOrder.add(property.getName());

		property = config.get(category, "Shrink exceptionally massive trees such as redwoods, mega swamp and jungle trees. Disabling this may cause immense cascading worldgen lag.", true);
		shrinkMassiveTrees = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		/* #####################################
		               MISCELLANEOUS
		   ##################################### */
		propertyOrder = new ArrayList<>();
		category = "6 : miscellaneous";

		config.setCategoryComment(category, "Miscellaneous settings. These usually will not interfere with gameplay.");

		property = config.get(category, "Load default JSON data and guide into the configuration folder if they do not exist", true);
		loadDefaultFiles = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder(category, propertyOrder);
		
		if (config.hasChanged())
			config.save();
	}
}
