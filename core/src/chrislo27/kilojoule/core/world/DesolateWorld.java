package chrislo27.kilojoule.core.world;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.generation.GeneratorSettings;
import chrislo27.kilojoule.core.generation.step.BiomeStep.BiomeRange;
import chrislo27.kilojoule.core.registry.Biomes;
import chrislo27.kilojoule.core.universe.Universe;

public class DesolateWorld extends World {

	public DesolateWorld(Universe un, int sizex, int sizey) {
		super(un, sizex, sizey);
	}

	@Override
	public void assignBiomes(Array<BiomeRange> rangeArray) {
		rangeArray.add(new BiomeRange(Biomes.getBiome("wasteland"), 1));
	}

	@Override
	public GeneratorSettings getGeneratorSettings() {
		GeneratorSettings gs = new GeneratorSettings(this);
		
		return gs;
	}

}
