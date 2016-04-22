package chrislo27.kilojoule.core.biome;

import com.badlogic.gdx.graphics.Color;

public class Biome {

	public Color foliageColor = new Color();
	public float hillCoefficient = 1;

	public Biome(Color foliage) {
		foliageColor = new Color(foliage.r, foliage.g, foliage.b, 1);
	}

}
