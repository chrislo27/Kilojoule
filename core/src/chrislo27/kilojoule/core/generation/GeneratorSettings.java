package chrislo27.kilojoule.core.generation;

import chrislo27.kilojoule.core.world.World;

public class GeneratorSettings {

	public final World world;

	public int hillHeight = 16;
	public int seaLevel = 128;

	public GeneratorSettings(World world) {
		this.world = world;
		
		seaLevel = world.worldHeight - seaLevel;
	}

}
