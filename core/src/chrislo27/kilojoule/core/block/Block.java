package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.RenderBlock;

public abstract class Block {
	
	public static final int TILE_SIZE = 32;

	protected RenderBlock renderBlock;

	public Block() {

	}

	public RenderBlock getRenderBlock() {
		return renderBlock;
	}

	public void getRequiredTextures(ObjectMap<String, Texture> map) {

	}

}
