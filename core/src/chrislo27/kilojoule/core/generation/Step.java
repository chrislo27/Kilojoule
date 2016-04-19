package chrislo27.kilojoule.core.generation;

public abstract class Step {

	private float percentageComplete = 0;
	protected final DimensionGenerator generator;

	public String messageString = "";

	public Step(DimensionGenerator gen) {
		generator = gen;
	}

	public abstract void step();

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
