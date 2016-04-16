package chrislo27.kilojoule.core.world;

import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.dimension.Dimension;

public class World {

	public ObjectMap<String, Dimension> dimensions = new ObjectMap<>();
	public String playerDimension = "overworld";

	public World() {
		dimensions.put("overworld", new Dimension(System.currentTimeMillis(), 1600, 800));
	}

	public Dimension getCurrentDim() {
		return dimensions.get(playerDimension);
	}

}
