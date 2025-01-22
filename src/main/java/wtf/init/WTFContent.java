
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
import wtf.Core;
import wtf.blocks.*;
import wtf.blocks.redstone.BlockDenseRedstoneOre;
import wtf.client.WTFModelRegistry;
import wtf.config.*;
import wtf.blocks.BlockOreSand;
import wtf.items.ItemBlockDerivative;
import wtf.items.ItemBlockSpeleothem;
import wtf.items.ItemHomeScroll;
import wtf.items.ItemBlockState;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(Core.coreID)
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
	public static final Block ubcSand = null;
	public static final Block dirt_patch = null;
	public static final Block mossy_dirt_patch = null;
	public static final Block sand_patch = null;
	public static final Block red_sand_patch = null;
	public static final Block gravel_patch = null;
	public static final Block podzol_patch = null;
	public static final Block puddle = null;
	public static final Block ice_patch = null;
	public static final Block stained_terracotta_patch = null;

	public static final Block icicle = null;
	public static final Block roots = null;

	public static final Block gen_marker = null;
	
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

		// Blocks in the Creative tab
		registerSimpleBlock(reg, new BlockNitreOre().setCreativeTab(Core.wtfTab), "nitre_ore");
		registerSimpleBlock(reg, new BlockOreSand().setCreativeTab(Core.wtfTab), "gold_ore_sand");
		registerSimpleBlock(reg, new BlockRedCactus().setCreativeTab(Core.wtfTab), "red_cactus");
		registerSimpleBlock(reg, new BlockMycorrack().setCreativeTab(Core.wtfTab), "mycorrack");

		registerSimpleBlock(reg, new BlockFoxfire().setCreativeTab(Core.wtfTab), "foxfire");

		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "dirt_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "mossy_dirt_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState()), "sand_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)), "red_sand_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.GRAVEL.getDefaultState()), "gravel_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "podzol_patch");
		registerSimpleBlock(reg, new BlockPuddle().setCreativeTab(Core.wtfTab), "puddle");
		registerSimpleBlock(reg, new BlockIcePatch().setCreativeTab(Core.wtfTab), "ice_patch");
		registerSimpleBlock(reg, new BlockPatch(Blocks.HARDENED_CLAY.getDefaultState()), "terracotta_patch");
		registerSimpleBlock(reg, new BlockStainedTerracottaPatch().setCreativeTab(Core.wtfTab), "terracotta_patch_stained", 15);

		registerSimpleBlock(reg, new BlockIcicle().setCreativeTab(Core.wtfTab), "icicle", 2);
		registerSimpleBlock(reg, new BlockRoots().setCreativeTab(Core.wtfTab), "roots", 5);

		// wcicTable = registerBlock(new WCICTable(), "wcic_table");
		// GameRegistry.registerTileEntity(WCICTileEntity.class, "WCICTable");

		// new NetherrackReplacer();

		// BlockWTFTorch.torch_off = new BlockWTFTorch(false);

		registerSimpleBlock(reg, new BlockNaturalSandstone(Blocks.SANDSTONE.getDefaultState()), "natural_sandstone");
		registerSimpleBlock(reg, new BlockNaturalSandstone(Blocks.RED_SANDSTONE.getDefaultState()), "natural_red_sandstone");
	}

	@SuppressWarnings("unused")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerConfigDependentBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();

		/*  ==============================================
			==================== ORES ====================
		    ============================================== */

		for(OreEntry entry : JSONLoader.oreEntries) {
			if(entry.hasDenseBlocks()) {
				String[] oreSplit = entry.getBlockId().split("@");
				int oreMeta = oreSplit.length > 1 ? Integer.parseInt(oreSplit[1]) : 0;
				String oreId = oreSplit[0];

				IBlockState oreState = Block.getBlockFromName(oreId).getStateFromMeta(oreMeta);

				for (String stone : entry.getStoneList()) {
					BlockEntry stoneEntry = JSONLoader.identifierToBlockEntry.get(stone);
					String[] stoneSplit = stoneEntry.getBlockId().split("@");
					int stoneMeta = stoneSplit.length > 1 ? Integer.parseInt(stoneSplit[1]) : 0;
					String stoneId = stoneSplit[0];

					IBlockState stoneState = Block.getBlockFromName(stoneId).getStateFromMeta(stoneMeta);

					if(oreState == Blocks.REDSTONE_ORE.getDefaultState()) {
						BlockDenseRedstoneOre oreOff = new BlockDenseRedstoneOre(stoneState, false);
						oreOff.setRegistryName(Core.coreID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
						oreOff.setCreativeTab(Core.wtfTab);

						BlockDenseRedstoneOre oreOn = new BlockDenseRedstoneOre(stoneState, true);
						oreOn.setRegistryName(Core.coreID, "lit_dense_" + stoneEntry.getName() + "_" + entry.getName());

						oreOff.setToggled(oreOn);
						oreOn.setToggled(oreOff);

						blocks.add(oreOff);
						blocks.add(oreOn);

						oreEntryMap.put(oreOn, entry);
						oreEntryMap.put(oreOff, entry);

						reg.register(oreOff);
						reg.register(oreOn);
					} else if(stoneState.getBlock() instanceof BlockFalling) {
						// Sadly, there is no such thing as a falling redstone ore since both redstone logic and falling block logic depend on ticking. :(
						BlockDenseOreFalling ore = new BlockDenseOreFalling(stoneState, oreState);
						ore.setRegistryName(Core.coreID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
						ore.setCreativeTab(Core.wtfTab);

						blocks.add(ore);
						oreEntryMap.put(ore, entry);

						reg.register(ore);
					} else {
						BlockDenseOre ore = new BlockDenseOre(stoneState, oreState);
						ore.setRegistryName(Core.coreID, "dense_" + stoneEntry.getName() + "_" + entry.getName());
						ore.setCreativeTab(Core.wtfTab);


						blocks.add(ore);
						oreEntryMap.put(ore, entry);

						reg.register(ore);
					}
				}
			}
		}

		/*  ==============================================
			================= BLOCK BASED ================
		    ============================================== */

		for(BlockEntry entry : JSONLoader.blockEntries) {
			String[] blockSplit = entry.getBlockId().split("@");

			String blockName = entry.getName();

			int blockMeta = blockSplit.length > 1 ? Integer.parseInt(blockSplit[1]) : 0;
			String blockId = blockSplit[0];

			IBlockState blockState = Block.getBlockFromName(blockId).getStateFromMeta(blockMeta);

			// SPELEOTHEMS
			if(entry.hasSpeleothems()) {
				BlockSpeleothem speleothem = new BlockSpeleothem(blockState);
				speleothem.setRegistryName(Core.coreID, blockName + "_speleothem");
				speleothem.setCreativeTab(Core.wtfTab);

				blocks.add(speleothem);
				reg.register(speleothem);

				BlockSpeleothemFrozen frozenSpeleothem = new BlockSpeleothemFrozen(speleothem);
				frozenSpeleothem.setRegistryName(Core.coreID, "frozen_" + blockName + "_speleothem");
				frozenSpeleothem.setCreativeTab(Core.wtfTab);

				blocks.add(frozenSpeleothem);
				reg.register(frozenSpeleothem);
			}

			// STATIC DECOR
			for(BlockDecoStatic.StaticDecoType decorType : BlockDecoStatic.StaticDecoType.values()) {
				if(entry.getStaticDecorTypes().get(decorType)) {
					BlockDecoStatic decor = new BlockDecoStatic(blockState, decorType);
					decor.setRegistryName(Core.coreID, "decoration_" + decorType.getName() + "_" + blockName);
					decor.setCreativeTab(Core.wtfTab);

					blocks.add(decor);
					reg.register(decor);
				}
			}

			// ANIMATED DECOR
			for(BlockDecoAnim.AnimatedDecoType decorType : BlockDecoAnim.AnimatedDecoType.values()) {
				if(entry.getAnimatedDecorTypes().get(decorType)) {
					BlockDecoAnim decor = new BlockDecoAnim(blockState, decorType);
					decor.setRegistryName(Core.coreID, "decoration_" + decorType.getName() + "_" + blockName);
					decor.setCreativeTab(Core.wtfTab);

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

		if(MasterConfig.gameplaytweaks && GameplayConfig.homescroll)
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

			else if (WTFModelRegistry.metaMap.get(block.getRegistryName()) != null)
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

	public static <T extends Block> Block registerBlockWithoutItem(IForgeRegistry<Block> registry, T block, String name) {
		block.setRegistryName(Core.coreID, name);
		block.setUnlocalizedName(block.getRegistryName().toString());

		registry.register(block);
		return block;
	}

	public static <T extends Block> Block registerSimpleBlock(IForgeRegistry<Block> registry, T block, String name) {
		blocks.add(block);

		registerBlockWithoutItem(registry, block, name);
        return block;
    }

	public static <T extends Block> Block registerSimpleBlock(IForgeRegistry<Block> registry, T block, String name, int meta) {
		registerSimpleBlock(registry, block, name);
		WTFModelRegistry.metaMap.put(block.getRegistryName(), meta);

		return block;
	}

	public static <T extends Item> Item registerItem(IForgeRegistry<Item> registry, T item, String name) {
		item.setCreativeTab(Core.wtfTab);
		item.setRegistryName(Core.coreID, name);
		item.setUnlocalizedName(item.getRegistryName().toString());

		items.add(item);

		registry.register(item);
		return item;
	}

	public static <T extends ItemBlock> ItemBlock registerItemBlockNoModel(IForgeRegistry<Item> registry, T itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
		itemBlock.setHasSubtypes(true);
		itemBlock.setMaxDamage(0);

		registry.register(itemBlock);
		return itemBlock;
	}

	public static <T extends ItemBlock> ItemBlock registerItemBlock(IForgeRegistry<Item> registry, T itemBlock) {
		items.add(itemBlock);

		registerItemBlockNoModel(registry, itemBlock);
		return itemBlock;
	}
}
