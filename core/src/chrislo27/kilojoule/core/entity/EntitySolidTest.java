package chrislo27.kilojoule.core.entity;

import chrislo27.kilojoule.client.render.entity.RenderSolidTest;
import chrislo27.kilojoule.core.chunk.IChunkLoader;
import chrislo27.kilojoule.core.world.World;

public class EntitySolidTest extends Entity implements IChunkLoader {

	public EntitySolidTest(World world, float x, float y, float width, float height) {
		super(world, x, y, width, height);

		this.renderer = new RenderSolidTest(this);
	}

}
