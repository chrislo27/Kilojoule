package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class BlockGrass extends BlockDirt {

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("grass", new Texture("images/blocks/grass.png"));
	}

}
