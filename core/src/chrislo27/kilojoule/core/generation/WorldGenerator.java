package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.step.BiomeStep;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.generation.step.height.FineHeightmapStep;
import chrislo27.kilojoule.core.generation.step.height.RoughHeightmapStep;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;

/**
 * Generates a world by calling #step() continuously until it is done.
 * <br>
 * A world should provide an array of ordered Steps to follow.
 *
 */
public class WorldGenerator {

	public final World world;
	private final String worldId;
	public final GeneratorSettings settings;

	private int currentStep = 0;
	private int lastStep = -1;
	private Array<Step> steps = new Array<>();
	private float cachedPercentage = 0;
	private long lastStepTime = 0;
	private long totalTime = 0;
	private boolean firstStep = true;

	public WorldGenerator(String wid, World w, GeneratorSettings settings) {
		this.world = w;
		this.worldId = wid;
		this.settings = settings;
		setSteps();
	}

	public void setSteps() {
		steps.clear();

		steps.add(new BiomeStep(this));
		steps.add(new RoughHeightmapStep(this));
		steps.add(new FineHeightmapStep(this));
	}

	public void step(WorldLoadingBuffer buffer) {

		if (firstStep) {
			firstStep = false;
			Main.logger.info("Starting world generation for \"" + worldId + "\" ("
					+ world.getClass().getSimpleName() + "), " + steps.size + " steps");
		}

		if (!isFinished()) {
			long time = System.nanoTime();

			if (lastStep < currentStep) {
				lastStep = currentStep;
				if (currentStep > 0) {
					steps.get(currentStep).onStart(steps.get(currentStep - 1), buffer);
				}
			}

			steps.get(currentStep).step(world, buffer);

			long finishTime = System.nanoTime() - time;
			lastStepTime += finishTime;
			totalTime += finishTime;

			if (steps.get(currentStep).isFinished()) {
				Main.logger.info("Finished world generation step "
						+ steps.get(currentStep).getClass().getSimpleName() + " in "
						+ (lastStepTime / 1_000_000f) + " ms (" + (currentStep + 1) + " / "
						+ steps.size + ")");

				if (currentStep < steps.size - 1) {
					currentStep++;
					lastStepTime = 0;
				} else {
					Main.logger.info("Completed generating world \"" + worldId + "\" ("
							+ world.getClass().getSimpleName() + ") in " + (totalTime / 1_000_000f)
							+ " ms");
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
