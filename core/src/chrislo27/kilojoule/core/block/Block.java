package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.BlockRenderer;
import chrislo27.kilojoule.core.world.World;

public abstract class Block {

	public static final int TILE_SIZE = 32;

	protected BlockRenderer renderBlock;
	protected Color mapColor = new Color(1, 1, 1, 1);

	public Block() {

	}

	public BlockRenderer getRenderBlock() {
		return renderBlock;
	}

	public Color getMapColor(World world, int x, int y) {
		return mapColor;
	}

	public abstract void getRequiredTextures(ObjectMap<String, Texture> map);

}
