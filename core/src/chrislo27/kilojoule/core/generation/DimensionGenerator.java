package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.dimension.Dimension;

/**
 * Generates a dimension by calling #step() continuously until it is done.
 * <br>
 * A dimension should provide an array of ordered Steps to follow.
 *
 */
public class DimensionGenerator {

	public final Dimension dimension;

	private int currentStep = 0;
	private Array<Step> steps;
	private float cachedPercentage = 0;

	public DimensionGenerator(Dimension dim, Array<Step> steps) {
		this.dimension = dim;
		this.steps = steps;
	}

	public void step(FrameBuffer fbuffer) {

		if (!isFinished()) {
			steps.get(currentStep).step(fbuffer);

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

	/**
	 * Will block until the world has been generated.
	 */
	public void finishLoading(FrameBuffer fbuffer) {
		while (!isFinished()) {
			step(fbuffer);
		}
	}

	public boolean isFinished() {
		return getTotalPercentage() >= 1;
	}

}
