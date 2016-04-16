package chrislo27.kilojoule.client.render.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.dimension.Dimension;

public abstract class RenderBlock {

	public abstract void render(Batch batch, Dimension dim, int x, int y);

}
