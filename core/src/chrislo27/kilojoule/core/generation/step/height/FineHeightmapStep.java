package chrislo27.kilojoule.core.generation.step.height;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.world.World;
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
				+ generator.settings.seaLevel;

		actualHeights[x] = actualHeight;

		float color = (x % lastStep.interval == 0 ? 0.4f : 0.25f);
		buffer.fillRect(color, color, color, x, 0, 1, (int) actualHeight);

		x++;
		setPercentage((x * 1f) / world.worldWidth);
	}

	@Override
	public void onStart(Step lastStep, WorldLoadingBuffer buffer) {
		super.onStart(lastStep, buffer);

		buffer.clear(0, 0, 0);

		if (lastStep instanceof RoughHeightmapStep) {
			this.lastStep = (RoughHeightmapStep) lastStep;
			actualHeights = new double[this.generator.world.worldWidth];
		} else {
			throw new IllegalArgumentException("Previous step must be of RoughHeightmapStep");
		}
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.heightmapFine");
	}

}
