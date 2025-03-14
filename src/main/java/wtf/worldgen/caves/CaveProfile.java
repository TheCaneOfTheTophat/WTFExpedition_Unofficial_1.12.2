package wtf.worldgen.caves;

import java.util.ArrayList;
import java.util.Random;

import wtf.utilities.wrappers.CaveListWrapper;
import wtf.worldgen.GeneratorMethods;
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

	public AbstractDungeonType getDungeonForCave(GeneratorMethods gen, Random random, CaveListWrapper cave, int surface) {
		for (int loop = 0; loop < 5; loop++) {
			float height = (float) cave.getAvgFloor() / (surface);

			if (height < 0.33) {
				int chance = random.nextInt(dungeonDeep.size());

				if (dungeonDeep.get(chance).canGenerateAt(gen, cave))
					return dungeonDeep.get(chance);
			} else if (height < 0.66) {
				int chance = random.nextInt(dungeonMid.size());

				if (dungeonMid.get(chance).canGenerateAt(gen, cave))
					return dungeonMid.get(chance);
			} else {
				int chance = random.nextInt(dungeonShallow.size());

				if (dungeonShallow.get(chance).canGenerateAt(gen, cave))
					return dungeonShallow.get(chance);
			}
		}

		return null;
	}
}
