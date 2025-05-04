package wtf.worldgen.caves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import wtf.WTFExpedition;
import wtf.config.WTFExpeditionConfig;
import wtf.worldgen.caves.types.*;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;
import wtf.worldgen.dungeoncaves.DungeonTypeRegister;


public class CaveTypeRegister {

	public static HashMap<Biome, CaveProfile> cavebiomemap = new HashMap<>();

	private static final int floorChance = 2;
	private static final int ceilingChance = 3;

	public static final AbstractCaveType simple = new CaveTypeDefault("default", floorChance, ceilingChance);

	public static final AbstractCaveType wet = new CaveTypeWet("wet", floorChance, ceilingChance + 1);

	public static final AbstractCaveType swamp = new CaveTypeSwamp("swamp", floorChance, ceilingChance + 2);
	public static final AbstractCaveType swampRocky = new CaveTypeSwampRocky("swampRocky", floorChance, ceilingChance + 3);

	public static final AbstractCaveType sand = new CaveTypeSandy("sand", floorChance, ceilingChance, false);
	public static final AbstractCaveType redSand = new CaveTypeSandy("redSand", floorChance, ceilingChance, true);
	public static final AbstractCaveType sandRocky = new CaveTypeRockySandy("sandRocky", floorChance, ceilingChance + 3, false);
	public static final AbstractCaveType redSandRocky = new CaveTypeRockySandy("redSandRocky", floorChance, ceilingChance + 3, true);
	public static final AbstractCaveType paintedDesert = new CaveTypePaintedDesert("paintedDesert", 0, ceilingChance);

	public static final AbstractCaveType lush = new CaveTypeLush("lush", floorChance + 3, ceilingChance + 12);
	public static final AbstractCaveType lushVolcanic = new CaveTypeJungleVolcano("lushVolcanic", floorChance + 3, ceilingChance+12);

	public static final AbstractCaveType rocky = new CaveTypeRocky("rocky", floorChance, ceilingChance + 3);

	public static final AbstractCaveType ice = new CaveTypeIce("ice", floorChance, ceilingChance + 3);
	public static final AbstractCaveType iceRocky = new CaveTypeIceRocky("iceRocky", floorChance, ceilingChance + 3);
	public static final AbstractCaveType permafrost = new CaveTypeIcePermafrost("permafrost", floorChance, ceilingChance + 4);

	public static final AbstractCaveType fungal = new CaveTypeFungal("fungal", floorChance, ceilingChance);

	public static final AbstractCaveType dirtWater = new CaveTypeDirtWater("dirtWater", floorChance, ceilingChance);

	public static final AbstractCaveType podzol = new CaveTypePodzol("podzol", floorChance, ceilingChance);
	public static final AbstractCaveType podzolRocky = new CaveTypePodzolRocky("podzolRocky", floorChance, ceilingChance);

	public static final AbstractCaveType mossy = new CaveTypeMossy("mossy", floorChance, ceilingChance);
	public static final AbstractCaveType mossyRocky = new CaveTypeMossyRocky("mossyRocky", floorChance, ceilingChance);

	public static final AbstractCaveType volcanic = new CaveTypeVolcanic("volcanic", floorChance, ceilingChance + 4);
	public static final AbstractCaveType sandyVolcanic = new CaveTypeSandyVolcanic("redSandyVolcanic", floorChance, ceilingChance + 4);
	public static final AbstractCaveType hypervolcanic = new CaveTypeHypervolcanic("hypervolcanic", 0, 0);

	public static final CaveTypeHell nether = new CaveTypeHell("nether", floorChance, ceilingChance);

	public static CaveProfile getCaveProfile(Biome biome) {
		return cavebiomemap.get(biome) != null ? cavebiomemap.get(biome) : getNewProfile(biome);
	}

	public static CaveProfile getNewProfile(Biome biome) {
		// ====================== SHALLOW CAVES ======================

		ArrayList<AbstractDungeonType> dungeonShallow = new ArrayList<>(DungeonTypeRegister.defaultList());
		AbstractCaveType shallow = simple;

		if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
			shallow = paintedDesert;
			dungeonShallow.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.hasType(biome,BiomeDictionary.Type.HILLS)) {
			if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SNOWY)) {
				shallow = iceRocky;
				dungeonShallow.addAll(DungeonTypeRegister.coldList());
			} else if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
				shallow = swampRocky;
				dungeonShallow.addAll(DungeonTypeRegister.wetList());
				dungeonShallow.addAll(DungeonTypeRegister.forestList());
			} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SAVANNA)) {
				shallow = redSandRocky;
				dungeonShallow.addAll(DungeonTypeRegister.desertList());
			} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SANDY)) {
				shallow = sandRocky;
				dungeonShallow.addAll(DungeonTypeRegister.desertList());
			} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.JUNGLE)) {
				shallow = lushVolcanic;
				dungeonShallow.addAll(DungeonTypeRegister.lushList());
			} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.CONIFEROUS)) {
				shallow = podzolRocky;
				dungeonShallow.addAll(DungeonTypeRegister.forestList());
				dungeonShallow.addAll(DungeonTypeRegister.coldList());
			} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.FOREST)) {
				shallow = mossyRocky;
				dungeonShallow.addAll(DungeonTypeRegister.forestList());
			} else {
				shallow = rocky;
			}

			dungeonShallow.addAll(DungeonTypeRegister.mountainList());
		} else if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
			shallow = ice;
			dungeonShallow.addAll(DungeonTypeRegister.coldList());
		} else if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
			shallow = wet;
			dungeonShallow.addAll(DungeonTypeRegister.wetList());
		} else if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
			shallow = swamp;
			dungeonShallow.addAll(DungeonTypeRegister.wetList());
			dungeonShallow.addAll(DungeonTypeRegister.forestList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SAVANNA)) {
			shallow = redSand;
			dungeonShallow.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SANDY)) {
			shallow = sand;
			dungeonShallow.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.JUNGLE)) {
			shallow = lush;
			dungeonShallow.addAll(DungeonTypeRegister.lushList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.MUSHROOM)) {
			shallow = fungal;
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.PLAINS)) {
			shallow = dirtWater;
			dungeonShallow.addAll(DungeonTypeRegister.wetList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.CONIFEROUS)) {
			shallow = podzol;
			dungeonShallow.addAll(DungeonTypeRegister.forestList());
			dungeonShallow.addAll(DungeonTypeRegister.coldList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.FOREST)) {
			if (BiomeDictionary.hasType(biome,BiomeDictionary.Type.LUSH)) {
				shallow = swamp;
				dungeonShallow.addAll(DungeonTypeRegister.forestList());
				dungeonShallow.addAll(DungeonTypeRegister.wetList());
			} else {
				shallow = mossy;
			}

			dungeonShallow.addAll(DungeonTypeRegister.forestList());
		}

		// ====================== MID CAVES ======================
		ArrayList<AbstractDungeonType> dungeonMid = new ArrayList<>(DungeonTypeRegister.defaultList());
		AbstractCaveType mid = rocky;

		if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SNOWY)) {
			mid = iceRocky;
			dungeonMid.addAll(DungeonTypeRegister.coldList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome,BiomeDictionary.Type.RIVER)) {
			mid = wet;
			dungeonMid.addAll(DungeonTypeRegister.wetList());
		} else if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
			mid = swampRocky;
			dungeonMid.addAll(DungeonTypeRegister.wetList());
			dungeonMid.addAll(DungeonTypeRegister.forestList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SAVANNA) || BiomeDictionary.hasType(biome,BiomeDictionary.Type.MESA)) {
			mid = redSandRocky;
			dungeonMid.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SANDY)) {
			mid = sandRocky;
			dungeonMid.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.JUNGLE)) {
			mid = lushVolcanic;
			dungeonMid.addAll(DungeonTypeRegister.volcanicList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.CONIFEROUS)) {
			mid = podzolRocky;
			dungeonMid.addAll(DungeonTypeRegister.forestList());
			dungeonMid.addAll(DungeonTypeRegister.coldList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.FOREST)) {
			mid = mossyRocky;
			dungeonMid.addAll(DungeonTypeRegister.forestList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.WET)) {
			mid = wet;
			dungeonMid.addAll(DungeonTypeRegister.wetList());
		}

		// ====================== DEEP CAVES ======================
		AbstractCaveType deep = volcanic;
		ArrayList<AbstractDungeonType> dungeonDeep = new ArrayList<>(DungeonTypeRegister.netherList());
		if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SNOWY)) {
			deep = WTFExpeditionConfig.enablePermafrostBiome ? permafrost : iceRocky;
			dungeonDeep.addAll(DungeonTypeRegister.coldList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.SANDY) || BiomeDictionary.hasType(biome,BiomeDictionary.Type.SAVANNA)) {
			deep = sandyVolcanic;
			dungeonDeep.addAll(DungeonTypeRegister.desertList());
		} else if(BiomeDictionary.hasType(biome,BiomeDictionary.Type.JUNGLE) && WTFExpeditionConfig.enableHypervolcanicBiome) {
			deep = hypervolcanic;
			dungeonDeep.addAll(DungeonTypeRegister.volcanicList());
		}

		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
			shallow = nether;
			mid = nether;
			deep = nether;
		}

		CaveProfile profile = new CaveProfile(deep, mid, shallow);
		WTFExpedition.wtfLog.debug("Setting up cave biomes for " + biome.getBiomeName() + " SHALLOW: " + shallow.name + " MID: " + mid.name + " DEEP: " + deep.name);
		profile.dungeonDeep = new ArrayList<>(new HashSet<>(dungeonDeep));
		profile.dungeonMid = new ArrayList<>(new HashSet<>(dungeonMid));
		profile.dungeonShallow = new ArrayList<>(new HashSet<>(dungeonShallow));

		cavebiomemap.put(biome, profile);
		return profile;
	}
}
