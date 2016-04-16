package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.BlockRenderer;

public abstract class Block {

	public static final int TILE_SIZE = 32;

	protected BlockRenderer renderBlock;

	public Block() {

	}

	public BlockRenderer getRenderBlock() {
		return renderBlock;
	}

	public abstract void getRequiredTextures(ObjectMap<String, Texture> map);

}
