package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.RenderBlockGeneric;

public class BlockStone extends Block {

	public BlockStone() {
		this.renderBlock = new RenderBlockGeneric("stone");
		this.mapColor.set(0.5f, 0.5f, 0.5f, 1);
	}

	@Override
	public void getRequiredTextures(ObjectMap<String, Texture> map) {
		map.put("stone", new Texture("images/blocks/stone.png"));
	}

}
