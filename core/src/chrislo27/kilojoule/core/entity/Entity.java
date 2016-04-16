package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ionium.util.quadtree.QuadRectangleable;

public abstract class Entity implements QuadRectangleable {

	public Rectangle boundingBox = new Rectangle();
	public Vector2 velocity = new Vector2(0, 0);

	public Entity(float x, float y, float width, float height) {
		boundingBox.set(x, y, width, height);
	}

	@Override
	public float getX() {
		return boundingBox.x;
	}

	@Override
	public float getY() {
		return boundingBox.y;
	}

	@Override
	public float getWidth() {
		return boundingBox.width;
	}

	@Override
	public float getHeight() {
		return boundingBox.height;
	}

}
