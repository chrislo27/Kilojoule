package chrislo27.kilojoule.core.generation.step;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.world.World;

public abstract class Step {

	private float percentageComplete = 0;
	protected final WorldGenerator generator;

	public Step(WorldGenerator gen) {
		generator = gen;
	}

	public abstract void step(World world, WorldLoadingBuffer buffer);

	public abstract String getMessageString();

	public void onStart(Step lastStep, WorldLoadingBuffer buffer) {

	}

	public boolean isFinished() {
		return percentageComplete >= 1;
	}

	public void setPercentage(float p) {
		percentageComplete = p;
	}

	public float getPercentage() {
		return percentageComplete;
	}

}
