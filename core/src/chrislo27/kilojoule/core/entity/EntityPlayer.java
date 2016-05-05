package chrislo27.kilojoule.core.entity;

import com.badlogic.gdx.math.Vector2;

import chrislo27.kilojoule.client.render.entity.EntityPlayerRenderer;
import chrislo27.kilojoule.core.chunk.IChunkLoader;
import chrislo27.kilojoule.core.world.World;
import ionium.util.MathHelper;

public class EntityPlayer extends Entity implements IChunkLoader {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y, 1, 1);

		this.renderer = new EntityPlayerRenderer(this);
		this.maxSpeed.set(10, 10);
		this.accSpeed.set(maxSpeed.x * 5, maxSpeed.y * 5);
		this.jumpHeight = MathHelper.getJumpVelo(Math.abs(world.gravity.y), 3.25f);
	}

}
