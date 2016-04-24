package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import chrislo27.kilojoule.core.nbt.NBTSaveable;
import chrislo27.kilojoule.core.world.World;
import ionium.util.quadtree.QuadRectangleable;

public abstract class Entity implements QuadRectangleable, NBTSaveable {

	public World world;

	public Rectangle boundingBox = new Rectangle();
	public Vector2 velocity = new Vector2(0, 0);

	public Entity(World world, float x, float y, float width, float height) {
		this.world = world;
		boundingBox.set(x, y, width, height);
	}

	public void tickUpdate() {

	}

	public boolean shouldBeRemoved() {
		return false;
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
