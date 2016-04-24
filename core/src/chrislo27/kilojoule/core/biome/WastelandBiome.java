package chrislo27.kilojoule.core.biome;

import com.badlogic.gdx.graphics.Color;

import chrislo27.kilojoule.core.generation.BiomeGeneratorSettings;
import chrislo27.kilojoule.core.generation.settings.BlockLayer;
import chrislo27.kilojoule.core.registry.Blocks;

public class WastelandBiome extends Biome {

	public WastelandBiome(Color foliage) {
		super(foliage);

		BiomeGeneratorSettings settings = this.generatorSettings;

		settings.hillAmplification = 0.5f;
		settings.blockLayers.clear();
		settings.blockLayers.add(new BlockLayer(Blocks.getBlock("stone"), 1));
	}

}
