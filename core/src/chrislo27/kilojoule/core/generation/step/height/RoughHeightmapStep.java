package chrislo27.kilojoule.core.generation.step.height;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;
import ionium.util.MathHelper;
import ionium.util.i18n.Localization;

public class RoughHeightmapStep extends Step {

	public int interval = 8;
	public float heightsFactor = 0.025f;
	public float roughnessFactor = 0.085f;
	public float detailFactor = 0.075f;

	protected double[] heights = null;
	protected double[] roughness = null;
	protected double[] detail = null;

	private int current = -1;

	public RoughHeightmapStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {
		if (current <= -1) {
			heights = new double[(world.worldWidth / interval) + 1];
			roughness = new double[heights.length];
			detail = new double[roughness.length];

			current = 0;
		}

		if (current < heights.length) {

			heights[current] = world.universe.simplexNoise.eval(current * interval * heightsFactor,
					0);
			roughness[current] = world.universe.simplexNoise
					.eval(current * interval * roughnessFactor, world.worldHeight);
			detail[current] = world.universe.simplexNoise.eval(current * interval * detailFactor,
					world.worldHeight * 2);

		}

		setPercentage(current * 1f / heights.length);
		current++;
	}

	public double getValue(double[] set, int x) {
		return MathHelper.lerp(set[x / interval], set[Math.min(x / interval + 1, set.length - 1)],
				(x % interval) / (interval * 1f));
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.heightmapRough");
	}

}
