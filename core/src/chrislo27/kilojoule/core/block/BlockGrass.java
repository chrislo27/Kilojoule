package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.RenderGrass;

public class BlockGrass extends BlockDirt {

	public BlockGrass() {
		this.renderBlock = new RenderGrass("dirt", "grass");
	}

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("grass", new Texture("images/blocks/grass.png"));
	}

}
