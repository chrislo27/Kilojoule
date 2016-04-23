package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.generation.step.height.FineHeightmapStep;
import chrislo27.kilojoule.core.generation.step.height.RoughHeightmapStep;
import chrislo27.kilojoule.core.generation.step.test.TripleHeightmapStep;
import chrislo27.kilojoule.core.world.World;

/**
 * Generates a world by calling #step() continuously until it is done.
 * <br>
 * A world should provide an array of ordered Steps to follow.
 *
 */
public class WorldGenerator {

	public final World world;
	public final GeneratorSettings settings;

	private int currentStep = 0;
	private int lastStep = -1;
	private Array<Step> steps = new Array<>();
	private float cachedPercentage = 0;

	public WorldGenerator(World w, GeneratorSettings settings) {
		this.world = w;
		this.settings = settings;
		setSteps();
	}

	public void setSteps() {
		steps.clear();

		steps.add(new RoughHeightmapStep(this));
		steps.add(new FineHeightmapStep(this));
	}

	public void step(WorldLoadingBuffer buffer) {

		if (!isFinished()) {
			if (lastStep < currentStep) {
				lastStep = currentStep;
				if (currentStep > 0) {
					steps.get(currentStep).onStart(steps.get(currentStep - 1));
				}
			}
			steps.get(currentStep).step(world, buffer);

			if (steps.get(currentStep).isFinished()) {
				if (currentStep < steps.size - 1) {
					currentStep++;
				}
			}

			recalculateTotalPercentage();
		}

	}

	public void recalculateTotalPercentage() {
		float total = 0;

		for (Step s : steps) {
			total += s.getPercentage();
		}

		cachedPercentage = total / steps.size;
	}

	public float getTotalPercentage() {
		return cachedPercentage;
	}

	public float getStepPercentage() {
		return steps.get(currentStep).getPercentage();
	}

	public Step getCurrentStep() {
		return steps.get(currentStep);
	}

	/**
	 * Will block until the world has been generated.
	 */
	public void finishLoading(WorldLoadingBuffer buffer) {
		while (!isFinished()) {
			step(buffer);
		}
	}

	public boolean isFinished() {
		return getTotalPercentage() >= 1;
	}

}
