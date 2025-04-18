package wtf.utilities.UBC;

import java.util.ArrayList;

import exterminatorjeff.undergroundbiomes.api.API;
import net.minecraft.block.state.IBlockState;
import org.apache.commons.lang3.tuple.Pair;
import wtf.WTFExpedition;
import wtf.init.BlockSets;
import wtf.enums.Modifier;

public class UBCCompat {

	public static final String[] IgneousStoneList = new String[]{"red_granite", "black_granite", "rhyolite", "andesite", "gabbro", "basalt", "komatiite", "dacite"};
	public static final String[] MetamorphicStoneList = new String[]{"gneiss", "eclogite", "marble", "quartzite", "blueschist", "greenschist", "soapstone", "migmatite"};
	public static final String[] SedimentaryStoneList = new String[]{"limestone", "chalk", "shale", "siltstone", "lignite", "dolomite", "greywacke", "chert"};

	public static final String[] IgneousCobblestoneList = new String[]{"redGraniteCobble", "blackGraniteCobble", "rhyoliteCobble", "andesiteCobble", "gabbroCobble", "basaltCobble", "komatiiteCobble", "daciteCobble"};
	public static final String[] MetamorphicCobblestoneList = new String[]{"gneissCobble", "eclogiteCobble", "marbleCobble", "quartziteCobble", "blueschistCobble", "greenschistCobble", "soapstoneCobble", "migmatiteCobble"};


	public static IBlockState[] IgneousStone;
	public static IBlockState[] IgneousCobblestone;
	public static IBlockState[] IgneousBrick;
	public static IBlockState[] MetamorphicStone;
	public static IBlockState[] MetamorphicCobblestone;
	public static IBlockState[] MetamorphicBrick;
	public static IBlockState[] SedimentaryStone;

	public static ArrayList<String> UBCStoneList = new ArrayList<String>();

	public static void loadUBCStone(){

		WTFExpedition.wtfLog.info("Getting UBC stones");
		
		//new ReplacerVanillaStone();
		

		System.out.println("Registering UBC Sedimentary Sand");
		
		IgneousStone = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			IgneousStone[loop] = API.IGNEOUS_STONE.getBlock().getStateFromMeta(loop);
			UBCStoneList.add(IgneousStone[loop].getBlock().getRegistryName()+"@"+loop);


			
		}

		IgneousCobblestone = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			IgneousCobblestone[loop] = API.IGNEOUS_COBBLE.getBlock().getStateFromMeta(loop);
			UBCStoneList.add(IgneousCobblestone[loop].getBlock().getRegistryName()+"@"+loop);



		}
		
		IgneousBrick = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			IgneousBrick[loop] = API.IGNEOUS_BRICK.getBlock().getStateFromMeta(loop);
			BlockSets.blockTransformer.put(Pair.of(IgneousStone[loop], Modifier.BRICK), IgneousBrick[loop]);
		}

		MetamorphicStone = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			MetamorphicStone[loop] = API.METAMORPHIC_STONE.getBlock().getStateFromMeta(loop);
			UBCStoneList.add(MetamorphicStone[loop].getBlock().getRegistryName()+"@"+loop);


		}

		MetamorphicCobblestone = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			MetamorphicCobblestone[loop] = API.METAMORPHIC_COBBLE.getBlock().getStateFromMeta(loop);

			UBCStoneList.add(MetamorphicCobblestone[loop].getBlock().getRegistryName()+"@"+loop);


		}
		
		MetamorphicBrick = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			MetamorphicBrick[loop] = API.METAMORPHIC_BRICK.getBlock().getStateFromMeta(loop);
			BlockSets.blockTransformer.put(Pair.of(MetamorphicStone[loop], Modifier.BRICK), MetamorphicBrick[loop]);
		}

		SedimentaryStone = new IBlockState[8];
		for (int loop = 0; loop < 8; loop++){
			SedimentaryStone[loop] = API.SEDIMENTARY_STONE.getBlock().getStateFromMeta(loop);



			// Also not sure about this one either, chief
			// GameRegistry.addShapelessRecipe(API.SEDIMENTARY_STONE.getItemBlock().getRegistryName(), null, new ItemStack(API.SEDIMENTARY_STONE.getBlock(), 4, loop), Ingredient.fromStacks(new ItemStack(WTFContent.ubcSand, 1, loop), new ItemStack(WTFContent.ubcSand, 1, loop), new ItemStack(WTFContent.ubcSand, 1, loop), new ItemStack(WTFContent.ubcSand, 1, loop)));

			UBCStoneList.add(SedimentaryStone[loop].getBlock().getRegistryName()+"@"+loop);


		}

		
		//StainedGlass

//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 0), new ItemStack(Blocks.STAINED_GLASS, 1, 9), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 1), new ItemStack(Blocks.STAINED_GLASS, 1, 0), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 2), new ItemStack(Blocks.STAINED_GLASS, 1, 8), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 3), new ItemStack(Blocks.STAINED_GLASS, 1, 1), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 4), new ItemStack(Blocks.STAINED_GLASS, 1, 15), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 5), new ItemStack(Blocks.STAINED_GLASS, 1, 4), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 6), new ItemStack(Blocks.STAINED_GLASS, 1, 7), 0F);
//		GameRegistry.addSmelting(new ItemStack(WTFContent.ubcSand, 1, 7), new ItemStack(Blocks.STAINED_GLASS, 1, 12), 0F);
		//GameRegistry.addSmelting(new ItemStack(Blocks.SAND, 1, 1), new ItemStack(Blocks.STAINED_GLASS, 1, 14), 0F);
		
		new ReplacerUBCMossyCobble();
		
	}




}
