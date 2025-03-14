package wtf.worldgen.dungeoncaves.mob;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import wtf.WTFExpedition;
import wtf.blocks.BlockFoxfire;
import wtf.init.WTFContent;
import wtf.utilities.wrappers.CaveListWrapper;
import wtf.utilities.wrappers.CavePosition;
import wtf.worldgen.GeneratorMethods;
import wtf.worldgen.dungeoncaves.AbstractDungeonType;

public class DungeonZombieFarmer extends AbstractDungeonType {

	public DungeonZombieFarmer(String name) {
		super(name, 5, 15);
	}
	
	@Override
	public void generateCeilingAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		gen.replaceBlock(pos.up(2), Blocks.LOG.getDefaultState());
		gen.replaceBlock(pos.up(), Blocks.LOG.getDefaultState());
		gen.replaceBlock(pos, Blocks.LOG.getDefaultState());
		gen.replaceBlock(pos.down(), Blocks.LOG.getDefaultState());
		gen.replaceBlock(pos.down(2), WTFContent.foxfire.getDefaultState().withProperty(BlockFoxfire.HANGING, true));
	}
	
	@Override
	public void generateFloor(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		gen.replaceBlock(pos, Blocks.MYCELIUM.getDefaultState());
	}
	
	@Override
	public void generateCenter(GeneratorMethods gen, Random rand, CavePosition pos, float depth) {
		gen.spawnVanillaSpawner(pos.getFloorPos().up(), new ResourceLocation(WTFExpedition.modID,"zombie_farmer"), 2);
	}

	@Override
	public boolean canGenerateAt(GeneratorMethods gen, CaveListWrapper cave) {
		return false;
	}

	@Override
	public void generateCeiling(GeneratorMethods gen, Random random, BlockPos pos, float depth) {}

	@Override
	public void generateFloorAddons(GeneratorMethods gen, Random random, BlockPos pos, float depth) {
		gen.replaceBlock(pos, Blocks.BROWN_MUSHROOM.getDefaultState());
	}

	@Override
	public void generateWall(GeneratorMethods gen, Random random, BlockPos pos, float depth, int height) {}
}
