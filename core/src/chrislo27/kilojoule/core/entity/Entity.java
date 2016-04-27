package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.nbt.NBTSaveable;
import chrislo27.kilojoule.core.world.World;
import ionium.aabbcollision.PhysicsBody;
import ionium.registry.GlobalVariables;
import ionium.util.CoordPool;
import ionium.util.Coordinate;
import ionium.util.quadtree.QuadRectangleable;

public abstract class Entity implements QuadRectangleable, NBTSaveable {

	protected static final Array<Coordinate> tempCoordArray = new Array<>();
	protected static final Array<PhysicsBody> tempBodyArray = new Array<>();

	public World world;

	public PhysicsBody physicsBody = new PhysicsBody();

	public Entity(World world, float x, float y, float width, float height) {
		this.world = world;
		physicsBody.setBounds(x, y, width, height);

	}

	public void movementUpdate() {
		physicsBody.velocity.add(world.gravity.x / GlobalVariables.getInt("TICKS"),
				world.gravity.y / GlobalVariables.getInt("TICKS"));

		Vector2 collisionResult;
		Array<PhysicsBody> bodies = world.collisionResolver.getTempBodyArray();

		tempCoordArray.clear();
		tempBodyArray.clear();
		world.getAllBlocksInArea(tempCoordArray,
				physicsBody.getAreaOfTravel(world.collisionResolver.timeScale));

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

		CoordPool.freeAll(tempCoordArray);
		tempCoordArray.clear();

		for (int i = 0; i < world.getActiveEntities().size; i++) {
			Entity e = world.getActiveEntities().get(i);

			if (e == this) continue;

			bodies.add(e.physicsBody);
		}

		collisionResult = world.collisionResolver.resolveCollisionBetweenBodies(this.physicsBody,
				bodies);

		physicsBody.bounds.x = collisionResult.x;
		physicsBody.bounds.y = collisionResult.y;

		if (world.collisionResolver.wasLastResolutionACollision()) {
			physicsBody.velocity.setZero();
		}

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
