package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public abstract class Step {

	private float percentageComplete = 0;
	protected final WorldGenerator generator;

	public String messageString = "";

	public Step(WorldGenerator gen) {
		generator = gen;
	}

	public abstract void step(FrameBuffer fbuffer);

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
