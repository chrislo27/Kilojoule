package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.biome.Biome;
import chrislo27.kilojoule.core.biome.WastelandBiome;

public class Biomes {

	private static Biomes instance;

	private Biomes() {
	}

	public static Biomes instance() {
		if (instance == null) {
			instance = new Biomes();
			instance.loadResources();
		}
		return instance;
	}

	private ObjectMap<String, Biome> biomes = new ObjectMap<>();

	private void loadResources() {
		addBiome("hills", new Biome(new Color(0, 175 / 255f, 17 / 255f, 1)));
		addBiome("desert", new Biome(new Color(106 / 255f, 150 / 255f, 30 / 255f, 1)));
		addBiome("wasteland", new WastelandBiome(new Color(106 / 255f, 150 / 255f, 30 / 255f, 1)));
	}

	public static Biome getBiome(String key) {
		return instance().biomes.get(key);
	}

	public void addBiome(String key, Biome biome) {
		biomes.put(key, biome);
	}

}
