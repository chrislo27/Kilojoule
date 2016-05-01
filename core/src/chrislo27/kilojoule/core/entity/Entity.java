package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import chrislo27.kilojoule.client.render.entity.EntityRenderer;
import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.nbt.NBTSaveable;
import chrislo27.kilojoule.core.world.World;
import ionium.aabbcollision.PhysicsBody;
import ionium.registry.GlobalVariables;
import ionium.templates.Main;
import ionium.util.CoordPool;
import ionium.util.Coordinate;
import ionium.util.MathHelper;
import ionium.util.quadtree.QuadRectangleable;

public abstract class Entity implements QuadRectangleable, NBTSaveable {

	protected static final Array<Coordinate> tempCoordArray = new Array<>();
	protected static final Array<PhysicsBody> tempBodyArray = new Array<>();

	public World world;
	public EntityRenderer renderer;
	public PhysicsBody physicsBody = new PhysicsBody();
	public transient Vector2 lastKnownPosition = new Vector2();
	public Vector2 collidingPosition = new Vector2();

	public Vector2 maxSpeed = new Vector2(10, 10);
	public Vector2 accSpeed = new Vector2(maxSpeed.x * 2, maxSpeed.y * 2);
	public float jumpHeight = MathHelper.getJumpVelo(9.8f, 1);

	public Entity(World world, float x, float y, float width, float height) {
		this.world = world;
		physicsBody.setBounds(x, y, width, height);
		lastKnownPosition.set(x, y);
	}

	public void movementUpdate() {
		lastKnownPosition.set(physicsBody.bounds.x, physicsBody.bounds.y);
//		physicsBody.velocity.add(world.gravity.x / GlobalVariables.ticks,
//				world.gravity.y / GlobalVariables.ticks);

		Vector2[] collisionResult;
		Array<PhysicsBody> bodies = world.collisionResolver.getTempBodyArray();

		// prep temp arrays
		tempCoordArray.clear();
		tempBodyArray.clear();

		// set tmp1 to the original bounds
		Rectangle.tmp.set(physicsBody.bounds);

		// expand the bounds so we have blocks around us
		physicsBody.bounds.x -= 1;
		physicsBody.bounds.y -= 1;
		physicsBody.bounds.width += 2;
		physicsBody.bounds.height += 2;

		// add the coordinates from CoordPool
		world.getAllBlocksInArea(tempCoordArray,
				physicsBody.getAreaOfTravel(world.collisionResolver.timeScale));

		// set tmp2 to the expanded bounds
		Rectangle.tmp2.set(physicsBody.bounds);
		// reset bounds
		physicsBody.bounds.set(Rectangle.tmp);

		// add all the physicsbodies to the array
		for (int i = 0; i < tempCoordArray.size; i++) {
			Coordinate c = tempCoordArray.get(i);
			Block b = world.getBlock(c.getX(), c.getY());

			if (b == null) {
				continue;
			}

			PhysicsBody body = world.physicsBodyPool.obtain();
			PhysicsBody result = b.getPhysicsBody(body, c.getX(), c.getY());

			if (result == null) {
				world.physicsBodyPool.free(body);

				continue;
			}

			tempBodyArray.add(result);
			bodies.add(result);
		}

		// free coordinates, we're done
		CoordPool.freeAll(tempCoordArray);
		tempCoordArray.clear();

		// add entity physics
		for (int i = 0; i < world.getActiveEntities().size; i++) {
			Entity e = world.getActiveEntities().get(i);

			if (e == this) continue;
			if (!e.physicsBody.bounds.overlaps(Rectangle.tmp2)) continue;

			bodies.add(e.physicsBody);
		}

		// resolve collision
		collisionResult = world.collisionResolver.resolveCollisionBetweenBodies(this.physicsBody,
				bodies, Rectangle.tmp2);

		// set position
		collidingPosition.set(collisionResult[0]);

		//physicsBody.velocity.set(collisionResult[1]);

		physicsBody.bounds.x += physicsBody.velocity.x * world.collisionResolver.timeScale;
		physicsBody.bounds.y += physicsBody.velocity.y * world.collisionResolver.timeScale;

		// free physicsbodies
		world.physicsBodyPool.freeAll(tempBodyArray);
		tempBodyArray.clear();
	}

	public void tickUpdate() {

	}

	public boolean shouldBeRemoved() {
		return false;
	}

	@Override
	public void writeToNBT(TagCompound compound) {
	}

	@Override
	public void readFromNBT(TagCompound compound)
			throws TagNotFoundException, UnexpectedTagTypeException {
	}

	public void move(float amtX, float amtY) {
		if (Math.abs(physicsBody.velocity.x) <= maxSpeed.x && amtX != 0) {
			physicsBody.velocity.x = MathUtils.clamp(physicsBody.velocity.x + amtX * accSpeed.x,
					-maxSpeed.x, maxSpeed.x);
		}

		if (Math.abs(physicsBody.velocity.y) <= maxSpeed.y && amtY != 0) {
			physicsBody.velocity.y = MathUtils.clamp(physicsBody.velocity.y + amtY * accSpeed.y,
					-maxSpeed.y, maxSpeed.y);
		}
	}

	public void jump() {
		physicsBody.velocity.y += jumpHeight;
	}

	@Override
	public float getX() {
		return physicsBody.bounds.x;
	}

	@Override
	public float getY() {
		return physicsBody.bounds.y;
	}

	@Override
	public float getWidth() {
		return physicsBody.bounds.width;
	}

	@Override
	public float getHeight() {
		return physicsBody.bounds.height;
	}

}
