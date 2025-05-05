package wtf.worldgen.dungeoncaves;

import java.util.ArrayList;
import java.util.List;

import wtf.config.WTFExpeditionConfig;
import wtf.worldgen.dungeoncaves.ambient.*;
import wtf.worldgen.dungeoncaves.mob.*;

public class DungeonTypeRegister {
	
	private static final AbstractDungeonType Skeleton = new DungeonSimpleSkeleton("SkeletonClassic");
	private static final AbstractDungeonType Stray = new DungeonSimpleStray("Stray");
	private static final AbstractDungeonType SkeletonMage = new DungeonTypeSkeletonMage();
	private static final AbstractDungeonType SkeletonKnight = new DungeonSimpleSkeletonKnight("SkeletonKnight");

	private static final AbstractDungeonType Zombie = new DungeonSimpleZombie("ZombieClassic");
	private static final AbstractDungeonType Husk = new DungeonSimpleHusk("Husk");
	private static final AbstractDungeonType Mummy = new DungeonTypeMummy("Mummy");
	private static final AbstractDungeonType ZombieFarm = new DungeonZombieFarmer("ZombieFarm");
	private static final AbstractDungeonType ZombieLumberjack = new DungeonZombieLumberjack("ZombieLumberjack");
	private static final AbstractDungeonType Mine = new DungeonMine("Mine");

	private static final AbstractDungeonType NetherPortal = new DungeonTypeNetherPortal("NetherPortal");
	private static final AbstractDungeonType MagmaSlime = new DungeonSimpleMagma("MagmaSlime");
	private static final AbstractDungeonType Blaze = new DungeonBlaze("Blaze");
	private static final AbstractDungeonType Spider = new DungeonSimpleSpider("ClassicSpider");
	private static final AbstractDungeonType Slime = new DungeonSlime("Slime");
	private static final AbstractDungeonType Golem = new DungeonTypeDerangedGolem("DerangedGolem");

	private static final AbstractDungeonType FireElemental = new DungeonFireElemental("FireElemental");
	private static final AbstractDungeonType CursedArmor = new DungeonCursedArmor("CursedArmor");
	private static final AbstractDungeonType Blockhead = new DungeonBlockhead("Blockhead");

	private static final AbstractDungeonType CaveIn = new DungeonTypeCaveIn("CaveIn");
	private static final AbstractDungeonType Grotto = new DungeonSpeleothemGrotto("SpeleothemGrotto", 50, 50);
	private static final AbstractDungeonType Bat = new DungeonTypeBatCave("BatCave", 10, 0);

	private static final AbstractDungeonType Prismarine = new DungeonTypePrismarine("Prismarine", 5, 5);
	private static final AbstractDungeonType BogLantern = new DungeonBogLantern("BogLantern");
	private static final AbstractDungeonType Rain = new DungeonTypeRain("Rain", 5, 5);

	private static final AbstractDungeonType Foxfire = new DungeonTypeFoxfire("Foxfire", 10, 10);

	private static final AbstractDungeonType FrozenSolid = new DungeonTypeFrozenSolid("FrozenSolid");

	private static final AbstractDungeonType JungleTemple = new DungeonJungleTemple("JungleTemple");

	private static final AbstractDungeonType SoulSand = new DungeonTypeSoulsand("SoulSand", 5, 5);
	
	public static ArrayList<AbstractDungeonType> defaultList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();
		
		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(CaveIn);
			list.add(Grotto);
			list.add(Bat);
		}
		
		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Skeleton);
			list.add(Zombie);
		}
		
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> forestList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(Foxfire);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Spider);
			list.add(ZombieFarm);
			list.add(ZombieLumberjack);
		}

		return list;
	}

	public static ArrayList<AbstractDungeonType> spookyList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {

		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(CursedArmor);
			list.add(Blockhead);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> lushList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(Foxfire);
			list.add(JungleTemple);
			list.add(Rain);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Spider);
			list.add(ZombieFarm);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> coldList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(FrozenSolid);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Stray);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> wetList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(Rain);
			list.add(Prismarine);
			list.add(BogLantern);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Slime);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> volcanicList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(JungleTemple);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(MagmaSlime);
			list.add(FireElemental);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> desertList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(SoulSand);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Husk);
			list.add(Mummy);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> netherList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {
			list.add(SoulSand);
		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(NetherPortal);
			list.add(Blaze);
			list.add(SkeletonKnight);
			list.add(SkeletonMage);
		}

		return list;
	}
	
	public static ArrayList<AbstractDungeonType> mountainList() {
		ArrayList<AbstractDungeonType> list = new ArrayList<>();

		if (WTFExpeditionConfig.enableSubtypes) {

		}

		if (WTFExpeditionConfig.enableDungeons) {
			list.add(Golem);
			list.add(Mine);			
		}

		return list;
	}
}
