package chrislo27.kilojoule.core.generation.step.test;

import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.registry.Blocks;
import chrislo27.kilojoule.core.world.World;

public class WeightedNoiseGenerationStep extends Step {

	int x = 0;
	boolean hasFractaled = false;

	public WeightedNoiseGenerationStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {

		if (!hasFractaled) {
			hasFractaled = true;
			fractal(world);
		}

		for (int y = 0; y < world.worldHeight; y++) {
			double density = world.universe.simplexNoise.eval(x * 0.035f, y * 0.035f);
			density += MathUtils.random(0.015f) * MathUtils.randomSign();

			density += (y * 1f / (generator.settings.seaLevel - y));

			if (density >= 0) {
				world.setBlock(Blocks.getBlock("stone"), x, y);
				buffer.setPixel(0.25f, 0.25f, 0.25f, x, y);
			}
		}

		setPercentage((x * 1f) / world.worldWidth);
		x++;
	}

	private void fractal(World world) {

	}

	@Override
	public String getMessageString() {
		return "noising: ";
	}

}
