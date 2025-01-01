
package wtf.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import wtf.Core;
import wtf.blocks.*;
import wtf.client.WTFModels;
import wtf.config.StoneRegEntry;
import wtf.config.WTFStoneRegistry;
import wtf.blocks.OreSandGoldNugget;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(Core.coreID)
public class WTFBlocks{

	public static HashMap<IBlockState, BlockSpeleothem> speleothemMap = new HashMap<IBlockState, BlockSpeleothem>();

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

	public static ArrayList<Block> blocks = new ArrayList<>();

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();


		// Blocks in the Creative tab
		registerBlock(reg, new BlockNitreOre().setCreativeTab(Core.wtfTab), "nitre_ore");
		registerBlock(reg, new OreSandGoldNugget().setCreativeTab(Core.wtfTab), "gold_ore_sand");
		registerBlock(reg, new BlockRedCactus().setCreativeTab(Core.wtfTab), "red_cactus");
		registerBlock(reg, new BlockMycorrack().setCreativeTab(Core.wtfTab), "mycorrack");

		registerBlock(reg, new BlockFoxfire().setCreativeTab(Core.wtfTab), "foxfire");

		registerBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "dirt_patch");
		registerBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "mossy_dirt_patch");
		registerBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState()), "sand_patch");
		registerBlock(reg, new BlockPatch(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)), "red_sand_patch");
		registerBlock(reg, new BlockPatch(Blocks.GRAVEL.getDefaultState()), "gravel_patch");
		registerBlock(reg, new BlockPatch(Blocks.DIRT.getDefaultState()), "podzol_patch");
		registerBlock(reg, new BlockPuddle().setCreativeTab(Core.wtfTab), "puddle");
		registerBlock(reg, new BlockIcePatch().setCreativeTab(Core.wtfTab), "ice_patch");
		registerBlock(reg, new BlockPatch(Blocks.HARDENED_CLAY.getDefaultState()), "terracotta_patch");
		registerBlock(reg, new BlockStainedTerracottaPatch().setCreativeTab(Core.wtfTab), "terracotta_patch_stained", 15);

		// new NetherrackReplacer();
		registerBlock(reg, new BlockIcicle().setCreativeTab(Core.wtfTab), "icicle", 2);
		registerBlock(reg, new BlockRoots().setCreativeTab(Core.wtfTab), "roots", 5);
		// BlockstateWriter.writeDecoStaticBlockstate(Blocks.DIRT.getDefaultState(), "dirt0DecoStatic");
		// wcicTable = registerBlock(new WCICTable(), "wcic_table");
		// GameRegistry.registerTileEntity(WCICTileEntity.class, "WCICTable");

		// BlockWTFTorch.torch_off = new BlockWTFTorch(false);

		registerBlockWithoutItem(reg, new Block(Material.AIR), "base");
		registerBlockWithoutItem(reg, new Block(Material.AIR), "gen_marker");

		registerBlock(reg, new BlockNaturalSandstone(Blocks.SANDSTONE.getDefaultState()), "natural_sandstone");
		registerBlock(reg, new BlockNaturalSandstone(Blocks.RED_SANDSTONE.getDefaultState()), "natural_red_sandstone");

	}
	
	public static void initDependentBlocks(){	
		
		for(Entry<IBlockState, StoneRegEntry> entry : WTFStoneRegistry.stoneReg.entrySet()){
			
			String stoneName = entry.getKey().getBlock().getRegistryName().toString().split(":")[1] + entry.getKey().getBlock().getMetaFromState(entry.getKey());
			//String cobbleName = entry.getValue().cobble.getBlock().getRegistryName().toString().split(":")[1] + entry.getValue().cobble.getBlock().getMetaFromState(entry.getValue().cobble);
			
			
			String localisedName = StringUtils.capitalize(entry.getValue().textureLocation.split("/")[1].replaceAll("_", " "));
			
			if (entry.getValue().speleothem){
				// registerBlockItemSubblocks(new BlockSpeleothem(entry.getKey()).setFrozen(stoneName + "Speleothem"), 6, stoneName + "Speleothem");// .setFrozen("stoneSpeleothem");
				Core.proxy.writeSpeleothemBlockstate(entry.getKey(), stoneName + "Speleothem");
				
				Core.proxy.addName(stoneName+"Speleothem.0", localisedName + " Small Stalactite");
				Core.proxy.addName(stoneName+"Speleothem.1", localisedName + " Stalactite Base");
				Core.proxy.addName(stoneName+"Speleothem.2", localisedName + " Stalactite Tip");
				Core.proxy.addName(stoneName+"Speleothem.3", localisedName + " Column");
				Core.proxy.addName(stoneName+"Speleothem.4", localisedName + " Small Stalagmite");
				Core.proxy.addName(stoneName+"Speleothem.5", localisedName + " Stalagmite Base");
				Core.proxy.addName(stoneName+"Speleothem.6", localisedName + " Stalagmite Tip");
				
				Core.proxy.addName(stoneName+"SpeleothemFrozen.0", localisedName + " Icy Small Stalactite");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.1", localisedName + " Icy Stalactite Base");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.2", localisedName + " Icy Stalactite Tip");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.3", localisedName + " Icy Column");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.4", localisedName + " Icy Small Stalagmite");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.5", localisedName + " Icy Stalagmite Base");
				Core.proxy.addName(stoneName+"SpeleothemFrozen.6", localisedName + " Icy Stalagmite Tip");
			}
			
			if (entry.getValue().decoAnim){
				// registerBlockItemSubblocks(new BlockDecoAnim(entry.getKey()), BlockDecoAnim.ANIMTYPE.values().length-1, stoneName+"DecoAnim");
				Core.proxy.writeDecoAnimBlockstate(entry.getKey(), stoneName+"DecoAnim");
				
				Core.proxy.addName(stoneName+"DecoAnim.0", localisedName+ " Lava Crust");
				Core.proxy.addName(stoneName+"DecoAnim.1", "Wet " + localisedName);
				Core.proxy.addName(stoneName+"DecoAnim.2", "Dripping Lava " + localisedName);
			}
			
			if (entry.getValue().decoStatic){
				// registerBlockItemSubblocks(new BlockDecoStatic(entry.getKey()), BlockDecoStatic.DecoType.values().length-1, stoneName+"DecoStatic");
				Core.proxy.writeDecoStaticBlockstate(entry.getKey(), stoneName+"DecoStatic");
				
				Core.proxy.addName(stoneName+"DecoStatic.0", "Mossy " + localisedName);
				Core.proxy.addName(stoneName+"DecoStatic.1", "Soul "+localisedName);
				Core.proxy.addName(stoneName+"DecoStatic.2", "Cracked "+localisedName);
			}
		}
		
		//registerBlockItemSubblocks(new RedstoneStalactite(false).setFrozen("redstoneSpeleothem"), 6, "redstoneSpeleothem");// .setFrozen("stoneSpeleothem");
		//BlockstateWriter.writeSpeleothemBlockstate(Blocks.REDSTONE_ORE.getDefaultState(), "redstoneSpeleothem");
		
		//registerBlockItemSubblocks(new RedstoneStalactite(false).setFrozen("redstoneSpeleothem_on"), 6, "redstoneSpeleothem_on");// .setFrozen("stoneSpeleothem");
		//BlockstateWriter.writeSpeleothemBlockstate(Blocks.LIT_REDSTONE_ORE.getDefaultState(), "redstoneSpeleothem_on");
		
	
	}

	public static <T extends Block> Block registerBlockWithoutItem(IForgeRegistry<Block> registry, T block, String name) {
		block.setRegistryName(Core.coreID, name);
		block.setUnlocalizedName(block.getRegistryName().toString());

		registry.register(block);
		return block;
	}

	public static <T extends Block> Block registerBlock(IForgeRegistry<Block> registry, T block, String name) {
		blocks.add(block);

		registerBlockWithoutItem(registry, block, name);
        return block;
    }

	public static <T extends Block> Block registerBlock(IForgeRegistry<Block> registry, T block, String name, int meta) {
		registerBlock(registry, block, name);
		WTFModels.metaMap.put(block.getRegistryName(), meta);

		return block;
	}
}
