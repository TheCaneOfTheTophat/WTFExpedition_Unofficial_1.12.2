
package wtf.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockFalling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import wtf.WTFExpedition;
import wtf.blocks.*;
import wtf.blocks.enums.AnimatedDecoType;
import wtf.blocks.enums.StaticDecoType;
import wtf.blocks.redstone.BlockDenseRedstoneOre;
import wtf.blocks.BlockWTFTorch;
import wtf.config.*;
import wtf.blocks.BlockOreSand;
import wtf.items.ItemBlockDerivative;
import wtf.items.ItemBlockSpeleothem;
import wtf.items.ItemHomeScroll;
import wtf.items.ItemBlockState;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(WTFExpedition.modID)
@SuppressWarnings({"unused", "ConstantConditions"})
public class WTFContent {

	public static HashMap<IBlockState, BlockSpeleothem> speleothemMap = new HashMap<>();

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
	
	public static final Block natural_sandstone = null;
	public static final Block natural_red_sandstone = null;

	public static final Item sulfur = null;
	public static final Item nitre = null;
	public static final Item home_scroll = null;

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

		// new NetherrackReplacer();

		registerSimpleBlock(reg, new BlockNaturalSandstone(Blocks.SANDSTONE.getDefaultState()), "natural_sandstone");
		registerSimpleBlock(reg, new BlockNaturalSandstone(Blocks.RED_SANDSTONE.getDefaultState()), "natural_red_sandstone");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerConfigDependentBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();

		/*  ==============================================
			==================== ORES ====================
		    ============================================== */

		for(OreEntry entry : JSONLoader.oreEntries) {
			if(entry.usesDenseBlocks()) {
				IBlockState oreState = JSONLoader.getStateFromId(entry.getBlockId());

				for (String stone : entry.getStoneList()) {
					BlockEntry stoneEntry = JSONLoader.identifierToBlockEntry.get(stone);

					if(stoneEntry == null) {
						WTFExpedition.wtfLog.error("Stone entry \"" + stone + "\" does not exist! (encountered while registering blocks for ore \"" + entry.getName() + "\")");
						continue;
					}

					IBlockState stoneState = JSONLoader.getStateFromId(stoneEntry.getBlockId());

					if(oreState == Blocks.REDSTONE_ORE.getDefaultState()) {
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

					} else if(stoneState.getBlock() instanceof BlockFalling) {
						// Sadly, there is no such thing as a falling redstone ore since both redstone logic and falling block logic depend on ticking. :(
						BlockDenseOreFalling ore = new BlockDenseOreFalling(stoneState, oreState);
						ore.setRegistryName(WTFExpedition.modID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
						ore.setCreativeTab(WTFExpedition.wtfTab);

						blocks.add(ore);
						oreEntryMap.put(ore, entry);
						BlockSets.adjacentFracturingBlocks.add(ore.getRegistryName().toString());

						reg.register(ore);
					} else {
						BlockDenseOre ore = new BlockDenseOre(stoneState, oreState);
						ore.setRegistryName(WTFExpedition.modID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
						ore.setCreativeTab(WTFExpedition.wtfTab);

						blocks.add(ore);
						oreEntryMap.put(ore, entry);
						BlockSets.adjacentFracturingBlocks.add(ore.getRegistryName().toString());

						reg.register(ore);
					}
				}
			}
		}

		/*  ==============================================
			================= BLOCK BASED ================
		    ============================================== */

		for(BlockEntry entry : JSONLoader.blockEntries) {

			String blockName = entry.getName();
			IBlockState blockState = JSONLoader.getStateFromId(entry.getBlockId());
			IBlockState fracturedBlockState = JSONLoader.getStateFromId(entry.getFracturedBlockId());

			if(blockState == null) {
				WTFExpedition.wtfLog.error("Block ID " + entry.getBlockId() + " in entry " + entry.getName() + " is invalid! Skipping...");
				continue;
			}

			if(fracturedBlockState == null && !entry.getFracturedBlockId().isEmpty()) {
				WTFExpedition.wtfLog.error("Fractured block ID " + entry.getFracturedBlockId() + " in entry " + entry.getName() + " is invalid! Skipping...");
				continue;
			}

			// SPELEOTHEMS
			if(entry.hasSpeleothems()) {
				BlockSpeleothem speleothem = new BlockSpeleothem(blockState);
				speleothem.setRegistryName(WTFExpedition.modID, blockName + "_speleothem");
				speleothem.setCreativeTab(WTFExpedition.wtfTab);

				blocks.add(speleothem);
				reg.register(speleothem);

				BlockSpeleothemFrozen frozenSpeleothem = new BlockSpeleothemFrozen(speleothem);
				frozenSpeleothem.setRegistryName(WTFExpedition.modID, "frozen_" + blockName + "_speleothem");
				frozenSpeleothem.setCreativeTab(WTFExpedition.wtfTab);

				blocks.add(frozenSpeleothem);
				reg.register(frozenSpeleothem);
			}

			// STATIC DECOR
			for(StaticDecoType decorType : StaticDecoType.values()) {
				if(entry.getStaticDecorTypes().get(decorType)) {
					BlockDecoStatic decor = new BlockDecoStatic(blockState, decorType);
					decor.setRegistryName(WTFExpedition.modID, "decoration_" + decorType.getName() + "_" + blockName);
					decor.setCreativeTab(WTFExpedition.wtfTab);

					blocks.add(decor);
					reg.register(decor);
				}
			}

			// ANIMATED DECOR
			for(AnimatedDecoType decorType : AnimatedDecoType.values()) {
				if(entry.getAnimatedDecorTypes().get(decorType)) {
					BlockDecoAnim decor = new BlockDecoAnim(blockState, decorType);
					decor.setRegistryName(WTFExpedition.modID, "decoration_" + decorType.getName() + "_" + blockName);
					decor.setCreativeTab(WTFExpedition.wtfTab);

					blocks.add(decor);
					reg.register(decor);
				}
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
}
