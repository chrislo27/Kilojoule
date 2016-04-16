package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class BlockStone extends Block {

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("stone", new Texture("images/blocks/stone.png"));
	}

}
