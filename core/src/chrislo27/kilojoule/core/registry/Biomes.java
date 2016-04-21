package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.biome.Biome;

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
		addBiome("hills", new Biome(new Color(0, 175f / 255f, 17f / 255f, 1)));
		addBiome("desert", new Biome(new Color(184f / 255f, 184f / 255f, 61f / 255f, 1)));
	}

	public static Biome getBiome(String key) {
		return instance().biomes.get(key);
	}

	public void addBiome(String key, Biome biome) {
		biomes.put(key, biome);
	}

}
