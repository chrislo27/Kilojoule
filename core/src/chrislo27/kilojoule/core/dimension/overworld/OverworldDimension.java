package chrislo27.kilojoule.core.dimension.overworld;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.dimension.Dimension;
import chrislo27.kilojoule.core.generation.Step;

public class OverworldDimension extends Dimension {

	public OverworldDimension(long seed, int sizex, int sizey) {
		super(seed, sizex, sizey);
	}

	@Override
	public void provideGenerationSteps(Array<Step> steps) {
	}

}
