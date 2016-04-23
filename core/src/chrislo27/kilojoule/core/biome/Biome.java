package chrislo27.kilojoule.core.biome;

import com.badlogic.gdx.graphics.Color;

import chrislo27.kilojoule.core.generation.BiomeGeneratorSettings;

public class Biome {

	public Color foliageColor = new Color();
	public BiomeGeneratorSettings generatorSettings = new BiomeGeneratorSettings();

	public Biome(Color foliage) {
		foliageColor = new Color(foliage.r, foliage.g, foliage.b, 1);
	}

}
