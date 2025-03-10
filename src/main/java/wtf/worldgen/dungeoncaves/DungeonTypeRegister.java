package wtf.worldgen.dungeoncaves;

import java.util.ArrayList;

import wtf.config.WTFExpeditionConfig;
import wtf.worldgen.dungeoncaves.ambient.DungeonJungleTemple;
import wtf.worldgen.dungeoncaves.ambient.DungeonSpeleothemGrotto;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeBatCave;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeCaveIn;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeFoxfire;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeFrozenSolid;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypePrismarine;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeRain;
import wtf.worldgen.dungeoncaves.ambient.DungeonTypeSoulsand;
import wtf.worldgen.dungeoncaves.mob.*;

public class DungeonTypeRegister {
	
	private static AbstractDungeonType Skeleton = new DungeonSimpleSkeleton("SkeletonClassic");
	private static AbstractDungeonType Zombie = new DungeonSimpleZombie("ZombieClassic");
	private static AbstractDungeonType MagmaSlime = new DungeonSimpleMagma("MagmaSlime");
	private static AbstractDungeonType Blaze = new DungeonBlaze("Blaze");
	private static AbstractDungeonType Stray = new DungeonSimpleStray("Stray");
	private static AbstractDungeonType Husk = new DungeonSimpleHusk("Husk");
	
	private static AbstractDungeonType SkeletonKnight = new DungeonSimpleSkeletonKnight("SkeletonKnight");
	private static AbstractDungeonType Spider = new DungeonSimpleSpider("ClassicSpider");
	private static AbstractDungeonType Slime = new DungeonSlime("Slime");
	public static AbstractDungeonType Mummy = new DungeonTypeMummy("Mummy");
	private static AbstractDungeonType Golem = new DungeonTypeDerangedGolem("DerangedGolem");
	public static AbstractDungeonType Bat = new DungeonTypeBatCave("BatCave", 10, 0);
	
	public static AbstractDungeonType CaveIn = new DungeonTypeCaveIn("Cavein");
	public static AbstractDungeonType Grotto = new DungeonSpeleothemGrotto("SpeleothemGrotto", 50, 50);
	public static AbstractDungeonType Mine = new DungeonMine("Mine");
	public static AbstractDungeonType BogLantern = new DungeonBogLantern("BogLangern");
	public static AbstractDungeonType ZombieFarm = new DungeonZombieFarmer("ZombieFarm");

	private static AbstractDungeonType Foxfire = new DungeonTypeFoxfire("Foxfire", 10, 10);
	private static AbstractDungeonType Frozen = new DungeonTypeFrozenSolid("FrozenSolid");
	private static AbstractDungeonType Rainstone = new DungeonTypeRain("Rain", 5, 5);
	private static AbstractDungeonType JungleTemple = new DungeonJungleTemple("JungleTemple");
	

	private static AbstractDungeonType Soulsand = new DungeonTypeSoulsand("SoulSand", 5, 5);
	
	private static AbstractDungeonType SkeletonMage = new DungeonTypeSkeletonMage();
	private static AbstractDungeonType Prismarine = new DungeonTypePrismarine("Prismarine", 5, 5);

	private static AbstractDungeonType NetherPortal = new DungeonTypeNetherPortal("NetherPortal");
	private static AbstractDungeonType FireElemental = new DungeonFireElemental("FireElemental");
	
	public static ArrayList<AbstractDungeonType> defaultList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(CaveIn);
			list.add(Grotto);
			list.add(Bat);
					
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Skeleton);
			list.add(Zombie);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> forestList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			
			list.add(Foxfire);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Spider);
			list.add(ZombieFarm);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> lushList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			
			list.add(Foxfire);
			list.add(JungleTemple);
			list.add(Rainstone);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Spider);
			
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> coldList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(Frozen);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Stray);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> wetList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(Rainstone);
			list.add(Prismarine);
			list.add(BogLantern);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Slime);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> volcanicList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(JungleTemple);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(MagmaSlime);
			list.add(FireElemental);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> desertList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(Soulsand);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Husk);
			list.add(Mummy);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> netherList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){
			list.add(Soulsand);
		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(NetherPortal);
			list.add(Blaze);
			list.add(SkeletonKnight);
			list.add(SkeletonMage);
		}
		return list;
	}
	
	public static ArrayList<AbstractDungeonType> mountainList(){
		ArrayList<AbstractDungeonType> list = new ArrayList<AbstractDungeonType>();
		if (WTFExpeditionConfig.enableSubtypes){

		}
		if (WTFExpeditionConfig.enableDungeons){
			list.add(Golem);
			list.add(Mine);			
		}
		return list;
	}
	
}
