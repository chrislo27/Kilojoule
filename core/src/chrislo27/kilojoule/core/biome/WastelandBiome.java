package chrislo27.kilojoule.core.biome;

import com.badlogic.gdx.graphics.Color;

import chrislo27.kilojoule.core.generation.BiomeGeneratorSettings;

public class WastelandBiome extends Biome {

	public WastelandBiome(Color foliage) {
		super(foliage);

		BiomeGeneratorSettings settings = this.generatorSettings;

		settings.hillAmplification = 0.5f;
	}

}
