package chrislo27.kilojoule.core.entity.living;

import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.world.World;

public abstract class EntityLiving extends Entity {

	public EntityLiving(World world, float x, float y, float width, float height) {
		super(world, x, y, width, height);
	}

}
