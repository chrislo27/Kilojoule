package chrislo27.kilojoule.core.entity;

import chrislo27.kilojoule.core.chunk.IChunkLoader;
import chrislo27.kilojoule.core.world.World;

public class EntityPlayer extends Entity implements IChunkLoader {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y, 1, 1);
	}

}
