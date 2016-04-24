package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.RenderBlockGeneric;

public class BlockDirt extends Block {

	public BlockDirt() {
		this.renderBlock = new RenderBlockGeneric("dirt");
		this.mapColor.set(132 / 255f, 84 / 255f, 50 / 255f, 1);
	}

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("dirt", new Texture("images/blocks/dirt.png"));
	}

}
