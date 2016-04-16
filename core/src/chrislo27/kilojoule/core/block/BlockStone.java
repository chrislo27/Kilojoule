package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.RenderBlockGeneric;

public class BlockStone extends Block {

	public BlockStone() {
		this.renderBlock = new RenderBlockGeneric("stone");
	}

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("stone", new Texture("images/blocks/stone.png"));
	}

}
