package chrislo27.kilojoule.core.generation.step.height;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.world.World;
import ionium.util.MathHelper;
import ionium.util.i18n.Localization;

public class FineHeightmapStep extends Step {

	int x = 0;
	private RoughHeightmapStep lastStep;

	public FineHeightmapStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {
		// Notch's method of heightmaps
		// one for height, roughness, detail
		// (elevation + (roughness*detail)) * coefficient + sealevel

		double elevation = lastStep.getValue(lastStep.heights, x);
		double roughness = lastStep.getValue(lastStep.roughness, x);
		double detail = lastStep.getValue(lastStep.detail, x);

		double actualHeight = (elevation + (roughness * detail)) / 0.035f
				+ generator.settings.seaLevel;

		float color = (x % lastStep.interval == 0 ? 0.8f : 0.25f);
		buffer.fillRect(color, color, color, x, 0, 1, (int) actualHeight);

		setPercentage((x * 1f) / world.worldWidth);
		x++;
	}

	@Override
	public void onStart(Step lastStep) {
		super.onStart(lastStep);

		
		
		if (lastStep instanceof RoughHeightmapStep) {
			this.lastStep = (RoughHeightmapStep) lastStep;
		}
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.heightmapFine");
	}

}
