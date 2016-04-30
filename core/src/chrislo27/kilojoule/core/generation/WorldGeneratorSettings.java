package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.graphics.Color;

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

	/** The colour of darkness (default black) **/
	public Color shadowColor = new Color(0, 0, 0, 1);

	/** The tint colour multiplied to all light (default white) **/
	public Color allLightTint = new Color(1, 1, 1, 1);

	/** The colour of the sky light (default white) **/
	public Color skyLightTint = new Color(1, 1, 1, 1);

	/** The colour of all artificial light (default white) **/
	public Color artificialLightTint = new Color(1, 1, 1, 1);

	public WorldGeneratorSettings(World world) {
		this.world = world;

		seaLevel = world.worldHeight - seaLevel;
	}

}
