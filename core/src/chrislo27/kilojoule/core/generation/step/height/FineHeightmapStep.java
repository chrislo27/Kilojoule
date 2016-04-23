package chrislo27.kilojoule.core.generation.step.height;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.biome.Biome;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;
import ionium.util.MathHelper;
import ionium.util.i18n.Localization;

public class FineHeightmapStep extends Step {

	protected double actualHeights[];

	int x = 0;
	private RoughHeightmapStep lastStep;

	public FineHeightmapStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {

		double elevation = lastStep.getValue(lastStep.heights, x);
		double roughness = lastStep.getValue(lastStep.roughness, x);
		double detail = lastStep.getValue(lastStep.detail, x);

		double actualHeight = (elevation + (roughness * detail)) / (lastStep.heightsFactor * 2f)
				* world.getBiome(x).generatorSettings.hillAmplification
				+ generator.settings.seaLevel;

		actualHeights[x] = actualHeight;

		drawHeight(x, actualHeight, buffer);

		x++;
		setPercentage((x * 1f) / world.worldWidth);

		if (getPercentage() >= 1) {
			smoothBiomeBorders(world, buffer);
		}
	}

	private void drawHeight(int x, double actualHeight, WorldLoadingBuffer buffer) {
		float color = (x % lastStep.interval == 0 ? 0.4f : 0.25f);
		buffer.fillRect(color, color, color, x, 0, 1, (int) actualHeight);
	}

	private void smoothBiomeBorders(World world, WorldLoadingBuffer buffer) {
		if (lastStep.interval <= 1) return;

		int x = 0;
		Biome lastBiome = world.getBiome(0);

		while (x < world.worldWidth) {

			Biome current = world.getBiome(x);

			if (current != lastBiome) {
				int pointOfChange = x;
				int left = Math.max(0, x - lastStep.interval);
				int right = Math.min(actualHeights.length - 1, x + lastStep.interval - 1);
				double leftHeight = actualHeights[left];
				double rightHeight = actualHeights[right];

				for (int i = left; i <= right; i++) {
					actualHeights[i] = MathHelper.lerp(leftHeight, rightHeight,
							((i - left) * 1f) / (right - left));

					drawHeight(i, actualHeights[i], buffer);
				}

				buffer.fillRect(0, 0, 0, pointOfChange, 0, 1, world.worldHeight);
				buffer.fillRect(0.8f, 0.25f, 0.25f, pointOfChange, 0, 1,
						(int) actualHeights[pointOfChange]);

			}

			lastBiome = current;

			x++;

		}

	}

	@Override
	public void onStart(Step lastStep, WorldLoadingBuffer buffer) {
		super.onStart(lastStep, buffer);

		buffer.clear(0, 0, 0);

		if (lastStep instanceof RoughHeightmapStep) {
			this.lastStep = (RoughHeightmapStep) lastStep;
			actualHeights = new double[this.generator.world.worldWidth];

			for (int i = 0; i < actualHeights.length; i++) {
				actualHeights[i] = -999;
			}
		} else {
			throw new IllegalArgumentException("Previous step must be of RoughHeightmapStep");
		}
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.heightmapFine");
	}

}
