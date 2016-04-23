package chrislo27.kilojoule.core.generation;

import chrislo27.kilojoule.core.world.World;

public class WorldGeneratorSettings {

	public final World world;

	public int hillHeight = 16;
	public int seaLevel = 128;

	public WorldGeneratorSettings(World world) {
		this.world = world;
		
		seaLevel = world.worldHeight - seaLevel;
	}

}
