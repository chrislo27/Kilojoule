package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.utils.Array;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import chrislo27.kilojoule.core.nbt.NBTSaveable;
import chrislo27.kilojoule.core.world.World;
import ionium.aabbcollision.PhysicsBody;
import ionium.templates.Main;
import ionium.util.quadtree.QuadRectangleable;

public abstract class Entity implements QuadRectangleable, NBTSaveable {

	public World world;

	public PhysicsBody physicsBody = new PhysicsBody();

	public Entity(World world, float x, float y, float width, float height) {
		this.world = world;
		physicsBody.setBounds(x, y, width, height);

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
