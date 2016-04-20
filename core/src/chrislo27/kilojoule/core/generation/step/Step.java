package chrislo27.kilojoule.core.generation.step;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;

public abstract class Step {

	private float percentageComplete = 0;
	protected final WorldGenerator generator;

	public String messageString = "";

	public Step(WorldGenerator gen) {
		generator = gen;
	}

	public abstract void step(WorldLoadingBuffer buffer);

	public abstract void updateMessageString();

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
