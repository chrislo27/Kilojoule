package chrislo27.kilojoule.client.render.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.world.World;

public abstract class BlockRenderer {

	public abstract void render(Batch batch, World world, int x, int y);

}
