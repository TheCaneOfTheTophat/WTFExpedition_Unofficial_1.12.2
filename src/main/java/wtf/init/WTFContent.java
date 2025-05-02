
package wtf.init;

import java.util.*;

import net.minecraft.block.BlockFalling;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.apache.commons.lang3.tuple.Pair;
import wtf.WTFExpedition;
import wtf.blocks.*;
import wtf.client.WTFModelRegistry;
import wtf.enums.AnimatedDecoType;
import wtf.enums.Modifier;
import wtf.enums.StaticDecoType;
import wtf.blocks.BlockDenseRedstoneOre;
import wtf.blocks.BlockWTFTorch;
import wtf.config.*;
import wtf.blocks.BlockOreSand;
import wtf.entities.customentities.*;
import wtf.entities.simpleentities.*;
import wtf.items.*;
import wtf.worldgen.WorldGenListener;
import wtf.worldgen.ores.OreGenAbstract;
import wtf.worldgen.ores.VanillaOreGenCatcher;
import wtf.worldgen.ores.oregenerators.*;
import wtf.worldgen.ores.oregenerators.secondary.OreGenCaveFloor;
import wtf.worldgen.ores.oregenerators.secondary.OreGenUnderWater;
import wtf.utilities.wrappers.StoneAndOre;
import wtf.worldgen.SubBiomeGenerator;
import wtf.worldgen.replacers.LavaReplacer;
import wtf.worldgen.subbiomes.BiomeAutumnForest;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(WTFExpedition.modID)
@SuppressWarnings({"unused", "ConstantConditions"})
public class WTFContent {

	/*
	====================================
	            STATIC FIELDS
	====================================
	*/

	public static final Block nitre_ore = null;
	public static final Block gold_ore_sand = null;
	public static final Block red_cactus = null;
	public static final Block mycorrack = null;

	public static final Block foxfire = null;

	public static Block wcicTable;
	public static final Block dirt_patch = null;
	public static final Block mossy_dirt_patch = null;
	public static final Block sand_patch = null;
	public static final Block red_sand_patch = null;
	public static final Block gravel_patch = null;
	public static final Block podzol_patch = null;
	public static final Block puddle = null;
	public static final Block ice_patch = null;
	public static final Block terracotta_patch = null;
	public static final Block terracotta_patch_stained = null;

	public static final Block icicle = null;
	public static final Block roots = null;

	public static final Block lit_torch = null;
	public static final Block extinguished_torch = null;

	public static final Item sulfur = null;
	public static final Item nitre = null;
	public static final Item home_scroll = null;

	public static final BiomeAutumnForest autumn_forest = null;
	public static final BiomeAutumnForest autumn_hills = null;

	public static ArrayList<Item> items = new ArrayList<>();
	public static ArrayList<Block> blocks = new ArrayList<>();

	public static Map<Block, OreEntry> oreEntryMap = new HashMap<>();

	/*
	====================================
	               EVENTS
	====================================
	*/

	@SuppressWarnings("unused")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerSimpleBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();

		registerSimpleBlock(reg, new BlockNitreOre().setCreativeTab(WTFExpedition.wtfTab), "nitre_ore");
		registerSimpleBlock(reg, new BlockOreSand().setCreativeTab(WTFExpedition.wtfTab), "gold_ore_sand");
		registerSimpleBlock(reg, new BlockRedCactus().setCreativeTab(WTFExpedition.wtfTab), "red_cactus");
		registerSimpleBlock(reg, new BlockMycorrack().setCreativeTab(WTFExpedition.wtfTab), "mycorrack");

		registerSimpleBlock(reg, new BlockFoxfire().setCreativeTab(WTFExpedition.wtfTab), "foxfire");

		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "dirt_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "mossy_dirt_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState()), "sand_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)), "red_sand_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.GRAVEL.getDefaultState()), "gravel_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "podzol_patch");
		registerSimpleBlock(reg, new BlockPuddle().setCreativeTab(WTFExpedition.wtfTab), "puddle");
		registerSimpleBlock(reg, new BlockIcePatch().setCreativeTab(WTFExpedition.wtfTab), "ice_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.HARDENED_CLAY.getDefaultState()), "terracotta_patch");
		registerSimpleBlock(reg, new BlockStainedTerracottaPatch().setCreativeTab(WTFExpedition.wtfTab), "terracotta_patch_stained");

		registerSimpleBlock(reg, new BlockIcicle().setCreativeTab(WTFExpedition.wtfTab), "icicle");
		registerSimpleBlock(reg, new BlockRoots().setCreativeTab(WTFExpedition.wtfTab), "roots");

		BlockWTFTorch torchOff = new BlockWTFTorch(false);
		BlockWTFTorch torchOn = new BlockWTFTorch(true);

		torchOff.setToggleBlock(torchOn);
		torchOn.setToggleBlock(torchOff);

		registerBlockWithoutItem(reg, torchOn, "lit_torch");
		registerSimpleBlock(reg, torchOff, "extinguished_torch");

		// wcicTable = registerBlock(new WCICTable(), "wcic_table");
		// GameRegistry.registerTileEntity(WCICTileEntity.class, "WCICTable");

		if(WTFExpeditionConfig.replaceLavaWithWater || WTFExpeditionConfig.replaceLavaWithObsidian) {
			new LavaReplacer(true);
			new LavaReplacer(false);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerJSONDependentContent(RegistryEvent.Register<Block> event) {
		HashSet<String> unknownMods = new HashSet<>();
		IForgeRegistry<Block> reg = event.getRegistry();

		/*  ==============================================
			==================== ORES ====================
		    ============================================== */

		for(OreEntry entry : JSONLoader.oreEntries) {
			IBlockState oreState;

			HashSet<String> stoneList = new HashSet<>();

            for (String stone : entry.getStoneList()) {
                if (stone.contains("group#")) {
                    ArrayList<String> groupArray = JSONLoader.blockGroups.get(stone.split("#")[1]);

                    if (groupArray != null)
                        stoneList.addAll(groupArray);
                } else
                    stoneList.add(stone);
            }

			if(!entry.getBlockId().contains("type#")) {
				oreState = JSONLoader.getStateFromId(entry.getBlockId());

				if (entry.usesDenseBlocks()) {
					for (String stone : stoneList) {
						BlockEntry stoneEntry = JSONLoader.identifierToBlockEntry.get(stone);

						if (!Loader.isModLoaded(stoneEntry.getBlockId().split(":")[0])) {
							if(!unknownMods.contains(stoneEntry.getBlockId().split(":")[0])) {
								WTFExpedition.wtfLog.error("Mod \"" + stoneEntry.getBlockId().split(":")[0] + "\" is not loaded!");
								unknownMods.add(stoneEntry.getBlockId().split(":")[0]);
							}
							continue;
						}

						if (stoneEntry == null) {
							WTFExpedition.wtfLog.error("Stone entry \"" + stone + "\" does not exist! (encountered while registering blocks for ore \"" + entry.getName() + "\")");
							continue;
						}

						IBlockState stoneState = JSONLoader.getStateFromId(stoneEntry.getBlockId());

						if (oreState == Blocks.REDSTONE_ORE.getDefaultState()) {
							BlockDenseRedstoneOre oreOff = new BlockDenseRedstoneOre(stoneState, false);
							oreOff.setRegistryName(WTFExpedition.modID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
							oreOff.setCreativeTab(WTFExpedition.wtfTab);

							BlockDenseRedstoneOre oreOn = new BlockDenseRedstoneOre(stoneState, true);
							oreOn.setRegistryName(WTFExpedition.modID, "lit_dense_" + stoneEntry.getName() + "_" + entry.getName());

							oreOff.setToggled(oreOn);
							oreOn.setToggled(oreOff);

							blocks.add(oreOff);
							blocks.add(oreOn);

							oreEntryMap.put(oreOn, entry);
							oreEntryMap.put(oreOff, entry);

							reg.register(oreOff);
							reg.register(oreOn);

							BlockSets.adjacentFracturingBlocks.add(oreOff.getRegistryName().toString());
							BlockSets.adjacentFracturingBlocks.add(oreOn.getRegistryName().toString());

							BlockSets.stoneAndOre.put(new StoneAndOre(stoneState, oreState), oreOff.getDefaultState());

						} else if (stoneState.getBlock() instanceof BlockFalling) {
							// Sadly, there is no such thing as a falling redstone ore since both redstone logic and falling block logic depend on ticking. :(
							BlockDenseOreFalling ore = new BlockDenseOreFalling(stoneState, oreState);
							ore.setRegistryName(WTFExpedition.modID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
							ore.setCreativeTab(WTFExpedition.wtfTab);

							blocks.add(ore);
							oreEntryMap.put(ore, entry);
							BlockSets.adjacentFracturingBlocks.add(ore.getRegistryName().toString());

							BlockSets.stoneAndOre.put(new StoneAndOre(stoneState, oreState), ore.getDefaultState());

							reg.register(ore);
						} else {
							BlockDenseOre ore = new BlockDenseOre(stoneState, oreState);
							ore.setRegistryName(WTFExpedition.modID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
							ore.setCreativeTab(WTFExpedition.wtfTab);

							blocks.add(ore);
							oreEntryMap.put(ore, entry);
							BlockSets.adjacentFracturingBlocks.add(ore.getRegistryName().toString());

							BlockSets.stoneAndOre.put(new StoneAndOre(stoneState, oreState), ore.getDefaultState());

							reg.register(ore);
						}
					}
				}
			} else {
				String modifierString = entry.getBlockId().split("#")[1].toLowerCase();

				Modifier modifier;

                switch (modifierString) {
                    case "fractured":
                        modifier = Modifier.FRACTURED;
                        break;
                    case "moss":
                        modifier = Modifier.MOSS;
                        break;
                    case "soul":
                        modifier = Modifier.SOUL;
                        break;
                    case "cracked":
                        modifier = Modifier.CRACKED;
                        break;
					case "lava_crust":
						modifier = Modifier.LAVA_CRUST;
						break;
					case "wet":
						modifier = Modifier.WET;
						break;
					case "lava_dripping":
						modifier = Modifier.LAVA_DRIPPING;
						break;
					default:
						WTFExpedition.wtfLog.error("Modifier " + modifierString +  "does not exist! (encountered while registering ore \"" + entry.getName() + "\")");
						continue;
                }

				oreState = new DummyModifierBlock(modifier).getDefaultState();
			}

			for(OreGeneratorSettings generatorSettings : entry.getGenerators()) {
				OreGenAbstract generator = getOreGenerator(oreState, entry, generatorSettings, true);

				if (generator != null) {
					WorldGenListener.oreGenRegister.add(generator);
					VanillaOreGenCatcher.vanillaCanceler(oreState);
				}
			}
		}

		/*  ==============================================
			================= BLOCK BASED ================
		    ============================================== */

		for(BlockEntry entry : JSONLoader.blockEntries) {
			String blockName = entry.getName();
			IBlockState blockState = JSONLoader.getStateFromId(entry.getBlockId());
			String fracturedId = JSONLoader.processId(entry.getFracturedBlockId());
			IBlockState fracturedBlockState = JSONLoader.getStateFromId(fracturedId);

			if (!Loader.isModLoaded(entry.getBlockId().split(":")[0])) {
				if(!unknownMods.contains(entry.getBlockId().split(":")[0])) {
					WTFExpedition.wtfLog.error("Mod \"" + entry.getBlockId().split(":")[0] + "\" is not loaded!");
					unknownMods.add(entry.getBlockId().split(":")[0]);
				}
				continue;
			}

			if(blockState == null) {
				WTFExpedition.wtfLog.error("Block ID " + entry.getBlockId() + " in entry " + entry.getName() + " is invalid! Skipping...");
				continue;
			}

			if(fracturedBlockState == null && !fracturedId.isEmpty()) {
				WTFExpedition.wtfLog.error("Fractured block ID " + fracturedId + " in entry " + entry.getName() + " is invalid! Skipping...");
				continue;
			}

			WTFModelRegistry.textureMap.put(blockState, entry.getTexture());

			boolean fractures = fracturedBlockState != null;

			if(fractures) {
				BlockSets.blockTransformer.put(Pair.of(blockState, Modifier.FRACTURED), fracturedBlockState);

				if(entry.fracturesFirstWhenMined())
					BlockSets.fractureWhenMinedBlocks.add(blockState);
			}

			if(entry.getPercentageMineSpeedModifier() != 1)
				BlockSets.miningSpeedModifierMap.put(blockState, entry.getPercentageMineSpeedModifier());

			if(entry.getPercentageStability() != 100)
				BlockSets.percentageStabilityMap.put(blockState, entry.getPercentageStability());

			if(entry.isIrreplaceable())
				BlockSets.irreplaceable.add(blockState);

			if(entry.hasNoAddons())
				BlockSets.noAddons.add(blockState);

			// SPELEOTHEMS
			if(entry.hasSpeleothems()) {
				BlockSpeleothem speleothem = new BlockSpeleothem(blockState);
				speleothem.setRegistryName(WTFExpedition.modID, blockName + "_speleothem");
				speleothem.setCreativeTab(WTFExpedition.wtfTab);

				BlockSpeleothemFrozen frozenSpeleothem = new BlockSpeleothemFrozen(speleothem);
				frozenSpeleothem.setRegistryName(WTFExpedition.modID, "frozen_" + blockName + "_speleothem");
				frozenSpeleothem.setCreativeTab(WTFExpedition.wtfTab);

				speleothem.frozen = frozenSpeleothem;

				blocks.add(speleothem);
				reg.register(speleothem);

				blocks.add(frozenSpeleothem);
				reg.register(frozenSpeleothem);
			}

			for(Modifier modifier : Modifier.values()) {
				String modStateId = entry.getModifiers().get(modifier);

				if(modStateId == null)
					continue;

				if(!modStateId.isEmpty()) {
					IBlockState modState = null;

					if(modStateId.equals("#generated") && modifier.getDecoType() != null) {
						if(modifier.getDecoType() instanceof StaticDecoType) {
							StaticDecoType decorType = (StaticDecoType) modifier.getDecoType();

                            BlockDecoStatic decor = new BlockDecoStatic(blockState, decorType);
							decor.setRegistryName(WTFExpedition.modID, "decoration_" + decorType.getName() + "_" + blockName);
							decor.setCreativeTab(WTFExpedition.wtfTab);

							blocks.add(decor);
							reg.register(decor);

							modState = decor.getDefaultState();
						}

						if(modifier.getDecoType() instanceof AnimatedDecoType) {
							AnimatedDecoType decorType = (AnimatedDecoType) modifier.getDecoType();

							BlockDecoAnim decor = new BlockDecoAnim(blockState, decorType);
							decor.setRegistryName(WTFExpedition.modID, "decoration_" + decorType.getName() + "_" + blockName);
							decor.setCreativeTab(WTFExpedition.wtfTab);

							blocks.add(decor);
							reg.register(decor);

							modState = decor.getDefaultState();
						}

						if(fractures && modifier != Modifier.LAVA_CRUST)
							BlockSets.blockTransformer.put(Pair.of(modState, Modifier.FRACTURED), fracturedBlockState);
					} else
						modState = JSONLoader.getStateFromId(JSONLoader.processId(modStateId));

					BlockSets.blockTransformer.put(Pair.of(blockState, modifier), modState);
				}
			}
		}

		/*  ==============================================
			================= TRANSFORMER ================
		    ============================================== */

		HashMap<Pair<IBlockState, Modifier>, IBlockState> blockTransformer = new HashMap<>(BlockSets.blockTransformer);
		ArrayList<Pair<IBlockState, Modifier>> blockTransformerList = new ArrayList<>(new HashSet<>(blockTransformer.keySet()));

		blockTransformerList.sort((o1, o2) -> {
            int mod = o1.getValue().toString().compareToIgnoreCase(o2.getValue().toString());

            if(mod == 0)
                return o1.getKey().toString().compareToIgnoreCase(o2.getKey().toString());
            else
                return mod;
        });

		// Iterate again after all registration and basic transformer stuff has been done.
		for(Pair<IBlockState, Modifier> entry : blockTransformerList) {
			IBlockState blockState = entry.getKey();
			Modifier modifier = entry.getValue();
			IBlockState modState = blockTransformer.get(Pair.of(blockState, modifier));

			ArrayList<Modifier> modValues2 = new ArrayList<>();
			Collections.addAll(modValues2, Modifier.values());
			modValues2.remove(modifier);

			for(Modifier modifier2 : modValues2) {
				IBlockState modState2 = blockTransformer.get(Pair.of(blockState, modifier2));

				if(modState2 == null)
					continue;

				IBlockState modState3 = blockTransformer.get(Pair.of(modState2, modifier));

				if(modState3 == null || modState == modState3)
					continue;

				BlockSets.blockTransformer.remove(Pair.of(modState, modifier2));
				BlockSets.blockTransformer.put(Pair.of(modState, modifier2), modState3);
			}
		}
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> reg = event.getRegistry();

		registerItem(reg, new Item(), "nitre");
		registerItem(reg, new Item(), "sulfur");
		registerItem(reg, new ItemHomeScroll(), "home_scroll");

		for(Block block : blocks) {
			if (block instanceof BlockDenseOre)
				registerItemBlockNoModel(reg, new ItemBlockDerivative((BlockDenseOre) block));

			else if (block instanceof BlockDenseOreFalling)
				registerItemBlockNoModel(reg, new ItemBlockDerivative.ItemBlockDerivativeFalling((BlockDenseOreFalling) block));

			else if (block instanceof BlockSpeleothem)
				if (block instanceof BlockSpeleothemFrozen)
					registerItemBlockNoModel(reg, new ItemBlockSpeleothem((BlockSpeleothemFrozen) block));
				else
					registerItemBlockNoModel(reg, new ItemBlockSpeleothem((BlockSpeleothem) block));

			else if (block instanceof BlockDecoStatic)
				registerItemBlockNoModel(reg, new ItemBlockDerivative((BlockDecoStatic) block));

			else if (block instanceof BlockDecoAnim)
				registerItemBlockNoModel(reg, new ItemBlockDerivative((BlockDecoAnim) block));

			else if (block instanceof BlockRoots || block instanceof BlockIcicle || block instanceof BlockStainedTerracottaPatch)
				registerItemBlock(reg, new ItemBlockState(block));

			else
				registerItemBlock(reg, new ItemBlock(block));
		}

		for(Modifier mod : Modifier.values())
			registerItem(reg, new ItemDebugModifier(mod), "debug_modifier_" + mod.name().toLowerCase());
	}

	private static int entityCounter = 0;

	@SubscribeEvent()
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		IForgeRegistry<EntityEntry> reg = event.getRegistry();

		reg.register(buildEntry(EntityCursedArmor.class, "cursed_armor"));
		reg.register(buildEntry(EntityDerangedIronGolem.class, "deranged_iron_golem"));
		reg.register(buildEntry(EntityBogLantern.class, "bog_lantern"));
		reg.register(buildEntry(EntityBlockHead.class, "blockhead"));
		reg.register(buildEntry(EntityFireElemental.class, "fire_elemental"));

		reg.register(buildEntry(EntityCaveHusk.class, "cave_husk"));
		reg.register(buildEntry(EntityCaveStray.class, "cave_stray"));
		reg.register(buildEntry(EntityFarmerZombie.class, "zombie_farmer"));
		reg.register(buildEntry(EntityLumberjackZombie.class, "zombie_lumberjack"));
		reg.register(buildEntry(EntityMummy.class, "mummy"));
		reg.register(buildEntry(EntitySkeletonMage.class, "skeleton_mage"));
		reg.register(buildEntry(EntitySkeletonKnight.class, "skeleton_knight"));
		reg.register(buildEntry(EntityZombieMiner.class, "zombie_miner"));

		WTFExpedition.proxy.registerEntityRenderers();
	}

	@SubscribeEvent()
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		IForgeRegistry<Biome> reg = event.getRegistry();

		BiomeAutumnForest autumnForest = new BiomeAutumnForest(BiomeForest.Type.NORMAL, new Biome.BiomeProperties("Autumn Forest").setTemperature(0.4F).setRainfall(0.8F), Biomes.FOREST);

		reg.register(autumnForest.setRegistryName(WTFExpedition.modID, "autumn_forest"));
		BiomeDictionary.addTypes(autumnForest, BiomeDictionary.Type.FOREST);
		SubBiomeGenerator.subBiomeRegistry.put((byte) 4, autumnForest);

		BiomeManager.BiomeEntry entry = new BiomeManager.BiomeEntry(autumnForest, WTFExpeditionConfig.autumnForestID);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, entry);
		BiomeManager.removeBiome(BiomeManager.BiomeType.COOL, entry);

		BiomeAutumnForest autumnHills = new BiomeAutumnForest(BiomeForest.Type.NORMAL, new Biome.BiomeProperties("Autumn Hills").setTemperature(0.3F).setRainfall(0.8F), Biomes.FOREST_HILLS);

		reg.register(autumnHills.setRegistryName(WTFExpedition.modID, "autumn_hills"));
		BiomeDictionary.addTypes(autumnHills, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS);
		SubBiomeGenerator.subBiomeRegistry.put((byte) 18, autumnHills);

		entry = new BiomeManager.BiomeEntry(autumnHills, WTFExpeditionConfig.autumnHillsID);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, entry);
		BiomeManager.removeBiome(BiomeManager.BiomeType.COOL, entry);
	}

	/*
	====================================
	              METHODS
	====================================
	*/

	public static <T extends Block> void registerBlockWithoutItem(IForgeRegistry<Block> registry, T block, String name) {
		block.setRegistryName(WTFExpedition.modID, name);
		block.setUnlocalizedName(block.getRegistryName().toString());

		registry.register(block);
	}

	public static <T extends Block> void registerSimpleBlock(IForgeRegistry<Block> registry, T block, String name) {
		blocks.add(block);

		registerBlockWithoutItem(registry, block, name);
	}

	public static <T extends Item> void registerItem(IForgeRegistry<Item> registry, T item, String name) {
		item.setCreativeTab(WTFExpedition.wtfTab);
		item.setRegistryName(WTFExpedition.modID, name);
		item.setUnlocalizedName(item.getRegistryName().toString());

		items.add(item);

		registry.register(item);
	}

	public static <T extends ItemBlock> void registerItemBlockNoModel(IForgeRegistry<Item> registry, T itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
		itemBlock.setHasSubtypes(true);
		itemBlock.setMaxDamage(0);

		registry.register(itemBlock);
	}

	public static <T extends ItemBlock> void registerItemBlock(IForgeRegistry<Item> registry, T itemBlock) {
		items.add(itemBlock);

		registerItemBlockNoModel(registry, itemBlock);
	}

	public static EntityEntry buildEntry(Class<? extends Entity> entityClass, String name) {
		entityCounter++;
		ResourceLocation rl = new ResourceLocation(WTFExpedition.modID, name);
		return EntityEntryBuilder.create().id(rl, entityCounter).entity(entityClass).name(rl.toString()).tracker(64, 1, true).egg(entityCounter * 16, entityCounter * 22).build();
	}

	public static OreGenAbstract getOreGenerator(IBlockState oreState, OreEntry oreEntry, OreGeneratorSettings settings, boolean getSecondary) {
		String primary = settings.primaryGenerationType;
		String secondary = settings.secondaryGenerationType;
		
		int[] genRange = {settings.surfaceHeightMinPercentage, settings.surfaceHeightMaxPercentage};
		int[] hardYGenRange = {settings.minY, settings.maxY};
		int[] orePerChunk = {settings.minAmountPerChunk, settings.maxAmountPerChunk};

		boolean biomeWhitelist = settings.biomeTypeListWhitelist;
		boolean dimensionWhitelist = settings.dimensionListWhitelist;
		
		boolean denseBlock = oreEntry.usesDenseBlocks();

		OreGenAbstract generator = null;
		
		if(getSecondary && !secondary.isEmpty()) {
			OreGenAbstract primaryGen = getOreGenerator(oreState, oreEntry, settings, false);
			
			if (secondary.equals("cave")) {
				ArrayList<OreGenCaveFloor.surface> surfaceList = new ArrayList<>();

				if (settings.ceiling)
					surfaceList.add(OreGenCaveFloor.surface.ceiling);

				if (settings.wall)
					surfaceList.add(OreGenCaveFloor.surface.wall);

				if (settings.floor)
					surfaceList.add(OreGenCaveFloor.surface.floor);

				generator = new OreGenCaveFloor(primaryGen, oreState, settings.name, surfaceList);
			}

			if (secondary.equals("underwater"))
				generator = new OreGenUnderWater(primaryGen, oreState, settings.name);
		} else {
            switch (primary) {
                case "cloud":
					generator = new OreGenCloud(oreState, settings.name, settings.cloudDiameter);
					break;
                case "cluster":
					generator = new OreGenCluster(oreState, settings.name);
					break;
                case "single":
					generator = new OreGenSingle(oreState, settings.name);
					break;
                case "vanilla":
					generator = new OreGenVanilla(oreState, settings.name, settings.blocksPerCluster);
					break;
                case "vein":
					generator = new OreGenVein(oreState, settings.name, new int[]{settings.veinLength, settings.veinWidth, settings.veinVerticalThickness}, (float) settings.veinPitchAverage);
					break;
            }
        }

		if(generator != null) {
			generator.setSurfaceGenRange(genRange);
			generator.setHardYGenRange(hardYGenRange);
			generator.setMinMaxPerChunk(orePerChunk);
			generator.setBiomeWhiteList(biomeWhitelist);
			generator.setDimensionWhiteList(dimensionWhitelist);
			generator.setGenDenseOres(denseBlock);

			generator.dimension.addAll(settings.dimensionList);
			generator.setVeinDensity(settings.veinPercentDensity / 100F);

			for (Map.Entry<String, Integer> mapentry : settings.percentGenerationPerBiomeType.entrySet())
				generator.biomeModifier.put(BiomeDictionary.Type.getType(mapentry.getKey()), mapentry.getValue() / 100F);

			for (String biome : settings.biomeTypeList)
				generator.reqBiomeTypes.add(BiomeDictionary.Type.getType(biome));
		}

		return generator;
	}
}
