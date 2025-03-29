package wtf.worldgen.subbiomes;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.config.WTFExpeditionConfig;
import wtf.utilities.simplex.Simplex;

public class BiomeAutumnForest extends BiomeForest implements SubBiome {

	private final Biome parentBiome;
	
	public BiomeAutumnForest(Type typeIn, BiomeProperties properties, Biome parentBiome) {
		super(typeIn, properties);
		this.parentBiome = parentBiome;
	}
	
	Simplex simplex = new Simplex(17781);
	
    @Override
	@SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
    	int noise = (int) (simplex.noise(pos.getX()/ WTFExpeditionConfig.autumnForestColorScale, pos.getZ()/WTFExpeditionConfig.autumnForestColorScale) * 9 + 9);
    	return autumnLeaf[noise];
    	
    	//Leaves are coloured by returning a value from a range of 18 colours, based on the last digits of the x/y position
    	
    	//return autumnLeaf[MathHelper.abs_int(pos.getX())/8%10 + MathHelper.abs_int(pos.getZ())/8%10]; 
    }
    
    private static final int[] autumnLeaf = {15924992, 16776960, 16773632, 16770560, 16767232, 16763904,
    		16760576, 16757504, 16754176, 16750848, 16747520, 16744448,
    		16741120, 16737792, 16734464, 16731392, 16728064, 16724736, 16721408};


	@Override
	public void resetTopBlock(World world, BlockPos pos) {}

	@Override
	public double scale() {
		return WTFExpeditionConfig.autumnForestSize;
	}

	@Override
	public double freq() {
		return WTFExpeditionConfig.autumnForestPercentageFrequency / 100D;
	}

	@Override
	public Biome getBiome() {
		return this;
	}
	
	private byte ID = -1;

	@Override
	public byte getID() {
		if (ID == -1)
			ID = (byte) Biome.getIdForBiome(this);
		return ID; 
	}

	@Override
	public Biome getParentBiome() {
		return this.parentBiome;
	}

	@Override
	public WorldGenerator getTree(Random random) {
		return parentBiome.getRandomTreeFeature(random);
	}
}
