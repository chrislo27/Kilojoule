package chrislo27.kilojoule.core.generation.step;

import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.world.World;
import ionium.util.MathHelper;
import ionium.util.i18n.Localization;

public class CaveStep extends Step {

	int x = 0;

	public CaveStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {

		for (int y = 0; y < world.worldHeight; y++) {
			float thickness = MathHelper.lerp(generator.settings.caveThicknessBottom,
					generator.settings.caveThicknessSealevel,
					MathUtils.clamp(y * 1f / generator.settings.seaLevel, 0f, 1f));
			double sample1 = world.universe.simplexNoise.eval(
					x * generator.settings.caveSampleCoefficientWidth / thickness,
					y * generator.settings.caveSampleCoefficientHeight / thickness);

			sample1 = Math.abs(sample1);

			if (sample1 >= generator.settings.caveSampleFrom
					&& sample1 <= generator.settings.caveSampleTo) {
				world.setBlock(null, x, y);
				buffer.setPixel(0, 0, 0, x, y);
			}
		}

		x++;
		setPercentage(x * 1f / world.worldWidth);
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.caves");
	}

}
