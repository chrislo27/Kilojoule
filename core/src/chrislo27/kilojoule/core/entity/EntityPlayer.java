package chrislo27.kilojoule.core.entity;

import chrislo27.kilojoule.client.render.entity.RenderPlayer;
import chrislo27.kilojoule.core.chunk.IChunkLoader;
import chrislo27.kilojoule.core.entity.living.EntityLiving;
import chrislo27.kilojoule.core.world.World;
import ionium.util.MathHelper;

public class EntityPlayer extends EntityLiving implements IChunkLoader {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y, 2, 2);

		this.renderer = new RenderPlayer(this);
		this.maxSpeed.set(10, 10);
		this.accSpeed.set(maxSpeed.x * 5, maxSpeed.y * 5);
		this.jumpHeight = MathHelper.getJumpVelo(Math.abs(world.gravity.y),
				2f + physicsBody.bounds.height);
	}

}
