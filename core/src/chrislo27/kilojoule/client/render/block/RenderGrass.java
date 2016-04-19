package chrislo27.kilojoule.client.render.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.registry.Blocks;
import chrislo27.kilojoule.core.world.World;

public class RenderGrass extends BlockRenderer {

	public final String base;
	public final String foliage;

	public RenderGrass(String base, String foliage) {
		this.base = base;
		this.foliage = foliage;
	}

	@Override
	public void render(Batch batch, World world, int x, int y) {
		batch.draw(Blocks.getRegion(base), x, y, 1, 1);
		batch.setColor(world.getFoliageColor());
		batch.draw(Blocks.getRegion(foliage), x, y, 1, 1);
		batch.setColor(1, 1, 1, 1);
	}

}
