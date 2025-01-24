package wtf.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class WTFExpeditionConfig {
	
	public static boolean enableNameGetter = false;

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
	public static boolean replaceSandstone;
	public static boolean updateSandstone;

	// Tweaks: Miscellaneous
	public static double cropGrowthPercentModifier;
	public static boolean modifyCropGrowth;
	public static boolean waterSourceControl;
	public static boolean preventBabyZombies;

	// Tweaks: Mining
	public static boolean miningStoneFractures;
	public static boolean miningOreFractures;
	public static boolean simpleFracturing;
	public static boolean miningSpeedModificationEnabled;
	public static boolean modifyHammerBehaviour;
	public static String[] blockFractureList;

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
	public static String[] blockStabilityList;

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

	// Overworld : Miscellaneous
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
	
	public static Configuration config;

	// TODO Bring back block state rotations

	public static void syncConfig() {
		config.load();
		
		/* #####################################
		                  MASTER
		   ##################################### */
		List<String> propertyOrder = new ArrayList<>();

		config.setCategoryComment("1 : master", "Configurations for all modules of WTFExpedition.");

		Property property = config.get("1 : master", "Set to false to disable all gameplay tweaks for a vanilla gameplay experience", true);
		gameplayTweaksEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("1 : master", "Set to false to disable all cave decoration", true);
		caveGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("1 : master", "Set to false to disable all dungeon and cave subtype generation", true);
		dungeonGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("1 : master", "Set to false to disable WTFExpedition's custom ore generation completely", true);
		oreGenEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("1 : master", "Set to false to disable all Overworld generation added by WTFExpedition", true);
		overworldGenerationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("1 : master", propertyOrder);

		/* #####################################
		                CAVE BIOMES
		   ##################################### */
		propertyOrder = new ArrayList<>();

		config.setCategoryComment("2 : cave biomes", "Configurations for the Cave Biomes module of WTFExpedition.");

		property = config.get("2 : cave biomes", "Allow generation of custom mob based dungeons", true);
		enableDungeons = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("2 : cave biomes", "Allow generation of cave subtypes", true);
		enableSubtypes = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("2 : cave biomes", "Percent generation chance of cave subtypes and dungeons", 50);
		subtypeChance = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("2 : cave biomes", "Allow generation of puddles in wet cave biomes", true);
		enablePuddles = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("2 : cave biomes", "Replace sandstone during world gen. This prevents stalactites forming in pyramid ruins", true);
		replaceSandstone = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("2 : cave biomes", "Update sandstone after replacement. Required only if you've changed the texture of naturalised sandstone", true);
		updateSandstone = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("2 : cave biomes", propertyOrder);

		/* #####################################
		               TWEAKS: MISC
		   ##################################### */
		propertyOrder = new ArrayList<>();

		config.setCategoryComment("3a : tweaks", "Configurations for the Tweaks module of WTFExpedition.");

		property = config.get("3a : tweaks", "Growth rate percent modifier for crops", 10);
		cropGrowthPercentModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("3a : tweaks", "Modify crop growth rates", true);
		modifyCropGrowth = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3a : tweaks", "Prevent infinite water source blocks outside biomes with the WET type", false);
		waterSourceControl = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3a : tweaks", "Prevent spawning of baby zombies", false);
		preventBabyZombies = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3a : tweaks", propertyOrder);

		/* #####################################
		              TWEAKS: MINING
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("3b : tweaks - mining", "Stone fractures upon mining. Individual stones can be disabled in the block registry", true);
		miningStoneFractures = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3b : tweaks - mining", "Ores fracture adjacent stone upon mining. Individual blocks can be removed from the config list", true);
		miningOreFractures = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3b : tweaks - mining", "Use simplified fracturing (always fractures all 6 adjacent blocks)", false);
		simpleFracturing = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3b : tweaks - mining", "Enable mining speed modification. Values are set in the block registry", true);
		miningSpeedModificationEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3b : tweaks - mining", "Modify hammer behaviour", true);
		modifyHammerBehaviour = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3b : tweaks - mining", "Blocks that fracture adjacent stone when mined", new String[] {"minecraft:coal_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:lapis_ore", "minecraft:diamond_ore", "minecraft:redstone_ore", "minecraft:lit_redstone_ore", "minecraft:emerald_ore"});
		blockFractureList = property.getStringList();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3b : tweaks - mining", propertyOrder);

		/* #####################################
		            TWEAKS: EXPLOSIVES
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("3c : tweaks - explosives", "Override explosions with Expedition's custom explosions. All other explosion-based values require custom explosions to be turned on", true);
		customExplosions = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Explosions fracture stone", true);
		explosionsFracture = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Explosion level above which blocks atomize, and below which they drop", 5);
		atomizingExplosionLevel = property.getDouble();
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Explosion level above which blocks drop, and below which they fracture (if fracturing on- if off nothing happens below)", 1);
		droppingExplosionLevel = property.getDouble();
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Explosion damage percentage modifier", 100);
		explosionDamageModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Explosion force percentage modifier", 100);
		explosionForceModifier = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("3c : tweaks - explosives", "Creeper explosion upward modifier. Increase to have creeper explosions explode more upwards", 1.5);
		creeperUpwardModifier = property.getDouble() * 100;
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3c : tweaks - explosives", propertyOrder);

		/* #####################################
		              TWEAKS: GRAVITY
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("3d : tweaks - gravity", "Enable gravity for additional blocks", true);
		additionalBlockGravity = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - gravity", "Enable damage from falling blocks", true);
		fallingBlocksDamage = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - gravity", "Nerdpole prevention: Prevent indefinite stacking of non-stable blocks, causing them to slide off", true);
		antiNerdPole = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - gravity", "Block stability list. Format is blockName@percentStability", new String[] {"minecraft:dirt@50", "minecraft:cobblestone@75", "minecraft:mossy_cobblestone@90","minecraft:sand@10", "minecraft:soul_sand@10","minecraft:gravel@20", "minecraft:snow@40"});
		blockStabilityList = property.getStringList();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3d : tweaks - gravity", propertyOrder);

		/* #####################################
		        TWEAKS: LOOT AND CRAFTING
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("3d : tweaks - loot & crafting", "Average number of minutes between chickens dropping feathers", 30);
		featherDropRate = property.getInt() * 20 * 60;
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "Percentage of leaf blocks that drop sticks", 50);
		stickDropPercentage = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "When not killed by a player, mobs only drop items this percentage of the time", 25);
		playerlessMobDropPercentage = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "Enable Crafting Recipe Table", true);
		wcicTableEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "Enable home scrolls", true);
		homeScrollsEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "Enable Netherrack --> Sulfur recipe", true);
		sulfurRecipeEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3d : tweaks - loot & crafting", "Remove recipes for vanilla tools (Requires Tinkers Construct to be installed)", false);
		removeVanillaTools = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3d : tweaks - loot & crafting", propertyOrder);
		
		/* #####################################
		              TWEAKS: TORCHES
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("3e : tweaks - torches", "Replace vanilla torches with finite torches", false);
		replaceTorches = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("3e : tweaks - torches", "Chance in 100 per block tick (avg every 45 seconds) that an unattended torch will go out", 20);
		torchLifespan = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("3e : tweaks - torches", "Number of blocks a player must be within to prevent a torch from going out", 20);
		torchRange = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("3e : tweaks - torches", "Torches can be relit by hand (true), or require flint and steel (false)", true);
		relightTorchByHand = property.getBoolean();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("3e : tweaks - torches", propertyOrder);
		
		/* #####################################
		              OVERWORLD: MISC
		   ##################################### */

		propertyOrder = new ArrayList<>();

		config.setCategoryComment("4a : overworld", "Configurations for the Overworld module of WTFExpedition.");

		property = config.get("4a : overworld", "Enable all surface modification", true);
		enableSurfaceModification = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of chunks which will have mossy surfaces in forests", 50);
		forestMossyChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of mossy blocks in mossy forest chunks", 50);
		forestMossyBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of chunks which will have fractured surfaces in mountains", 50);
		mountainFracturedChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of fractured blocks in fractured mountain chunks", 20);
		mountainFracturedBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of chunks which will have fractured surfaces in rivers", 50);
		riverFracturedChunkPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4a : overworld", "Percentage of fractured blocks in fractured river chunks", 20);
		riverFracturedBlockPercent = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("4a : overworld", propertyOrder);
		
		/* #####################################
		   OVERWORLD: SUB-BIOMES - AUTUMN FOREST
		   ##################################### */
		propertyOrder = new ArrayList<>();

		property = config.get("4b : overworld - sub biomes - autumn forest", "Autumn Forest ID, set to -1 to disable", 40);
		autumnForestID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("4b : overworld - sub biomes - autumn forest", "Autumn Hills ID, set to -1 to disable", 41);
		autumnHillsID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("4b : overworld - sub biomes - autumn forest", "Percentage frequency of Autumn Forest sub biomes within their parent biomes", 15);
		autumnForestPercentageFrequency = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get("4b : overworld - sub biomes - autumn forest", "Autumn forest size - setting smaller will give isolated patches, larger gives large swathes", 30);
		autumnForestSize = property.getInt() * 32;
		propertyOrder.add(property.getName());

		property = config.get("4b : overworld - sub biomes - autumn forest", "Autumn Color Scaling- setting smaller will give faster changes in colour", 10);
		autumnForestColorScale = property.getInt();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("4b : overworld - sub biomes - autumn forest", propertyOrder);

		/* #####################################
		           OVERWORLD: BIG TREES
		   ##################################### */

		propertyOrder = new ArrayList<>();

		property = config.get("4c : overworld - big trees", "Enable custom tree generation (required for the rest of the tree configs to have effect)", true);
		bigTreesEnabled = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get("4c : overworld - big trees", "Percentage of trees generated that this mod will attempt to replace with custom big trees", 50);
		bigTreeReplacementPercentage = property.getDouble() / 100;
		propertyOrder.add(property.getName());

		property = config.get("4c : overworld - big trees", "Scale for the simplex tree replacement - smaller values allow more mixing of tree types, larger values separate them out more", 3.0D);
		simplexBigTreeScale = property.getDouble();
		propertyOrder.add(property.getName());

		config.setCategoryPropertyOrder("4c : overworld - big trees", propertyOrder);
		
		if (config.hasChanged()) {
			config.save();
		}
	}
}
