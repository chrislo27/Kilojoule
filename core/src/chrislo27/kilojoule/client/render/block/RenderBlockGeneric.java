package chrislo27.kilojoule.client.render.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.registry.Blocks;
import chrislo27.kilojoule.core.world.World;

public class RenderBlockGeneric extends BlockRenderer {

	public final String blockTex;

	public RenderBlockGeneric(String tex) {
		blockTex = tex;
	}

	@Override
	public void render(Batch batch, World world, int x, int y) {
		batch.draw(Blocks.getRegion(blockTex), x, y, 1, 1);
	}

}
