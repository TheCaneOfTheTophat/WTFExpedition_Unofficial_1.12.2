package wtf.worldgen.caves;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

public class CaveProfile {

	public final AbstractCaveType caveDeep;
	public final AbstractCaveType caveMid;
	public final AbstractCaveType caveShallow;
	
	public ArrayList<AbstractDungeonType> dungeonShallow;
	public ArrayList<AbstractDungeonType> dungeonMid;
	public ArrayList<AbstractDungeonType> dungeonDeep;
	
	public CaveProfile(AbstractCaveType deepType, AbstractCaveType midType, AbstractCaveType shallowType) {
		caveDeep = deepType;
		caveMid = midType;
		caveShallow = shallowType;
	}
	
	public AbstractCaveType getCave(double d) {
		//float height = ((float)cave.floor)/((float)surface);
		if (d < 0.33)
			return caveDeep;
		else if (d < 0.66)
			return caveMid;
		else
			return caveShallow;
	}

	public AbstractDungeonType getDungeonForCave(World world, Random rand, CaveListWrapper cave, int surface) {
		for (int loop = 0; loop < 5; loop++) {
			float height = (float) cave.getAvgFloor() / (surface);

			if (height < 0.33) {
				int chance = rand.nextInt(dungeonDeep.size());

				if (dungeonDeep.get(chance).canGenerateAt(world, cave))
					return dungeonDeep.get(chance);
			} else if (height < 0.66) {
				int chance = rand.nextInt(dungeonMid.size());

				if (dungeonMid.get(chance).canGenerateAt(world, cave))
					return dungeonMid.get(chance);
			} else {
				int chance = rand.nextInt(dungeonShallow.size());

				if (dungeonShallow.get(chance).canGenerateAt(world, cave))
					return dungeonShallow.get(chance);
			}
		}

		return null;
	}
}
