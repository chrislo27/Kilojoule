package chrislo27.kilojoule.core.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.render.block.BlockRenderer;
import chrislo27.kilojoule.core.lighting.LightUtils;
import chrislo27.kilojoule.core.world.World;
import ionium.aabbcollision.PhysicsBody;

public abstract class Block {

	public static final int TILE_SIZE = 32;
	public static final int LIGHT_DECREASE_AIR = 1;
	public static final int LIGHT_DECREASE_SOLID = 8;

	protected BlockRenderer renderBlock;
	protected Color mapColor = new Color(1, 1, 1, 1);
	public int lightDecreaseAmount = LIGHT_DECREASE_SOLID;
	public int lightEmission = LightUtils.rgblsToInt(0, 0, 0, 0, 0);
	public float friction = 1;

	public Block() {

	}

	public BlockRenderer getRenderBlock() {
		return renderBlock;
	}

	public Color getMapColor(Color foliage) {
		return mapColor;
	}

	public PhysicsBody getPhysicsBody(PhysicsBody body, int x, int y) {
		return body.setBounds(x, y, 1, 1);
	}

	public abstract void getRequiredTextures(ObjectMap<String, Texture> map);

}
