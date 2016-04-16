package chrislo27.kilojoule.client.render.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.dimension.Dimension;
import chrislo27.kilojoule.core.registry.Blocks;

public class RenderGrass extends BlockRenderer {

	public final String base;
	public final String foliage;

	public RenderGrass(String base, String foliage) {
		this.base = base;
		this.foliage = foliage;
	}

	@Override
	public void render(Batch batch, Dimension dim, int x, int y) {
		batch.draw(Blocks.getRegion(base), x, y, 1, 1);
		batch.setColor(dim.getFoliageColor());
		batch.draw(Blocks.getRegion(foliage), x, y, 1, 1);
		batch.setColor(1, 1, 1, 1);
	}

}
