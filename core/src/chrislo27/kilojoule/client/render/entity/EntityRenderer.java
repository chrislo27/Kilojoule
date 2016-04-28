package chrislo27.kilojoule.client.render.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.world.World;
import ionium.registry.GlobalVariables;
import ionium.templates.Main;

public abstract class EntityRenderer<T extends Entity> {

	protected static final float THRESHOLD = 1f / Block.TILE_SIZE;
	protected static final Vector2 tmpVector = new Vector2();
	protected static final float LERP_SPEED = 8f;
	protected static float tickRate = -1;

	public final T entity;

	protected Vector2 lerpPosition = new Vector2();

	public EntityRenderer(T entity) {
		this.entity = entity;

		if (tickRate < 0) tickRate = 1f / GlobalVariables.getInt("TICKS");

		lerpPosition.set(entity.physicsBody.bounds.x, entity.physicsBody.bounds.y);
	}

	public void updateLerpPosition() {
		lerpPosition.set(entity.lastKnownPosition);
		lerpPosition.lerp(tmpVector.set(entity.physicsBody.bounds.x, entity.physicsBody.bounds.y),
				Main.tickDeltaTime / tickRate);
	}

	public abstract void render(Batch batch, World world);

}
