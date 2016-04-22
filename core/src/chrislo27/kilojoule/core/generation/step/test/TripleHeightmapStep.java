package chrislo27.kilojoule.core.generation.step.test;

import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.registry.Blocks;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;
import ionium.util.MathHelper;

public class TripleHeightmapStep extends Step {

	public static final int STEP_FORWARD_AMT = 8;

	int x = 0;

	public TripleHeightmapStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {
		// Notch's method of heightmaps
		// one for height, roughness, detail
		// (elevation + (roughness*detail)) * coefficient + sealevel

		double elevation1 = world.universe.simplexNoise.eval(x * 0.025f, 0);
		double roughness1 = world.universe.simplexNoise.eval(x * 0.085f,
				world.worldHeight * 0.085f);
		double detail1 = world.universe.simplexNoise.eval(x * 0.075f,
				world.worldHeight * 0.08f * 2f);

		double elevation2 = world.universe.simplexNoise.eval((x + STEP_FORWARD_AMT) * 0.025f, 0);
		double roughness2 = world.universe.simplexNoise.eval((x + STEP_FORWARD_AMT) * 0.085f,
				world.worldHeight * 0.085f);
		double detail2 = world.universe.simplexNoise.eval((x + STEP_FORWARD_AMT) * 0.075f,
				world.worldHeight * 0.08f * 2f);

		for (int i = 0; i < STEP_FORWARD_AMT; i++) {
			double elevation = MathHelper.lerp(elevation1, elevation2, i * 1f / STEP_FORWARD_AMT);
			double roughness = MathHelper.lerp(roughness1, roughness2, i * 1f / STEP_FORWARD_AMT);
			double detail = MathHelper.lerp(detail1, detail2, i * 1f / STEP_FORWARD_AMT);

			double actualHeight = (elevation + (roughness * detail)) / 0.035f
					+ generator.settings.seaLevel;

			buffer.fillRect(0.25f, 0.25f, 0.25f, x + i, 0, 1, (int) actualHeight);

			if (i == 0) {
				buffer.fillRect(1f, 0.25f, 0.25f, x + i, 0, 1, (int) actualHeight);
			}
		}

		setPercentage((x * 1f) / world.worldWidth);
		x += STEP_FORWARD_AMT;
	}

	@Override
	public String getMessageString() {
		return "fine details and stuff: ";
	}

}
