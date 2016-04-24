package chrislo27.kilojoule.core.generation;

import chrislo27.kilojoule.core.world.World;

public class WorldGeneratorSettings {

	public final World world;

	public int hillHeight = 16;
	public int seaLevel = 256;

	public float caveSampleCoefficientWidth = 0.065f;
	public float caveSampleCoefficientHeight = caveSampleCoefficientWidth * 2;
	public float caveSampleFrom = 0f;
	public float caveSampleTo = 0.25f;
	public float caveThicknessSealevel = 1;
	public float caveThicknessBottom = 1.75f;

	public WorldGeneratorSettings(World world) {
		this.world = world;

		seaLevel = world.worldHeight - seaLevel;
	}

}
